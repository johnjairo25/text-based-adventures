package web.sms;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SmsWebhookRequestTest {

    @Test
    public void correctlyBuiltObject() {
        Map<String, String> input = new HashMap<>();
        input.put("Body", "body");
        input.put("From", "from");

        SmsWebhookRequest request = SmsWebhookRequest.buildFrom(input);

        assertNotNull(request);
        assertEquals("body", request.getBody());
        assertEquals("from", request.getFrom());
    }

}
