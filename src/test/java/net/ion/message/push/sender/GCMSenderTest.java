package net.ion.message.push.sender;

import net.ion.framework.util.Debug;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static org.testng.Assert.assertNotNull;
import static org.testng.AssertJUnit.assertEquals;

public class GCMSenderTest {

    String sendTo = "APA91bFz7oI4kgcK4dt12fOPcovNv9hPrhC5Q_eFRnEu79maAsJTgjJ2Jl-n3c3kl7aoWuiCk7F0vT9VTl5GJFzVM1mG0Dxwzm0dpo0amhyp6rwKGe2a8MyFTkaf9CnMOUVYHddkeoWyk3QiOglvjOqbvhXs73Yx2XNelT_AOoHeyRCkYF9ZUY0";
    String apiKey = "AIzaSyCB3YWgx-2ECRJ0sHIlcMvrb6gOfRIQo88";

    GCMSender sender = GCMSender.create(apiKey);

    @Test
    public void testFirst() throws IOException {
        PushResponse push = sender.newMessage(sendTo)
                .message("안녕")
                .delayWhenIdle(false)
                .timeToLive(60 * 30)
                .collapseKey("msg")
                .push();

        assertEquals(push.isSuccess(), true);
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void error_whenNoMsg() throws Exception {
        sender.newMessage(sendTo).push();
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void error_tooLargeMessage() throws Exception {
        String largeMessage = createLargeMessage();
        Debug.line("Payload Size = ", largeMessage.getBytes().length);

        sender.newMessage(sendTo).message(largeMessage).push();
    }

    private String createLargeMessage() throws UnsupportedEncodingException {
        // return 12270 bytes message
        String base = "가";
        StringBuilder builder = new StringBuilder();
        //1366
        for(int i = 0; i < 4090; i++) {
            builder.append(base);
        }

        return builder.toString();
    }

    @Test
    public void response() throws Exception {
        PushResponse response = sender.newMessage(sendTo).message("Hello World").push();

        Debug.line(response);
        assertEquals(true, response.isSuccess());
        assertNotNull(response.getResponseMessage());
    }

    @Test
    public void failResponse() throws Exception {
        String invalidToken = "abcd";

        PushResponse response = sender.newMessage(invalidToken).message("This message cannot be sent").push();

        Debug.line(response.getResponseMessage());

        assertEquals(false, response.isSuccess());
        assertNotNull(response.getResponseMessage());
    }

}
