package francisco.languagecompiler.resource;

import com.google.protobuf.FieldMask;
import francisco.languagecompiler.resource.model.Build;
import francisco.languagecompiler.resource.model.BuildOperation;
import francisco.languagecompiler.resource.model.Job;
import francisco.languagecompiler.resource.model.Operation;
import francisco.languagecompiler.resource.requests.OperationRequest;
import francisco.languagecompiler.resource.service.BuildsService;
import francisco.languagecompiler.resource.service.JobsService;
import francisco.languagecompiler.resource.service.OperationQueueService;
import francisco.languagecompiler.resource.service.OperationsService;
import francisco.languagecompiler.resource.util.ErrorResponse;
import francisco.languagecompiler.resource.util.Response;
import francisco.languagecompiler.resource.util.StringUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/v1/jobs")
public class JobsController extends BaseController {
    private final JobsService jobsService;

    public JobsController(JobsService jobsService) {
        this.jobsService = jobsService;
    }

    @GetMapping
    public ResponseEntity getJobs(
            @RequestParam(name = "fields", required = false) String fields,
            @RequestParam(name = "done", required = false) String done) {

        Stream<Job> stream = this.jobsService.getStream();

     /*   if (done != null) {
            stream = stream.filter(op ->
                    (op.isDone() == Boolean.getBoolean(done))
            );
        }*/

        FieldMask fieldMask = parseFieldMask(fields);
        return Response.okResponse(stream, fieldMask);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Object> get(
            @PathVariable String id,
            @RequestParam(name = "fields", required = false) String fields) {

        Job build = this.jobsService.get(id);

        if (build == null) {
            return ErrorResponse.builder()
                    .addError("Not found Operation")
                    .notFound();
        }

        FieldMask fieldMask = parseFieldMask(fields);
        return Response.okResponse(build, fieldMask);
    }

    @PostMapping
    public ResponseEntity create(@RequestBody Job op,
                                 @RequestParam(name = "fields", required = false) String fields) {

        ErrorResponse.Builder err = ErrorResponse.builder();

        this.jobsService.add(op);

        if (op == null) {
            err.addError("Not able to register long running operation");
        }

        if (err.hasError()) {
            return err.badRequest();
        }

        FieldMask fieldMask = parseFieldMask(fields);

        return Response.createdResponse(op, fieldMask);
    }


    @PatchMapping("/{id}")
    public ResponseEntity patch(
            @PathVariable String id,
            @RequestBody Map<String, Object> updates) {

        Job modifiedOperation = this.jobsService.get(id);

        if (modifiedOperation == null) {
            return ErrorResponse.builder()
                    .addError("Not found operation")
                    .notFound();
        }
        ErrorResponse.Builder err = ErrorResponse.builder();

        if (!updates.containsKey("metadata") || updates.get("metadata") == null) {
            return err.addError("Metadata is required for the operation").badRequest();
        }


        if (err.hasError()) {
            return err.badRequest();
        }

     /*   applyPartialUpdates(modifiedOperation, updates);
        validateBuild(modifiedOperation);*/

        if (err.hasError()) {
            return err.badRequest();
        }

        // Save the updated build

        return Response.okResponse(modifiedOperation);
    }

    @PostMapping("/{id}:run")
    public ResponseEntity run(@PathVariable String id) {
        Job op = this.jobsService.get(id);


        if (op == null) {
            return ErrorResponse.builder()
                    .addError("Not found operation")
                    .notFound();
        }

       /*

       validate is already in progress

       if (op.getMetadata().equals(BuildStatus.IN_PROGRESS)) {
            return ErrorResponse.builder()
                    .addError("Build is already in progress")
                    .badRequest();
        }*/

        //operationsQueueService.addToQueue(op);
        return ResponseEntity.ok().build();
    }
}
