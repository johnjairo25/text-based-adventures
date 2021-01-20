package web;

import com.twilio.twiml.MessagingResponse;
import com.twilio.twiml.messaging.Body;
import com.twilio.twiml.messaging.Message;
import game.ConsoleMain;
import game.TextAdventuresGame;
import game.builder.JsonGameBuilder;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;
import java.util.Map;

@RestController
@RequestMapping("/sms")
public class SmsController {

//    private static final String WELCOME_MESSAGE = "Welcome to the Text Based Adventures World";

    private static TextAdventuresGame game;
    private static boolean started = false;

    static {
        InputStream is = readBasicFile();
        game = new JsonGameBuilder(is).build();
    }

    @PostMapping(path = "/play",
            produces = "application/xml",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String play(@RequestParam Map<String, String> paramMap) {

        String commandText = paramMap.get("Body");
        if (!started || "start game".equalsIgnoreCase(commandText.trim())) {

            started = true;
            String gameResult = game.startGame();
            return buildResponseMessage(gameResult);
        } else {

            String gameResult = game.applyCommand(commandText);
            return buildResponseMessage(gameResult);
        }
    }

    private static InputStream readBasicFile() {
        return ConsoleMain.class
                .getClassLoader()
                .getResourceAsStream("basicGame.json");
    }

    private String buildResponseMessage(String message) {
        MessagingResponse response = new MessagingResponse.Builder()
                .message(new Message.Builder()
                        .body(new Body.Builder(message).build())
                        .build())
                .build();
        return response.toXml();
    }

}
