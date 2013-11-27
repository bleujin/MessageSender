package net.ion.message.sms.message;

import org.testng.annotations.Test;

public class PhoneMessageTest {

    @Test
    public void unicodeMsg() {
        PhoneMessage message = new PhoneMessage("");
        message.message("안녕");

        System.out.println(message.getParam().get("to_message"));
    }


}
