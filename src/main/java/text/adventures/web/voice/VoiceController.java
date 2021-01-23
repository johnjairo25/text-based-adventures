package text.adventures.web.voice;

import com.twilio.twiml.VoiceResponse;
import com.twilio.twiml.voice.Gather;
import com.twilio.twiml.voice.Hangup;
import com.twilio.twiml.voice.Say;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import text.adventures.web.voice.VoiceGameManager.CommandResult;

import java.util.Map;

@RestController
@RequestMapping(path = "/voice-game")
public class VoiceController {

    private static final  int DEFAULT_SELECTION = 9;
    private static final String PLAY_ACTION = "/voice-text.adventures.game/play";

    private static final Logger LOGGER = LoggerFactory.getLogger(VoiceController.class);

    private final VoiceGameManager gameManager;

    @Autowired
    public VoiceController(VoiceGameManager gameManager) {
        this.gameManager = gameManager;
    }

    @PostMapping(path = "/start",
            produces = "application/xml",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String start(@RequestParam Map<String, String> paramMap) {

        VoiceWebhookRequest request = VoiceWebhookRequest.buildFrom(paramMap);
        LOGGER.debug("Endpoint: [{}], Request: [{}]", "start", request);

        String commandResponse = gameManager.startGame(getGameKey(request));
        LOGGER.trace("Endpoint: [{}], CommandResponse: [{}]", "start", commandResponse);

        return gatherWithMessage(commandResponse);
    }

    @PostMapping(path = "/play",
            produces = "application/xml",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String play(@RequestParam Map<String, String> paramMap) {

        VoiceWebhookRequest request = VoiceWebhookRequest.buildFrom(paramMap);
        LOGGER.debug("Endpoint: [{}], Request: [{}]", "play", request);

        int chosenOption = parseInt(request.getDigits());
        CommandResult commandResult = gameManager.applyCommand(getGameKey(request), chosenOption);
        LOGGER.trace("Endpoint: [{}], CommandResponse: [{}]", "play", commandResult);

        return commandResult.isEnded()
                ? endWithMessage(commandResult.getResult())
                : gatherWithMessage(commandResult.getResult());
    }

    private int parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            LOGGER.warn("Invalid value [{}] received in digits number, returning default value", value);
            return DEFAULT_SELECTION;
        }
    }


    private String getGameKey(VoiceWebhookRequest request) {
        return "VOICE" + request.getFrom();
    }

    private String gatherWithMessage(String prompt) {

        Gather gather = new Gather.Builder()
                .say(new Say.Builder(prompt).build())
                .action(VoiceController.PLAY_ACTION)
                .numDigits(1)
                .inputs(Gather.Input.DTMF)
                .build();
        VoiceResponse response = new VoiceResponse.Builder()
                .gather(gather)
                .build();
        return response.toXml();
    }

    private String endWithMessage(String prompt) {

        return new VoiceResponse.Builder()
                .say(new Say.Builder(prompt).build())
                .hangup(new Hangup.Builder().build())
                .build()
                .toXml();
    }


}
