package text.adventures.web.voice;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import text.adventures.web.application.WebApplication;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = WebApplication.class)
public class VoiceControllerIT {

    private static final String PHONE_NUMBER = "+571111111";
    private static final String GAME_KEY = "VOICE" + PHONE_NUMBER;
    private static final int OPTION = 1;

    @Autowired
    private MockMvc mvc;
    @MockBean
    private VoiceGameManager gameManager;

    @Test
    public void startGameCorrectly() throws Exception {
        when(gameManager.startGame(GAME_KEY)).thenReturn("Welcome");

        mvc.perform(post("/voice-game/start")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("From", PHONE_NUMBER)
                .param("Digits", ""))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_XML))
                .andExpect(content().string(containsString("Welcome")))
                .andExpect(content().string(containsString("Response")))
                .andExpect(content().string(containsString("Gather")))
                .andExpect(content().string(containsString("Say")));
    }

    @Test
    public void playWithAvailableNextMove() throws Exception {
        when(gameManager.applyCommand(GAME_KEY, OPTION))
                .thenReturn(new VoiceGameManager.CommandResult("result", false));

        mvc.perform(post("/voice-game/play")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("From", PHONE_NUMBER)
                .param("Digits", String.valueOf(OPTION)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_XML))
                .andExpect(content().string(containsString("result")))
                .andExpect(content().string(containsString("Response")))
                .andExpect(content().string(containsString("Gather")))
                .andExpect(content().string(containsString("Say")));
    }

    @Test
    public void playEndMove() throws Exception {
        when(gameManager.applyCommand(GAME_KEY, OPTION))
                .thenReturn(new VoiceGameManager.CommandResult("result", true));

        mvc.perform(post("/voice-game/play")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("From", PHONE_NUMBER)
                .param("Digits", String.valueOf(OPTION)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_XML))
                .andExpect(content().string(containsString("result")))
                .andExpect(content().string(containsString("Response")))
                .andExpect(content().string(containsString("Say")))
                .andExpect(content().string(containsString("Hangup")));
    }

}
