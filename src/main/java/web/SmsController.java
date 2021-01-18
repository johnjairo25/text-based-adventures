package web;

import com.twilio.twiml.messaging.Body;
import com.twilio.twiml.messaging.Message;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sms")
public class SmsController {

    /*
    INITIAL TASKS:
    1. Deploy a Welcome to the Text Based Adventures World message (Web - Local) [DONE]
    2. Deploy to Heroku, verify it works
    3. Configure and Deploy the webhook with Twilio and check with SMS
    4. Add security to the endpoint (prd)
     */

    @GetMapping("/test")
    public String test() {
        return demo();
    }

    private static final String WELCOME_MESSAGE = "Welcome to the Text Based Adventures World";

    private static String demo() {
        Message message = new Message.Builder()
                .body(new Body.Builder(WELCOME_MESSAGE).build())
                .build();
        return message.toXml();
    }

}
