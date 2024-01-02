package francisco.languagecompiler.resource;

import com.google.protobuf.FieldMask;
import francisco.languagecompiler.resource.model.Build;
import francisco.languagecompiler.resource.model.BuildLang;
import francisco.languagecompiler.resource.model.BuildOperation;
import francisco.languagecompiler.resource.model.BuildStatus;
import francisco.languagecompiler.resource.service.OperationQueueService;
import francisco.languagecompiler.resource.service.BuildsService;
import francisco.languagecompiler.resource.util.ErrorResponse;
import francisco.languagecompiler.resource.util.Response;
import francisco.languagecompiler.resource.util.StringUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Stream;

@RestController
@RequestMapping("/api/v1/builds")
public class BuildsController extends BaseController {
    private final BuildsService buildsService;
    private final OperationQueueService operationsQueueService;

    public BuildsController(BuildsService buildsService,
                            OperationQueueService operationQueueService) {
        this.buildsService = buildsService;
        this.operationsQueueService = operationQueueService;
    }

    @GetMapping
    public ResponseEntity getBuilds(
            @RequestParam(name = "fields", required = false) String fields,
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "name", required = false) String name) {

        Stream<Build> stream = this.buildsService.getStream();

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

        FieldMask fieldMask = parseFieldMask(fields);
        return Response.okResponse(stream, fieldMask);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Object> getBuildById(
            @PathVariable String id,
            @RequestParam(name = "fields", required = false) String fields) {

        Build build = this.buildsService.getBuildById(id);

        if (build == null) {
            return ErrorResponse.builder()
                    .addError("Not found build")
                    .notFound();
        }

        FieldMask fieldMask = parseFieldMask(fields);
        return Response.okResponse(build, fieldMask);
    }


    @PostMapping
    public ResponseEntity create(@RequestBody Build buildRequest,
                                 @RequestParam(name = "fields", required = false) String fields) {

        ErrorResponse.Builder err = ErrorResponse.builder();

        if (buildRequest.getLang() == BuildLang.Unknown) {
            err.addError("Lang is required for the build");
        }

        if (StringUtil.isNullOrEmpty(buildRequest.getCode())) {
            err.addError("Code is required for the build");
        }

        if (StringUtil.isNullOrEmpty(buildRequest.getName())) {
            err.addError("Name is required for the build");
        }

        if(err.hasError()){
            return err.badRequest();
        }

        Build build = this.buildsService.addbuild(buildRequest);
        FieldMask fieldMask = parseFieldMask(fields);
        return Response.createdResponse(build, fieldMask);
    }

    @DeleteMapping("/{id}")
    public void deleteBuild(@PathVariable String id) {
        buildsService.removeBuildById(id);
    }


    @PostMapping("/{id}:trigger")
    public ResponseEntity triggerBuild(@PathVariable String id) {
        Build build = this.buildsService.getBuildById(id);

        if (build == null) {
           return ErrorResponse.builder()
                    .addError("Not found build")
                    .notFound();
        }

        if (build.getStatus().equals(BuildStatus.IN_PROGRESS)) {
            return ErrorResponse.builder()
                    .addError("Build is already in progress")
                    .badRequest();
        }

        operationsQueueService.addToQueue(new BuildOperation(build));
        return ResponseEntity.ok().build();
    }
}