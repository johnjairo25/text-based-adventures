package text.adventures.web.voice;

import java.util.Map;

public class VoiceWebhookRequest {

    private static final String INBOUND = "inbound";

    private String direction;
    private String to;
    private String from;
    private String digits;

    public String getExternal() {
        return direction.equalsIgnoreCase(INBOUND) ? from : to;
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
        result.direction = parameterMap.get("Direction") == null ? INBOUND : parameterMap.get("Direction");
        result.to = parameterMap.get("To");
        result.from = parameterMap.get("From");
        result.digits = parameterMap.get("Digits");
        return result;
    }

}
