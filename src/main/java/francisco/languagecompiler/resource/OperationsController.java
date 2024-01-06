package francisco.languagecompiler.resource;

import com.google.protobuf.FieldMask;
import francisco.languagecompiler.resource.model.*;
import francisco.languagecompiler.resource.requests.OperationRequest;
import francisco.languagecompiler.resource.service.BuildsService;
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

import static francisco.languagecompiler.resource.model.BuildLang.getBuildLangFromString;

@RestController
@RequestMapping("/api/v1/operations")
public class OperationsController extends BaseController {
    private final OperationsService operationsService;
    private final BuildsService buildsService;
    private final OperationQueueService operationsQueueService;

    public OperationsController(OperationsService operationsService,
                                BuildsService buildsService,
                                OperationQueueService operationQueueService) {
        this.operationsService = operationsService;
        this.buildsService = buildsService;
        this.operationsQueueService = operationQueueService;
    }

    @GetMapping
    public ResponseEntity getOperations(
            @RequestParam(name = "fields", required = false) String fields,
            @RequestParam(name = "metadata.type", required = false) String metadatatype,
            @RequestParam(name = "done", required = false) String done) {

        Stream<Operation> stream = this.operationsService.getStream();

        if (metadatatype != null) {
            stream = stream.filter(operation ->
                    (Objects.equals(operation.getMetadata().getType(), metadatatype.trim()))
            );
        }

        if (done != null) {
            stream = stream.filter(op ->
                    (op.isDone() == Boolean.getBoolean(done))
            );
        }

        FieldMask fieldMask = parseFieldMask(fields);
        return Response.okResponse(stream, fieldMask);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Object> getOperations(
            @PathVariable String id,
            @RequestParam(name = "fields", required = false) String fields) {

        Operation build = this.operationsService.get(id);

        if (build == null) {
            return ErrorResponse.builder()
                    .addError("Not found Operation")
                    .notFound();
        }

        FieldMask fieldMask = parseFieldMask(fields);
        return Response.okResponse(build, fieldMask);
    }

    @PostMapping
    public ResponseEntity create(@RequestBody Operation op,
                                 @RequestParam(name = "fields", required = false) String fields) {

        ErrorResponse.Builder err = ErrorResponse.builder();

        this.operationsService.add(op);

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

        Operation modifiedOperation = this.operationsService.get(id);

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
        Operation op = this.operationsService.get(id);


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

        operationsQueueService.addToQueue(op);
        return ResponseEntity.ok().build();
    }

    @PostMapping(":createWithBuildId")
    public ResponseEntity createFromBuild(@RequestBody OperationRequest operationRequest,
                                          @RequestParam(name = "fields", required = false) String fields) {

        ErrorResponse.Builder err = ErrorResponse.builder();

        String id = operationRequest.getBuildId();
        if (StringUtil.isNullOrEmpty(id)) {
            err.addError("Runnable ID not found, it is required to register operation");
        }

        Build build = this.buildsService.getBuildById(id);
        if (build == null) {
            err = ErrorResponse.builder();
            err.addError("Build does not exist");
            return err.badRequest();
        }

        Operation op = this.operationsService.add(new BuildOperation(build));

        if (op == null) {
            err.addError("Not able to register long running operation");
        }

        if (err.hasError()) {
            return err.badRequest();
        }

        FieldMask fieldMask = parseFieldMask(fields);

        assert op != null;
        return Response.createdResponse(op, fieldMask);
    }



    /*    @PostMapping("/{id}:wait")
    public ResponseEntity wait(@PathVariable String id) {
        Operation op = this.operationsService.get(id);


        if (op == null) {
            return ErrorResponse.builder()
                    .addError("Not found operation")
                    .notFound();
        }

        */
    /*if (op.getMetadata().equals(BuildStatus.IN_PROGRESS)) {
            return ErrorResponse.builder()
                    .addError("Build is already in progress")
                    .badRequest();
        }*/
    /*

        operationsQueueService.addToQueue(op);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}:cancel")
    public ResponseEntity cancel(@PathVariable String id) {
        Operation op = this.operationsService.get(id);


        if (op == null) {
            return ErrorResponse.builder()
                    .addError("Not found operation")
                    .notFound();
        }

        */
    /*if (op.getMetadata().equals(BuildStatus.IN_PROGRESS)) {
            return ErrorResponse.builder()
                    .addError("Build is already in progress")
                    .badRequest();
        }*/
    /*

        operationsQueueService.addToQueue(op);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}:batchrun")
    public ResponseEntity batchRun(@RequestBody BatchOperationsRequest batchRequest) {

        ArrayList<String> idsList = new ArrayList<>(Arrays.asList(batchRequest.getIds()));

        if (idsList.isEmpty()) {
            return ErrorResponse.builder()
                    .addError("No ids especified")
                    .badRequest();
        }

        ArrayList<String> setToRun = new ArrayList<>();
        idsList.forEach(e -> {
            Operation op = this.operationsService.get(e);
            if (op != null) {
                operationsQueueService.addToQueue(op);
                setToRun.add(e);
            }
        });
        return Response.createdResponse(setToRun);
    }*/
}
