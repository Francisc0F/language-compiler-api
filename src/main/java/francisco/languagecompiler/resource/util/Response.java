package francisco.languagecompiler.resource.util;

import com.google.protobuf.FieldMask;
import francisco.languagecompiler.resource.model.Build;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Response {
    static public ResponseEntity createdResponse(ResponseMaker responseBody) {
        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }
    static public ResponseEntity<Object> createdResponse(ResponseMaker responseBody, FieldMask fieldMask) {
        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody.toMap(fieldMask));
    }
    static public ResponseEntity<Object> okResponse(ResponseMaker responseBody, FieldMask fieldMask) {
        return ResponseEntity.status(HttpStatus.OK).body(responseBody.toMap(fieldMask));
    }

    static public <T extends ResponseMaker> ResponseEntity<List<Map<String, Object>>> okResponse(Stream<T> stream, FieldMask fieldMask) {
        List<Map<String, Object>> list = stream.map(build -> build.toMap(fieldMask))
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

}
