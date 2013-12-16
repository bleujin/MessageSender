package net.ion.message.push.sender;

import net.ion.message.push.sender.handler.ResponseHandler;
import net.ion.message.push.sender.strategy.TestStrategies;
import org.testng.annotations.Test;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class SenderTest {

    private SenderConfig config = SenderConfig.createTest();
    private Sender senderForAPNS = config.createSender(TestStrategies.airkjhAPNSStrategy());
    private Sender senderForGCM = config.createSender(TestStrategies.airkjhGoogleStrategy());

    @Test
    public void testFirst_apns_sync() throws Exception {
        List<PushResponse> response = senderForAPNS.createMessage("airkjh", "bleujin").send("안녕");

        assertEquals(2, response.size());
        assertEquals(true, response.get(0).isSuccess());
        assertEquals(false, response.get(1).isSuccess());               // bleujin is invalid user
    }

    @Test
    public void apns_async() throws ExecutionException, InterruptedException {
        Future<List<PushResponse>> future = senderForAPNS.createMessage("airkjh").sendAsync("안녕");
        List<PushResponse> responses = future.get();

        assertEquals(1, responses.size());
        assertEquals(true, responses.get(0).isSuccess());
    }

    @Test
    public void gcm_async() throws ExecutionException, InterruptedException {
        Future<List<PushResponse>> future = senderForGCM.createMessage("airkjh").sendAsync("안녕");
        List<PushResponse> responses = future.get();

        assertEquals(1, responses.size());
        assertEquals(true, responses.get(0).isSuccess());
    }

    @Test
    public void retryWhenFailed() throws ExecutionException, InterruptedException {
        String invalidUserId = "airkjh2";
        int retryCount = 5;

        Sender sender = SenderConfig.createRetryTestConfig(retryCount).createSender(TestStrategies.airkjhAPNSStrategy());

        Future<Integer> attempts = sender.createMessage(invalidUserId).sendAsync("모두 실패!!", new ResponseHandler<Integer>() {
            int failCount = 0;

            @Override
            public Integer result() {
                return failCount;
            }

            @Override
            public void onSuccess(PushResponse response) {
            }

            @Override
            public void onFail(PushResponse response) {
                failCount++;
            }

            @Override
            public void onThrow(String receiver, String token, Throwable t) {

            }
        });

        // invalid token is considered as failed request, not exception
        assertEquals(retryCount, attempts.get().intValue());
    }

    @Test
    public void retryWhenException() throws ExecutionException, InterruptedException {
        int retryCount = 3;

        SenderConfig config = SenderConfig.newBuilder().googleConfig("invalid_api_key").retryAttempts(retryCount).build();
        Sender sender = config.createSender(TestStrategies.airkjhGoogleStrategy());

        Future<Integer> exceptionCount = sender.createMessage("airkjh").sendAsync("실패!!", new ResponseHandler<Integer>() {

            int exceptionCount = 0;

            @Override
            public Integer result() {
                return exceptionCount;
            }

            @Override
            public void onSuccess(PushResponse response) {
            }

            @Override
            public void onFail(PushResponse response) {
            }

            @Override
            public void onThrow(String receiver, String token, Throwable t) {
                exceptionCount++;
            }
        });


        // invalid api key occures exception
        assertEquals(retryCount, exceptionCount.get().intValue());
    }


}
