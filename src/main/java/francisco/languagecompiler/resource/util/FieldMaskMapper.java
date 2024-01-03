package francisco.languagecompiler.resource.util;

import com.google.protobuf.FieldMask;
import com.google.protobuf.ProtocolStringList;
import francisco.languagecompiler.resource.model.BaseResource;

import java.lang.reflect.Field;
import java.util.*;

public class FieldMaskMapper {
    public static final String NESTED_MARKER = "_*";

    public static <T> Map<String, Object> createHashMapWithFields(T obj, String... fieldMask) {
        Map<String, Object> fieldValues = new HashMap<>();
        scanFields(obj, fieldValues);

        Map<String, Object> filteredFieldValues = new HashMap<>();
        // Convert String[] to ArrayList<String>
        ArrayList<String> fields = new ArrayList<>(Arrays.asList(fieldMask));
        boolean returnAll = fields.contains("*");
        if (returnAll) {
            setAllFieldsAvailable(fieldValues, filteredFieldValues);
            return filteredFieldValues;
        }

        Map<String, ArrayList<String>> nestedAttributesList = new HashMap<>();


        for (String field : fieldMask) {
            boolean isNested = field.contains(".");
            if (fieldValues.containsKey(field) && !isNested) {
                filteredFieldValues.put(field, fieldValues.get(field));
            }
            if (isNested) {
                mapAllNestedAtributes(field, nestedAttributesList);
            }
        }


        for (String key : nestedAttributesList.keySet()) {
            ArrayList<String> values = nestedAttributesList.get(key);


            Object ob = fieldValues.getOrDefault(key, null);
            if (ob != null) {
                String[] nestedItem = key.split("_");
                String nestedKey = nestedItem[0];
                String[] selectedFields = values.toArray(new String[0]);
                setNestedObject(fieldValues, filteredFieldValues, key, nestedKey, selectedFields);
            }
        }

        return filteredFieldValues;
    }

    private static void setAllFieldsAvailable(Map<String, Object> fieldValues, Map<String, Object> filteredFieldValues) {
        setAllFields(fieldValues, filteredFieldValues);

        for (String key : fieldValues.keySet()) {
            if (!key.endsWith("_*")) {
                continue;
            }

            String[] nestedItem = key.split("_");
            String nestedKey = nestedItem[0];
            String nestedMask = nestedItem[1];
            setNestedObject(fieldValues, filteredFieldValues, key, nestedKey, nestedMask);
        }
    }

    private static void setNestedObject(Map<String, Object> fieldValues,
                                        Map<String, Object> currentHasMap,
                                        String auxKey,
                                        String finalKey,
                                        String... nestedMask) {
        Object nestedObj = fieldValues.get(auxKey);
        currentHasMap.put(finalKey, createHashMapWithFields(nestedObj, nestedMask));
        currentHasMap.remove(auxKey);
    }


    private static void setAllFields(Map<String, Object> fieldValues, Map<String, Object> filteredFieldValues) {
        for (String key : fieldValues.keySet()) {
            filteredFieldValues.put(key, fieldValues.get(key));
        }
    }

    private static <T> void scanFields(T obj, Map<String, Object> fieldValues) {
        Class<?> currentClass = obj.getClass();

        while (currentClass != null) {
            Field[] declaredFields = currentClass.getDeclaredFields();

            for (Field field : declaredFields) {
                try {
                    field.setAccessible(true);
                    Object value = field.get(obj);

                    if (!field.getName().startsWith("$") &&
                            !field.getName().startsWith("this") ) {
                        if (value instanceof BaseResource) {
                            // is nested resource
                            fieldValues.put(field.getName() + NESTED_MARKER, value);
                        } else {
                            fieldValues.put(field.getName(), value);
                        }
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace(); // Handle the exception as per your requirement
                }
            }

            if (currentClass.equals(BaseResource.class)) {
                break;
            }
            currentClass = currentClass.getSuperclass();
        }
    }


    private static void mapAllNestedAtributes(String field, Map<String, ArrayList<String>> nestedHelper) {
        String[] parts = field.split("\\.");
        if (parts.length > 1) {
            String subPropertyKey = parts[1];
            String subProperty = parts[0];
            ArrayList list = nestedHelper.getOrDefault(subProperty + NESTED_MARKER, null);
            if (list == null) {
                ArrayList propList = new ArrayList<>();
                propList.add(subPropertyKey);
                nestedHelper.put(subProperty+ NESTED_MARKER, propList);
            } else {
                list.add(subPropertyKey);
            }
        }
    }


    public static String[] getFmStrings(FieldMask fm) {
        ProtocolStringList fieldPaths = fm.getPathsList();

        // Create a list to collect the paths

        List<String> pathsList = new ArrayList<>(fieldPaths);

        String[] paths = pathsList.toArray(new String[0]);
        return paths;
    }

}
