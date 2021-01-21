package web.sms;

import java.util.Map;

public class SmsWebhookRequest {

    private String smsSid;
    private String smsStatus;
    private String body;
    private String messageStatus;
    private String to;
    private String messageSid;
    private String accountSid;
    private String from;
    private String apiVersion;

    private SmsWebhookRequest() {
    }

    public String getSmsSid() {
        return smsSid;
    }

    public String getSmsStatus() {
        return smsStatus;
    }

    public String getBody() {
        return body;
    }

    public String getMessageStatus() {
        return messageStatus;
    }

    public String getTo() {
        return to;
    }

    public String getMessageSid() {
        return messageSid;
    }

    public String getAccountSid() {
        return accountSid;
    }

    public String getFrom() {
        return from;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public static SmsWebhookRequest buildFrom(Map<String, String> parameterMap) {
        SmsWebhookRequest request = new SmsWebhookRequest();
        request.smsSid = parameterMap.get("SmsSid");
        request.smsStatus = parameterMap.get("SmsStatus");
        request.body = parameterMap.get("Body");
        request.messageStatus = parameterMap.get("MessageStatus");
        request.to = parameterMap.get("To");
        request.messageSid = parameterMap.get("MessageSid");
        request.accountSid = parameterMap.get("AccountSid");
        request.from = parameterMap.get("From");
        request.apiVersion = parameterMap.get("ApiVersion");
        return request;
    }

}
