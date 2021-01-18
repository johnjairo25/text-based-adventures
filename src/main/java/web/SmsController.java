package web;

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
            produces = "text/xml")
    public String test() {
        Message message = new Message.Builder()
                .body(new Body.Builder(WELCOME_MESSAGE).build())
                .build();
        return message.toXml();
    }

}
