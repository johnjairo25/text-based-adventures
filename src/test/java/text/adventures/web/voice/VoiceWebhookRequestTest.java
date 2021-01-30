package text.adventures.web.voice;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class VoiceWebhookRequestTest {

    @Test
    public void canHandleTestRequest() {
        Map<String, String> input = new HashMap<>();
        input.put("From", "from");
        input.put("Digits", "1");

        VoiceWebhookRequest request = VoiceWebhookRequest.buildFrom(input);

        assertNotNull(request);
        assertEquals("from", request.getExternal());
        assertEquals("1", request.getDigits());
    }


    @Test
    public void canHandleInboundRequest() {
        Map<String, String> input = new HashMap<>();
        input.put("Direction", "inbound");
        input.put("From", "from");
        input.put("To", "to");
        input.put("Digits", "1");

        VoiceWebhookRequest request = VoiceWebhookRequest.buildFrom(input);

        assertNotNull(request);
        assertEquals("from", request.getExternal());
        assertEquals("1", request.getDigits());
    }

    @Test
    public void canHandleOutboundRequest() {
        Map<String, String> input = new HashMap<>();
        input.put("Direction", "outbound-api");
        input.put("To", "to");
        input.put("From", "from");
        input.put("Digits", "1");

        VoiceWebhookRequest request = VoiceWebhookRequest.buildFrom(input);

        assertNotNull(request);
        assertEquals("to", request.getExternal());
        assertEquals("1", request.getDigits());
    }

}
