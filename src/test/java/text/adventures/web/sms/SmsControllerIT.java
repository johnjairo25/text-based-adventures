package text.adventures.web.sms;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import text.adventures.web.application.WebApplication;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = WebApplication.class)
public class SmsControllerIT {

    private static final String PHONE_NUMBER = "+57111111111";
    private static final String COMMAND_TEXT = "start text.adventures.game";

    @Autowired
    private MockMvc mvc;
    @MockBean
    private SmsGameManager manager;

    @Test
    public void managerMessageShouldBeReturnedInResponse() throws Exception {

        when(manager.applyCommand(PHONE_NUMBER, COMMAND_TEXT))
                .thenReturn("Welcome");

        mvc.perform(post("/sms/play")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("From", PHONE_NUMBER)
                .param("Body", COMMAND_TEXT))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_XML))
                .andExpect(content().string(containsString("Response")))
                .andExpect(content().string(containsString("Message")))
                .andExpect(content().string(containsString("Body")))
                .andExpect(content().string(containsString("Welcome")));
    }


}
