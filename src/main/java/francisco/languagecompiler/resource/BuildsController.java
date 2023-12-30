package francisco.languagecompiler.resource;

import com.google.protobuf.FieldMask;
import francisco.languagecompiler.resource.model.Build;
import francisco.languagecompiler.resource.model.BuildStatus;
import francisco.languagecompiler.resource.service.BuildQueueService;
import francisco.languagecompiler.resource.service.BuildsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/v1/builds")
public class BuildsController extends BaseController {
    private final BuildsService buildsService;
    private final BuildQueueService buildQueueService;

    public BuildsController(BuildsService buildsService,
                            BuildQueueService buildQueueService) {
        this.buildsService = buildsService;
        this.buildQueueService = buildQueueService;
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getBuilds(
            @RequestParam(name = "fields", required = false) String fields,
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "name", required = false) String name) {
        FieldMask fieldMask = parseFieldMask(fields);

        Stream<Build> stream = this.buildsService.getBuildsList().stream();

        if (status != null) {
            BuildStatus statusFilter = null;

            try {
                if (status != null) {
                    statusFilter = BuildStatus.fromString(status);
                }
            } catch (IllegalArgumentException ex) {
                statusFilter = null;
            }
            BuildStatus finalStatusFilter = statusFilter;
            stream = stream.filter(build ->
                    (build.getStatus() == finalStatusFilter)
            );
        }

        if (name != null) {
            stream = stream.filter(build ->
                    (build.getName().toLowerCase().contains(name.toLowerCase()))
            );
        }

        List<Map<String, Object>> buildsMapList = stream.map(build -> build.toMap(fieldMask))
                .collect(Collectors.toList());

        return ResponseEntity.ok(buildsMapList);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getBuildById(@PathVariable String id,
                                                            @RequestParam(name = "fields", required = false) String fields) {
        FieldMask fieldMask = parseFieldMask(fields);

        Build build = this.buildsService.getBuildById(id);

        if (build == null) {
            return ResponseEntity.notFound().build();
        }

        Map<String, Object> buildMap = build.toMap(fieldMask);
        return ResponseEntity.ok(buildMap);
    }


    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody Build buildRequest) {
        if (buildRequest.getCode() == null || buildRequest.getCode().isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Code is required for the build");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        Build createdBuild = this.buildsService.createBuild(buildRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBuild.toMap());
    }

    @DeleteMapping("/{id}")
    public void deleteBuild(@PathVariable String id) {
        buildsService.removeBuildById(id);
    }


    @PostMapping("/{id}:trigger")
    public ResponseEntity triggerBuild(@PathVariable String id) {
        Build build = this.buildsService.getBuildById(id);

        if (build == null) {
            return ResponseEntity.notFound().build();
        }

        if (build.getStatus().equals(BuildStatus.IN_PROGRESS)) {
            return ResponseEntity.badRequest().body("Build is already in progress");
        }

        buildQueueService.addToQueue(build);
        return ResponseEntity.ok().build();
    }
}