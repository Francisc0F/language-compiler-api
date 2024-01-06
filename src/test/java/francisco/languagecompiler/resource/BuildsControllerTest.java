package francisco.languagecompiler.resource;

import francisco.languagecompiler.resource.model.Build;
import francisco.languagecompiler.resource.model.BuildLang;
import francisco.languagecompiler.resource.service.BuildsService;
import francisco.languagecompiler.resource.service.OperationQueueService;
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

import static francisco.languagecompiler.resource.util.StringUtil.asJsonString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(classes = LanguageCompilerApplication.class)
@AutoConfigureMockMvc
class BuildsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BuildsService buildsService;

    @MockBean
    private OperationQueueService operationQueueService;

    @AfterEach
    void tearDown() {
        Mockito.reset(buildsService, operationQueueService);
    }

    @Nested
    @DisplayName("Build Retrieval Tests")
    class BuildRetrievalTests {

        @Test
        @DisplayName("Get Builds - test MediaType.APPLICATION_JSON")
        public void testJson() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/builds")).andExpect(status().isOk()).andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
        }

        @Test
        @DisplayName("Get Builds - Standard")
        public void testGetBuilds() throws Exception {
            // Mocking the service response
            List<Build> builds = Arrays.asList(new Build("build1", "Code1", BuildLang.C), new Build("build2", "Code223232323", BuildLang.C));
            given(buildsService.getStream()).willReturn(builds.stream());

            // Performing the GET request
            mockMvc.perform(get("/api/v1/builds")).andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.[0].name").value("build1")).andExpect(MockMvcResultMatchers.jsonPath("$.[0].code").value("Code1")).andExpect(MockMvcResultMatchers.jsonPath("$.[0].language").value("C")).andExpect(MockMvcResultMatchers.jsonPath("$.[1].name").value("build2")).andExpect(MockMvcResultMatchers.jsonPath("$.[1].code").value("Code223232323")).andExpect(MockMvcResultMatchers.jsonPath("$.[1].language").value("C"));
        }


        @Test
        @DisplayName("Get Builds with Fields Query String")
        public void testGetBuildsWithFieldsQueryString() throws Exception {
            // Mocking the service response
            List<Build> builds = Arrays.asList(new Build("build1", "Code1", BuildLang.C), new Build("build2", "Code223232323", BuildLang.C));
            given(buildsService.getStream()).willReturn(builds.stream());

            // Performing the GET request with query string
            mockMvc.perform(get("/api/v1/builds?fields=name,code")).andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.[0].name").value("build1")).andExpect(MockMvcResultMatchers.jsonPath("$.[0].code").value("Code1")).andExpect(MockMvcResultMatchers.jsonPath("$.[0].language").doesNotExist()).andExpect(MockMvcResultMatchers.jsonPath("$.[1].name").value("build2")).andExpect(MockMvcResultMatchers.jsonPath("$.[1].code").value("Code223232323")).andExpect(MockMvcResultMatchers.jsonPath("$.[1].language").doesNotExist());
        }
    }

    @Nested
    @DisplayName("Build Creation Tests")
    class BuildCreation {

        @Test
        @DisplayName("Build Create")
        public void testCreateBuild() throws Exception {
            // Mocking the new build data
            Build newBuild = new Build("newBuild", "NewCode", BuildLang.Java);

            // Mocking the service response after creating the build
            given(buildsService.addbuild(any(Build.class))).willReturn(newBuild);

            // Performing the POST request
            ObjectMapper objectMapper = new ObjectMapper();

            String content = objectMapper.writeValueAsString(newBuild);

            mockMvc.perform(post("/api/v1/builds").contentType(MediaType.APPLICATION_JSON).content(content)).andExpect(status().isCreated()).andExpect(MockMvcResultMatchers.jsonPath("$.name").value("newBuild")).andExpect(MockMvcResultMatchers.jsonPath("$.code").value("NewCode")).andExpect(MockMvcResultMatchers.jsonPath("$.language").value("Java"));
        }


        @Test
        @DisplayName("Create build with missing Lang")
        public void testCreateBuildMissingLang() throws Exception {
            // Mocking the new build data
            Build newBuild = new Build("newBuild", "NewCode", BuildLang.Java);
            newBuild.setLanguage(null);

            // Mocking the service response after creating the build
            given(buildsService.addbuild(any(Build.class))).willReturn(newBuild);

            ObjectMapper objectMapper = new ObjectMapper();
            String content = objectMapper.writeValueAsString(newBuild);

            mockMvc.perform(post("/api/v1/builds").contentType(MediaType.APPLICATION_JSON).content(content)).andExpect(status().isBadRequest()).andExpect(MockMvcResultMatchers.jsonPath("$.[0]").value("Lang is required for the build"));
        }


        @Test
        @DisplayName("Create build with missing Code")
        public void testCreateBuildMissingCode() throws Exception {
            // Mocking the new build data
            Build newBuild = new Build("newBuild", "NewCode", BuildLang.Java);
            newBuild.setCode(null);

            // Mocking the service response after creating the build
            given(buildsService.addbuild(any(Build.class))).willReturn(newBuild);

            ObjectMapper objectMapper = new ObjectMapper();
            String content = objectMapper.writeValueAsString(newBuild);

            mockMvc.perform(post("/api/v1/builds")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(content))
                    .andExpect(status().isBadRequest()).
                    andExpect(MockMvcResultMatchers.jsonPath("$.[0]").value("Code is required for the build"));
        }

        @Test
        @DisplayName("Create build with missing Name")
        public void testCreateBuildMissingName() throws Exception {
            // Mocking the new build data
            Build newBuild = new Build("newBuild", "NewCode", BuildLang.Java);
            newBuild.setName(null);

            // Mocking the service response after creating the build
            given(buildsService.addbuild(any(Build.class))).willReturn(newBuild);

            ObjectMapper objectMapper = new ObjectMapper();
            String content = objectMapper.writeValueAsString(newBuild);

            mockMvc.perform(post("/api/v1/builds").contentType(MediaType.APPLICATION_JSON).content(content)).andExpect(status().isBadRequest()).andExpect(MockMvcResultMatchers.jsonPath("$.[0]").value("Name is required for the build"));
        }

    }

    @Nested
    @DisplayName("Build Update Tests")
    class BuildUpdate {

        @Test
        @DisplayName("Test successful build update")
        void testSuccessfulBuildUpdate() throws Exception {

            Build b = new Build();

            // Mock the behavior of the buildsService
            when(buildsService.getBuildById(anyString())).thenReturn(b);

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
            when(buildsService.getBuildById(anyString())).thenReturn(b);
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
