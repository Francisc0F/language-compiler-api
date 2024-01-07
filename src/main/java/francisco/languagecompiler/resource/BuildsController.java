package francisco.languagecompiler.resource;

import com.google.protobuf.FieldMask;
import francisco.languagecompiler.resource.model.Build;
import francisco.languagecompiler.resource.model.BuildLang;
import francisco.languagecompiler.resource.model.BuildOperation;
import francisco.languagecompiler.resource.service.OperationQueueService;
import francisco.languagecompiler.resource.service.BuildsService;
import francisco.languagecompiler.resource.util.ErrorResponse;
import francisco.languagecompiler.resource.util.Response;
import francisco.languagecompiler.resource.util.StringUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Stream;

import static francisco.languagecompiler.resource.model.BuildLang.getBuildLangFromString;

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
            @RequestParam(name = "name", required = false) String name) {

        Stream<Build> stream = this.buildsService.getStream();

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

        Build build = this.buildsService.get(id);

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

        if (buildRequest.getLanguage() == null || buildRequest.getLanguage() == BuildLang.Unknown) {
            err.addError("Lang is required for the build");
        }

        if (StringUtil.isNullOrEmpty(buildRequest.getCode())) {
            err.addError("Code is required for the build");
        }

        if (StringUtil.isNullOrEmpty(buildRequest.getName())) {
            err.addError("Name is required for the build");
        }

        if (err.hasError()) {
            return err.badRequest();
        }

        Build build = this.buildsService.add(buildRequest);
        FieldMask fieldMask = parseFieldMask(fields);
        return Response.createdResponse(build, fieldMask);
    }


    @PatchMapping("/{id}")
    public ResponseEntity patchBuild(
            @PathVariable String id,
            @RequestBody Map<String, Object> updates) {

        Build modifiedBuild = this.buildsService.get(id);

        if (modifiedBuild == null) {
            return ErrorResponse.builder()
                    .addError("Not found build")
                    .notFound();
        }
        ErrorResponse.Builder err = ErrorResponse.builder();

        if (updates.containsKey("name") && StringUtil.isNullOrEmpty((String) updates.get("name"))) {
            err.addError("Name is required for the build");
        }

        if (updates.containsKey("language")) {
            Object languageObj = updates.get("language");

            if (languageObj instanceof String) {
                String languageStr = (String) languageObj;

                // Convert the String to the corresponding BuildLang enum
                BuildLang language = getBuildLangFromString(languageStr);

                if (language == null || language == BuildLang.Unknown) {
                    err.addError("Lang is required for the build");
                }
                updates.put("language", language);
            } else {
                // Handle the case where the value is not a String (optional)
                err.addError("Invalid language value");
            }
        }

        if (err.hasError()) {
            return err.badRequest();
        }

        applyPartialUpdates(modifiedBuild, updates);
        validateBuild(modifiedBuild);

        if (err.hasError()) {
            return err.badRequest();
        }

        // Save the updated build

        return Response.okResponse(modifiedBuild);
    }

    // Helper method to apply partial updates to the existing build
    private void applyPartialUpdates(Build existingBuild, Map<String, Object> updates) {
        if (updates.containsKey("name")) {
            existingBuild.setName((String) updates.get("name"));
        }

        if (updates.containsKey("language")) {
            existingBuild.setLanguage((BuildLang) updates.get("language"));
        }

        if (updates.containsKey("code")) {
            existingBuild.setCode((String) updates.get("code"));
        }
    }

    // Helper method to validate the build
    private ErrorResponse.Builder validateBuild(Build build) {
        ErrorResponse.Builder err = ErrorResponse.builder();

        if (build.getLanguage() == null || build.getLanguage() == BuildLang.Unknown) {
            err.addError("Lang is required for the build");
        }

        if (StringUtil.isNullOrEmpty(build.getCode())) {
            err.addError("Code is required for the build");
        }

        if (StringUtil.isNullOrEmpty(build.getName())) {
            err.addError("Name is required for the build");
        }

        return err;
    }

    @DeleteMapping("/{id}")
    public void deleteBuild(@PathVariable String id) {
        buildsService.removeById(id);
    }


    @PostMapping("/{id}:trigger")
    public ResponseEntity triggerBuild(@PathVariable String id) {
        Build build = this.buildsService.get(id);

        if (build == null) {
            return ErrorResponse.builder()
                    .addError("Not found build")
                    .notFound();
        }

        //TODO: check existing operation in progress if not, create operation and run
/*
        if (build.getStatus().equals(BuildStatus.IN_PROGRESS)) {
            return ErrorResponse.builder()
                    .addError("Build is already in progress")
                    .badRequest();
        }*/

        operationsQueueService.addToQueue(new BuildOperation(build));
        return ResponseEntity.ok().build();
    }
}