package text.adventures.web.sms;

import com.twilio.twiml.MessagingResponse;
import com.twilio.twiml.messaging.Body;
import com.twilio.twiml.messaging.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/sms")
public class SmsController {

    private final SmsGameManager gameManager;

    @Autowired
    public SmsController(SmsGameManager gameManager) {
        this.gameManager = gameManager;
    }

    @PostMapping(path = "/play",
            produces = "application/xml",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String play(@RequestParam Map<String, String> paramMap) {
        SmsWebhookRequest request = SmsWebhookRequest.buildFrom(paramMap);
        String commandResult = gameManager.applyCommand(request.getFrom(), request.getBody());
        return buildResponseMessage(commandResult);
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
