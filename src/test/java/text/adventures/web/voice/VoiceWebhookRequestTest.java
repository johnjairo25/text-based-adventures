package text.adventures.web.voice;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class VoiceWebhookRequestTest {

    @Test
    public void canBuildObjectCorrectly() {
        Map<String, String> input = new HashMap<>();
        input.put("From", "from");
        input.put("Digits", "1");

        VoiceWebhookRequest request = VoiceWebhookRequest.buildFrom(input);

        assertNotNull(request);
        assertEquals("from", request.getFrom());
        assertEquals("1", request.getDigits());
    }

}
