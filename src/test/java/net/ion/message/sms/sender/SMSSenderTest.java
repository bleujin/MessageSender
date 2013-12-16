package net.ion.message.sms.sender;

import net.ion.framework.util.Debug;
import net.ion.message.sms.message.PhoneMessage;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;

public class SMSSenderTest {

    @Test
    public void createSender() throws IOException, ExecutionException, InterruptedException {
        SMSSender sender = new SMSConfig().newDomestic().create() ;
        sender.newMessage("01091399660").message("안녕").from("02-3430-1751").send().get() ;
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void checkValidity() throws IOException {
        SMSSender sender = new SMSConfig().newDomestic().create() ;
        PhoneMessage noContentMsg = sender.newMessage("01091399660");
        noContentMsg.send();
    }

    @Test
    public void longMessge() throws IOException {
        SMSSender sender = new SMSConfig().newDomestic().create() ;
        PhoneMessage longContentMessage = sender.newMessage("01091399660").message("가나다라마바사아자차카타파하가나다라마바사아자차카타파하가나다라마바사아자차카타파하가나다라마바사아자차카타파하가나다라마바사아자차카타파하가나다라마바사아자차카타파하가나다라마바사아자차카타파하가나다라마바사아자차카타파하가나다라마바사아자차카타파하");

        try {
            longContentMessage.send();
        } catch(IllegalArgumentException e) {
            Debug.line(e.getMessage());
            assertThat(e.getMessage().indexOf("too large to send")).isGreaterThan(-1);
        }
    }

    @Test
    public void internationalSender_init() {
        SMSConfig config = new SMSConfig().newInternational();

        assertThat(config.getDeptCode()).isNotEmpty();
        assertThat(config.getUserCode()).isNotEmpty();
        assertThat(config.getHandlerURL()).isNotEmpty();
    }

    @Test
    public void internationalSender_message() {
        SMSSender sender = new SMSConfig().newInternational().create();
        PhoneMessage msg = sender.newMessage("01091399660").from("02-3430-1751").message("안녕하세요");

        assertThat(msg.getParam().getString("from_num")).isEqualTo("0234301751");
    }
}
