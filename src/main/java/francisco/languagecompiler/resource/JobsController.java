package francisco.languagecompiler.resource;

import com.google.protobuf.FieldMask;
import francisco.languagecompiler.resource.model.*;
import francisco.languagecompiler.resource.service.BuildsService;
import francisco.languagecompiler.resource.service.JobsService;
import francisco.languagecompiler.resource.service.OperationQueueService;
import francisco.languagecompiler.resource.service.OperationsService;
import francisco.languagecompiler.resource.util.ErrorResponse;
import francisco.languagecompiler.resource.util.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/v1/jobs")
public class JobsController extends BaseController {
    private final JobsService jobsService;
    private final OperationsService operationsService;
    private final OperationQueueService operationQueueService;
    private final BuildsService buildsService;


    public JobsController(JobsService jobsService,
                          OperationsService operationsService,
                          BuildsService buildsService,
                          OperationQueueService operationQueueService) {
        this.jobsService = jobsService;
        this.operationsService = operationsService;
        this.buildsService = buildsService;
        this.operationQueueService = operationQueueService;
    }

    @GetMapping
    public ResponseEntity getJobs(
            @RequestParam(name = "fields", required = false) String fields,
            @RequestParam(name = "done", required = false) String done) {

        Stream<Job> stream = this.jobsService.getStream();

        if (done != null) {
            stream = stream.filter(op ->
                    (op.isDone() == Boolean.getBoolean(done))
            );
        }

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
    public ResponseEntity create(@RequestBody Job job,
                                 @RequestParam(name = "fields", required = false) String fields) {

        ErrorResponse.Builder err = ErrorResponse.builder();

        this.jobsService.add(job);

        if (job == null) {
            err.addError("Not able to register long running operation");
        }

        if (err.hasError()) {
            return err.badRequest();
        }

        FieldMask fieldMask = parseFieldMask(fields);

        return Response.createdResponse(job, fieldMask);
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
                    .addError("Not found Job")
                    .notFound();
        }

        Build a =buildsService.get(op.getBuildId());


        JobRunnable runnable = new JobRunnable(op, operationQueueService, a);
        runnable.setOperationsService(operationsService);
        runnable.scheduleJob();




        return ResponseEntity.ok().build();
    }
}
