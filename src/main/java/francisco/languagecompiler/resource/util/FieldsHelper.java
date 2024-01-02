package francisco.languagecompiler.resource.util;

import com.google.protobuf.FieldMask;

public class FieldsHelper {
    public static boolean setField(String field, FieldMask fieldMask){
        return fieldMask == null || fieldMask.toString().contains(field);
    }

    public static boolean contains(String field, FieldMask fieldMask){
        return fieldMask.toString().contains(field);
    }
}
