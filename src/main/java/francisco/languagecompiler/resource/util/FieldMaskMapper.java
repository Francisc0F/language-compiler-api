package francisco.languagecompiler.resource.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FieldMaskMapper {
    public static <T> Map<String, Object> createHashMapWithFields(T obj, String... fieldMask) {
        Map<String, Object> fieldValues = new HashMap<>();

        scanFields(obj, fieldValues);
        Map<String, ArrayList<String>> nestedAttributesList = new HashMap<>();

        Map<String, Object> filteredFieldValues = new HashMap<>();
        for (String field : fieldMask) {
            boolean isNested = field.contains(".");
            if (fieldValues.containsKey(field) && !isNested) {
                filteredFieldValues.put(field, fieldValues.get(field));
            }
            if(isNested){
                mapAllNestedAtributes(field, nestedAttributesList);
            }
        }


        for (String key : nestedAttributesList.keySet()) {
            ArrayList<String> values = nestedAttributesList.get(key);
            String[] stringArray = values.toArray(new String[0]);

            Object ob = fieldValues.getOrDefault(key, null);
            if(ob != null){
                filteredFieldValues.put(key, createHashMapWithFields(ob, stringArray));
            }
        }

        return filteredFieldValues;
    }

    private static <T> void scanFields(T obj, Map<String, Object> fieldValues) {
        Class<?> currentClass = obj.getClass();

        while (currentClass != null) {
            Field[] declaredFields = currentClass.getDeclaredFields();

            for (Field field : declaredFields) {
                try {
                    field.setAccessible(true);
                    Object value = field.get(obj);
                    fieldValues.put(field.getName(), value);
                } catch (IllegalAccessException e) {
                    e.printStackTrace(); // Handle the exception as per your requirement
                }
            }

            currentClass = currentClass.getSuperclass();
        }
    }

    private static void mapAllNestedAtributes(String field, Map<String, ArrayList<String>> nestedHelper) {
            String[] parts = field.split("\\.");
            if (parts.length > 1) {
                String subPropertyKey = parts[1];
                String subProperty = parts[0];
                ArrayList list = nestedHelper.getOrDefault(subProperty, null);
                if (list == null) {
                    ArrayList propList = new ArrayList<>();
                    propList.add(subPropertyKey);
                    nestedHelper.put(subProperty, propList);
                }else{
                    list.add(subPropertyKey);
                }
            }
    }

}
