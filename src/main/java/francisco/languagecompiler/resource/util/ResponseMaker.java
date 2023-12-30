package francisco.languagecompiler.resource.util;

import com.google.protobuf.FieldMask;

import java.util.Map;

public interface ResponseMaker {
    Map<String, Object> toMap(FieldMask fieldMask);
    Map<String, Object> toMap();
}
