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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
                    .andExpect(MockMvcResultMatchers.jsonPath("$.[0].build.language").value(BuildLang.C.getText())) // Update as needed
                    .andExpect(MockMvcResultMatchers.jsonPath("$.[0].build.status").value("PENDING"));

        }

    }


    @Nested
    @DisplayName("Operation Creation Tests")
    class OperationCreation {

        @Test
        @DisplayName("Create build to create operation")
        public void testCreateOperation() throws Exception {
            Build b = new Build();
            OperationRequest operationRequest = new OperationRequest(b.getId());

            BuildOperation obj = new BuildOperation(b);

            when(operationsService.add(any(Operation.class))).thenReturn(obj);
            when(buildsService.getBuildById(any(String.class))).thenReturn(b);

            ObjectMapper objectMapper = new ObjectMapper();
            String content = objectMapper.writeValueAsString(operationRequest);

            mockMvc.perform((MockMvcRequestBuilders.post("/api/v1/operations")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(content)))
                    .andExpect(status().isCreated());
        }


        @Test
        @DisplayName("Create build with missing Build id")
        public void testCreateOperationMissingId() throws Exception {
        }


    }

}