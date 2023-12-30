package francisco.languagecompiler.resource;

import com.google.protobuf.FieldMask;

import java.util.List;

public class BaseController {

    protected FieldMask parseFieldMask(String fields) {
        if (fields == null || fields.isEmpty()) {
            return null;
        }

        List<String> fieldPaths = List.of(fields.split(","));
        return FieldMask.newBuilder().addAllPaths(fieldPaths).build();
    }
}
