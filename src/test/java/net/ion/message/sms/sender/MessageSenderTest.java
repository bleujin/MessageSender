package net.ion.message.sms.sender;

import net.ion.framework.util.Debug;
import net.ion.message.sms.message.PhoneMessage;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;

public class MessageSenderTest {

    @Test
    public void createSender() throws IOException, ExecutionException, InterruptedException {
        Sender sender = new SenderConfig().newDomestic().setFromPhone("02-3430-1751").create() ;
        sender.newMessage("01091399660").message("안녕").send().get() ;
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void checkValidity() throws IOException {
        Sender sender = new SenderConfig().newDomestic().create() ;
        PhoneMessage noContentMsg = sender.newMessage("01091399660");
        noContentMsg.send();
    }

    @Test
    public void longMessge() throws IOException {
        Sender sender = new SenderConfig().newDomestic().create() ;
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
        SenderConfig config = new SenderConfig().newInternational();

        assertThat(config.getDeptCode()).isNotEmpty();
        assertThat(config.getUserCode()).isNotEmpty();
        assertThat(config.getHandlerURL()).isNotEmpty();
    }

    @Test
    public void internationalSender_message() {
        Sender sender = new SenderConfig().newInternational().create();
        PhoneMessage msg = sender.newMessage("01091399660").message("안녕하세요");


        System.out.println(msg.getParam().getString("from_num"));
    }
}
