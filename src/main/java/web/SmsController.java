package web;

import com.twilio.twiml.MessagingResponse;
import com.twilio.twiml.messaging.Body;
import com.twilio.twiml.messaging.Message;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sms")
public class SmsController {

    private static final String WELCOME_MESSAGE = "Welcome to the Text Based Adventures World";

    @RequestMapping(value = "/test",
            method = {RequestMethod.GET, RequestMethod.POST},
            produces = "application/xml")
    public String test() {
        return buildResponseMessage(WELCOME_MESSAGE);
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
