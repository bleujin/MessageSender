package net.ion.message.sms.sender;

import com.google.common.base.Preconditions;
import net.ion.framework.util.StringUtil;
import net.ion.message.sms.message.Validator;
import net.ion.radon.aclient.NewClient;

public class SenderConfig {

    private String deptCode;
    private String userCode;
    private String handlerURL;
    private String fromPhone = "000-000-0000";
    private Validator validator;

    private NewClient client;
    private String callbackURL = "http://127.0.0.1/callback";

    public SenderConfig() {
        this(NewClient.create());
    }

    public SenderConfig(NewClient client) {
        this.client = client;
    }

    public SenderConfig setDeptCode(String deptCode) {
        this.deptCode = deptCode;
        return this;
    }

    public SenderConfig setUserCode(String userCode) {
        this.userCode = userCode;
        return this;
    }

    public SenderConfig setHandlerURL(String handlerURL) {
        this.handlerURL = handlerURL;
        return this;
    }

    public SenderConfig setFromPhone(String fromPhone) {
        Preconditions.checkArgument(StringUtil.split(fromPhone, "-").length == 3, "Phone format should be 000-000-0000");

        this.fromPhone = fromPhone;
        return this;
    }

    public SenderConfig setCallback(String callbackURL) {
        this.callbackURL = callbackURL;
        return this;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public String getUserCode() {
        return userCode;
    }

    public String getHandlerURL() {
        return handlerURL;
    }

    public String getFromPhone() {
        return fromPhone;
    }

    public NewClient getClient() {
        return client;
    }

    public String getCallbackURL() {
        return callbackURL;
    }

    public Validator getValidator() {
        return validator;
    }

    public SenderConfig setValidator(Validator validator) {
        this.validator = validator;
        return this;
    }

    public SenderConfig newDomestic() {
        final String deptCode = "8J-N2W-G1";
        final String userCode = "ioncom2";
        final String handlerURL = "https://toll.surem.com:440/message/direct_call_sms_return_post.asp";
        final Validator domesticValidator = Validator.domesticValidator();

        return this.setDeptCode(deptCode).setUserCode(userCode).setHandlerURL(handlerURL).setValidator(domesticValidator);
    }


    public SenderConfig newInternational() {
        final String deptCode = "JM-BWB-P6";
        final String userCode = "ioncom";
        final String handlerURL = "https://toll.surem.com:440/message/direct_INTL_return_post.asp";
        final Validator internationalValidator = Validator.internationalValidator();

        return this.setDeptCode(deptCode).setUserCode(userCode).setHandlerURL(handlerURL).setValidator(internationalValidator);
    }


    public Sender create() {
        Preconditions.checkArgument(StringUtil.isNotEmpty(deptCode), "deptCode is null or blank");
        Preconditions.checkArgument(StringUtil.isNotEmpty(userCode), "userCode is null or blank");
        Preconditions.checkArgument(StringUtil.isNotEmpty(handlerURL), "handlerURL is null or blank");

        return new Sender(this);
    }

}
