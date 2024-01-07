package francisco.languagecompiler.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import francisco.languagecompiler.resource.model.Build;
import francisco.languagecompiler.resource.model.BuildLang;
import francisco.languagecompiler.resource.model.BuildOperation;
import francisco.languagecompiler.resource.model.Operation;
import francisco.languagecompiler.resource.requests.OperationRequest;
import francisco.languagecompiler.resource.service.BuildsService;
import francisco.languagecompiler.resource.service.OperationsService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static francisco.languagecompiler.resource.util.StringUtil.asJsonString;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = LanguageCompilerApplication.class)
@AutoConfigureMockMvc
class OperationsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OperationsService operationsService;

    @MockBean
    private BuildsService buildsService;


    @AfterEach
    void tearDown() {
        Mockito.reset(operationsService, buildsService);
    }

    @Nested
    @DisplayName("Operation List Tests")
    class OperationList {

        @Test
        @DisplayName("Get Builds - Standard")
        public void testGetBuilds() throws Exception {
            // Mocking the service response
            List<Operation> ops = Arrays.asList(
                    new BuildOperation( new Build("build1", "Code1", BuildLang.C)),
                    new BuildOperation( new Build("3", "Code 324", BuildLang.Java))
            );
            given(operationsService.getStream()).willReturn(ops.stream());

            // Performing the GET request
            mockMvc.perform(get("/api/v1/operations"))
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.[0].done").value(ops.get(0).isDone()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.[0].build.name").value("build1"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.[0].build.code").value("Code1"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.[0].build.language").value(BuildLang.C.getText())); // Update as needed

        }

    }


    @Nested
    @DisplayName("Operation Creation Tests")
    class OperationCreation {

        @Test
        @DisplayName("Create operation from build")
        public void testCreateOperation() throws Exception {
            Build b = new Build();
            OperationRequest operationRequest = new OperationRequest(b.getId());

            BuildOperation obj = new BuildOperation(b);

            when(operationsService.add(any(Operation.class))).thenReturn(obj);
            when(buildsService.get(any(String.class))).thenReturn(b);

            ObjectMapper objectMapper = new ObjectMapper();
            String content = objectMapper.writeValueAsString(operationRequest);

            mockMvc.perform((MockMvcRequestBuilders.post("/api/v1/operations")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(content)))
                    .andExpect(status().isCreated());
        }

        @Nested
        @DisplayName("Operation Update Tests")
        class BuildUpdate {

            @Test
            @DisplayName("Test successful build update")
            void testSuccessfulBuildUpdate() throws Exception {

                Build b = new Build();

                // Mock the behavior of the buildsService
                when(buildsService.get(anyString())).thenReturn(b);

                // Prepare request body with valid updates
                Map<String, Object> updates = new HashMap<>();
                updates.put("name", "Updated Name");
                updates.put("language", BuildLang.Java.getText());
                updates.put("code", "Updated Code");

                ObjectMapper objectMapper = new ObjectMapper();
                String content = objectMapper.writeValueAsString(updates);

                mockMvc.perform(patch("/api/v1/builds/" + b.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content))
                        .andExpect(status().isOk());
            }


            @Test
            public void testInvalidBuildUpdate() throws Exception {
                Build b = new Build();
                // Mock the behavior of the buildsService
                when(buildsService.get(anyString())).thenReturn(b);
                // Prepare request body with invalid updates (e.g., empty name)
                Map<String, Object> invalidUpdates = new HashMap<>();
                invalidUpdates.put("name", ""); // Empty name should trigger an error

                // Perform PATCH request and expect a bad request response
                mockMvc.perform(patch("/api/v1/builds/" + b.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(invalidUpdates)))
                        .andExpect(status().isBadRequest())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.[0]").value("Name is required for the build"));
            }
        }
    }

}