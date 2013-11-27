package net.ion.message.sms.message;

import net.ion.message.sms.sender.Sender;
import net.ion.radon.aclient.ListenableFuture;
import net.ion.radon.aclient.Request;
import net.ion.radon.aclient.RequestBuilder;
import net.sf.json.JSONObject;
import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.StringUtils;
import org.restlet.data.Method;

import java.io.IOException;
import java.util.Iterator;

public class PhoneMessage {

    private JSONObject param = new JSONObject();
    private Sender sender;

    public PhoneMessage(String serialNo) {
        setAttribute("member", serialNo);
        setAttribute("group_name", "");
        setAttribute("to_message", "");
        setAttribute("encoding", "UNICODE");
    }

    public void setSender(Sender sender) {
        this.sender = sender;
    }

    public void setAttribute(String key, String value) {
        param.put(key, value);
    }

    public JSONObject getParam() {
        return param;
    }

    public PhoneMessage from(String fromPhone) {
        String[] phoneNums = StringUtils.split(fromPhone, "-");

        from(phoneNums[0], phoneNums[1], phoneNums[2]);

        return this;
    }

    public PhoneMessage from(String phoneItem1, String phoneItem2, String phoneItem3) {
        setAttribute("from_num1", phoneItem1);
        setAttribute("from_num2", phoneItem2);
        setAttribute("from_num3", phoneItem3);
        return this;
    }

    public String toQueryString() {
        StringBuilder queryString = new StringBuilder("?");

        Iterator keys = param.keys();

        while (keys.hasNext()) {
            String key = (String) keys.next();
            String value = param.getString(key);

            queryString.append(String.format("%s=%s&", key, value));
        }

        return queryString.substring(0, queryString.length() - 1);
    }

    public Request toRequest(String targetURL, Method method) {

        RequestBuilder builder = new RequestBuilder().setUrl(targetURL).setMethod(method);

        Iterator keys = param.keys();

        while (keys.hasNext()) {
            String key = (String) keys.next();
            String value = param.getString(key);

            builder.addParameter(key, value);
        }

        return builder.build();
    }

    public PhoneMessage message(String message) {
        setAttribute("to_message", toUnicode(message));
        return this;
    }

    private String toUnicode(String message) {
        char[] chars = message.toCharArray();
        StringBuilder builder = new StringBuilder();

        for (char c : chars) {
            String unicode = CharUtils.unicodeEscaped(c).replaceAll("\\\\u", "").toUpperCase();
            builder.append(unicode);
        }

        return builder.toString();
    }

    public ListenableFuture send() throws IOException {
        return sender.send(this);
    }
}
