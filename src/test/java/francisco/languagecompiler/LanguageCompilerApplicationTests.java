package francisco.languagecompiler;

import francisco.languagecompiler.resource.LanguageCompilerApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest(classes = LanguageCompilerApplication.class)
@AutoConfigureMockMvc
public class LanguageCompilerApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void contextLoads() {
	}

	@Test
	void testYourApiEndpoint() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/builds")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
		// Add more assertions based on your API response
	}

	// Add more test methods for other API endpoints as needed
}
