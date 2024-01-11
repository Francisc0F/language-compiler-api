package francisco.languagecompiler.resource.util;

import com.google.protobuf.FieldMask;
import francisco.languagecompiler.resource.model.Build;
import francisco.languagecompiler.resource.model.BuildOperation;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.Map;

import static francisco.languagecompiler.resource.util.FieldMaskMapper.getFmStrings;
import static org.junit.jupiter.api.Assertions.*;

class FieldMaskMapperTest {

    @Test
    void testMap() {
        Build b = new Build();
        BuildOperation obj = new BuildOperation(b);
        obj.setDone(true);
        // Specify the fields you want in the result
        String[] fieldMask = {"done"};

        // Use the FieldMaskMapper to create a HashMap with the specified fields and their values
        Map<String, Object> resultMap = FieldMaskMapper.createHashMapWithFields(obj, fieldMask);

        // Assert the expected values
        assertEquals(1, resultMap.size());
        assertEquals(true, resultMap.get("done"));
    }


    @Test
    void testNestedBuildProperties() {
        // Create an instance of your object
        Build b = new Build();
        b.setName("SampleBuild");
        b.setCode("12345");

        BuildOperation obj = new BuildOperation(b);
        obj.setDone(true);

        // Specify the fields you want, including nested properties
        String[] fieldMask = {"done", "build.name", "build.code", "id"};

        // Use the FieldMaskMapper to create a HashMap with the specified fields and their values
        Map<String, Object> resultMap = FieldMaskMapper.createHashMapWithFields(obj, fieldMask);

        // Assert the expected values
        assertEquals(3, resultMap.size());
        assertEquals(true, resultMap.get("done"));

        // Check nested properties
        assertTrue(resultMap.containsKey("build"));
        assertTrue(resultMap.get("build") instanceof Map);

        Map<String, Object> buildMap = (Map<String, Object>) resultMap.get("build");
        assertEquals("SampleBuild", buildMap.get("name"));
        assertEquals("12345", buildMap.get("code"));
    }

    @Test
    void testMetadataProperties() {
        // Create an instance of your object with metadata
        Build b = new Build();
        BuildOperation obj = new BuildOperation(b);
        obj.setDone(true);

        // Set metadata properties
        obj.getMetadata().setType("typeA");
        obj.getMetadata().setId("9876");
        obj.getMetadata().setStartTime(new Date());
        obj.getMetadata().setProgress(50);

        // Specify the fields you want, including metadata properties
        String[] fieldMask = {"done", "metadata.type", "metadata.id", "metadata.startTime", "metadata.progress"};

        // Use the FieldMaskMapper to create a HashMap with the specified fields and their values
        Map<String, Object> resultMap = FieldMaskMapper.createHashMapWithFields(obj, fieldMask);

        // Assert the expected values
        assertEquals(2, resultMap.size());
        assertEquals(true, resultMap.get("done"));
        assertEquals("typeA", ((Map<String, Object>) resultMap.get("metadata")).get("type"));
        assertEquals("9876", ((Map<String, Object>) resultMap.get("metadata")).get("id"));
        assertTrue(((Map<String, Object>) resultMap.get("metadata")).containsKey("startTime"));
        assertTrue(((Map<String, Object>) resultMap.get("metadata")).containsKey("progress"));
    }


    @Test
    void testWithGoogleFieldMask() {

        String[] fieldMask = {"done", "build.name", "build.code", "id"};

        // Build the FieldMask object using FieldMask.newBuilder()
        FieldMask.Builder fmBuilder = FieldMask.newBuilder();

        // Add the fields from the string array to the FieldMask builder
        for (String field : fieldMask) {
            fmBuilder.addPaths(field);
        }
        FieldMask fm = fmBuilder.build();

        String[] paths = getFmStrings(fm);
                                                Build.Builder b = new Build.Builder();
        FieldMaskMapper.createHashMapWithFields(b.C("test", "code"), paths);
    }

    @Test
    void testAllFields() {
        Build b = new Build();
        BuildOperation obj = new BuildOperation(b);
        obj.setDone(true);

        // Set metadata properties
        obj.getMetadata().setType("typeA");
        obj.getMetadata().setId("9876");
        obj.getMetadata().setStartTime(new Date());
        obj.getMetadata().setProgress(50);

        // Specify all fields
        String[] fieldMask = {"*"};

        // Use the FieldMaskMapper to create a HashMap with all fields and their values
        Map<String, Object> resultMap = FieldMaskMapper.createHashMapWithFields(obj, fieldMask);

        // Assert the expected values
        assertEquals(8, resultMap.size()); // Including base class fields
        assertEquals(true, resultMap.get("done"));
        assertEquals("typeA", ((Map<String, Object>) resultMap.get("metadata")).get("type"));
        assertEquals("9876", ((Map<String, Object>) resultMap.get("metadata")).get("id"));
        assertTrue(((Map<String, Object>) resultMap.get("metadata")).containsKey("startTime"));
        assertTrue(((Map<String, Object>) resultMap.get("metadata")).containsKey("progress"));
    }

}