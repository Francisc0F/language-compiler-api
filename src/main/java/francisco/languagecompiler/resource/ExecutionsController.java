package francisco.languagecompiler.resource;

import com.google.protobuf.FieldMask;
import francisco.languagecompiler.resource.model.Execution;
import francisco.languagecompiler.resource.model.Job;
import francisco.languagecompiler.resource.service.ExecutionsService;
import francisco.languagecompiler.resource.util.ErrorResponse;
import francisco.languagecompiler.resource.util.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/v1/executions")
public class ExecutionsController extends BaseController {
    private final ExecutionsService executionsService;


    public ExecutionsController(ExecutionsService executionsService) {
        this.executionsService = executionsService;
    }

    @GetMapping
    public ResponseEntity getExecutions(
            @RequestParam(name = "fields", required = false) String fields,
            @RequestParam(name = "done", required = false) String done) {

        Stream<Execution> stream = this.executionsService.getStream();

        /*if (done != null) {
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

        Execution build = this.executionsService.get(id);

        if (build == null) {
            return ErrorResponse.builder()
                    .addError("Not found Operation")
                    .notFound();
        }

        FieldMask fieldMask = parseFieldMask(fields);
        return Response.okResponse(build, fieldMask);
    }
}
