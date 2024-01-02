package francisco.languagecompiler.resource;

import com.google.protobuf.FieldMask;
import francisco.languagecompiler.resource.model.Build;
import francisco.languagecompiler.resource.model.Operation;
import francisco.languagecompiler.resource.service.OperationQueueService;
import francisco.languagecompiler.resource.service.OperationsService;
import francisco.languagecompiler.resource.util.ErrorResponse;
import francisco.languagecompiler.resource.util.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.stream.Stream;
@RestController
@RequestMapping("/api/v1/operations")
public class OperationsController extends BaseController {
    private final OperationsService operationsService;
    private final OperationQueueService operationsQueueService;

    public OperationsController(OperationsService operationsService,
                            OperationQueueService operationQueueService) {
        this.operationsService = operationsService;
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

        Operation build = this.operationsService.getOperationById(id);

        if (build == null) {
            return ErrorResponse.builder()
                    .addError("Not found Operation")
                    .notFound();
        }

        FieldMask fieldMask = parseFieldMask(fields);
        return Response.okResponse(build, fieldMask);
    }


}
