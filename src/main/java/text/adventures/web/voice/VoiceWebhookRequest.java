package text.adventures.web.voice;

import java.util.Map;

public class VoiceWebhookRequest {

    private String from;
    private String digits;

    public String getFrom() {
        return from;
    }

    public String getDigits() {
        return digits;
    }

    @Override
    public String toString() {
        return "VoiceWebhookRequest{" +
                "from='" + from + '\'' +
                ", digits='" + digits + '\'' +
                '}';
    }

    public static VoiceWebhookRequest buildFrom(Map<String, String> parameterMap) {

        VoiceWebhookRequest result = new VoiceWebhookRequest();
        result.from = parameterMap.get("From");
        result.digits = parameterMap.get("Digits");
        return result;
    }

}
