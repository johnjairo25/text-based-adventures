package web.sms;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import web.application.WebApplication;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = WebApplication.class)
public class SmsControllerIT {

    private static final String PHONE_NUMBER = "+573168904747";
    private static final String COMMAND_TEXT = "start game";

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
                .andExpect(MockMvcResultMatchers.content().string(containsStringIgnoringCase("Welcome")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("Response")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("Message")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("Body")));
    }


}
