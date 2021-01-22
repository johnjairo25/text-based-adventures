package web.sms;

import java.util.Map;

public class SmsWebhookRequest {

    private String body;
    private String from;

    private SmsWebhookRequest() {
    }

    public String getBody() {
        return body;
    }

    public String getFrom() {
        return from;
    }

    public static SmsWebhookRequest buildFrom(Map<String, String> parameterMap) {
        SmsWebhookRequest request = new SmsWebhookRequest();
        request.body = parameterMap.get("Body");
        request.from = parameterMap.get("From");
        return request;
    }

}
