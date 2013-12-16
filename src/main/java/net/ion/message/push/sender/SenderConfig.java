package net.ion.message.push.sender;

import net.ion.message.push.sender.strategy.PushStrategy;
import org.infinispan.util.concurrent.WithinThreadExecutor;

import java.util.concurrent.ExecutorService;

public class SenderConfig {

    private String googleAPIKey;
    private String apnsKeyStore;
    private String apnsPassword;
    private boolean apnsIsProduction;

    private ExecutorService es;
    private int retryCount;

    private SenderConfig() {
    }

    public static SenderConfigBuilder newBuilder() {
        return new SenderConfigBuilder();
    }

    public static SenderConfig createTest() {
        return newBuilder().appleConfig("/Users/airkjh/Desktop/toontalk.p12", "toontalk", true).googleConfig("AIzaSyCB3YWgx-2ECRJ0sHIlcMvrb6gOfRIQo88").build();
    }

    public static SenderConfig createRetryTestConfig(int retryCount) {
        return newBuilder()
                .appleConfig("/Users/airkjh/Desktop/toontalk.p12", "toontalk", true)
                .googleConfig("AIzaSyCB3YWgx-2ECRJ0sHIlcMvrb6gOfRIQo88")
                .retryAttempts(retryCount)
                .build();
    }

    public Sender createSender(PushStrategy strategy) {
        return Sender.create(this, strategy);
    }

    public String getGoogleAPIKey() {
        return googleAPIKey;
    }

    public String getApnsKeyStore() {
        return apnsKeyStore;
    }

    public String getApnsPassword() {
        return apnsPassword;
    }

    public boolean isApnsIsProduction() {
        return apnsIsProduction;
    }

    public ExecutorService getExecutorService() {
        return es;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public static class SenderConfigBuilder {

        private String keystore;
        private String password;
        private boolean production;
        private String apiKey;
        private ExecutorService es;
        private int retryCount = 0;             // default config is that don't retry when failed

        public SenderConfigBuilder appleConfig(String keystore, String password, boolean isProduction) {
            this.keystore = keystore;
            this.password = password;
            this.production = isProduction;
            return this;
        }

        public SenderConfigBuilder googleConfig(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public SenderConfigBuilder executor(ExecutorService es) {
            this.es = es;
            return this;
        }

        public SenderConfigBuilder retryAttempts(int retryCount) {
            this.retryCount = retryCount;
            return this;
        }

        public SenderConfig build() {
            SenderConfig config = new SenderConfig();
            config.apnsKeyStore = keystore;
            config.apnsPassword = password;
            config.apnsIsProduction = production;
            config.googleAPIKey = apiKey;
            config.retryCount = retryCount;

            if (this.es == null) {
                config.es = new WithinThreadExecutor();
            }

            return config;
        }

    }
}
