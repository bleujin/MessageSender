package net.ion.message.sms.message;

import com.google.common.base.Preconditions;
import net.ion.message.sms.response.MessagingResponse;
import net.ion.message.sms.sender.SMSSender;
import net.ion.radon.aclient.ListenableFuture;
import net.ion.radon.aclient.Request;
import net.ion.radon.aclient.RequestBuilder;
import net.sf.json.JSONObject;
import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.StringUtils;
import org.restlet.data.Method;

import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.Future;

public class PhoneMessage {

    private JSONObject param = new JSONObject();
    private SMSSender sender;

    public PhoneMessage(String serialNo) {
        setAttribute("member", serialNo);
        setAttribute("group_name", "");
        setAttribute("to_message", "");
        setAttribute("encoding", "UNICODE");
    }

    public void setSender(SMSSender sender) {
        this.sender = sender;
    }

    public void setAttribute(String key, String value) {
        param.put(key, value);
    }

    public JSONObject getParam() {
        return param;
    }

    public PhoneMessage from(String fromNum) {

        if(sender.isDomesticMessage()) {
            String[] fromNums = StringUtils.split(fromNum, "-");
            Preconditions.checkArgument(fromNums.length == 3, "Phone number format is XXX-XXXX-XXXX");

            setAttribute("from_num1", fromNums[0]);
            setAttribute("from_num2", fromNums[1]);
            setAttribute("from_num3", fromNums[2]);

        } else {
            setAttribute("from_num", StringUtils.replace(fromNum, "-", ""));
        }

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

    public Future<MessagingResponse> send() throws IOException {
        return sender.send(this);
    }
}
