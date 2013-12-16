package net.ion.message.push.sender;

import javapns.Push;
import javapns.communication.exceptions.CommunicationException;
import javapns.communication.exceptions.KeystoreException;
import javapns.devices.Device;
import javapns.devices.exceptions.InvalidDeviceTokenFormatException;
import javapns.devices.implementations.basic.BasicDevice;
import javapns.notification.*;
import org.json.JSONException;
import org.testng.annotations.Test;

import java.util.List;

public class JAVAPNSTest {

    String keystore = "/Users/airkjh/Desktop/toontalk.p12";
    String password = "toontalk";
    String deviceToken = "a7303190e155b41450e7ce0d7262114b3fa4d2fb2081da2f9786356e973114e8";

    private PushedNotification sendPayload(Payload payload) throws KeystoreException, CommunicationException, InvalidDeviceTokenFormatException {

        PushNotificationManager pushManager = new PushNotificationManager();

        try {
            AppleNotificationServer server = new AppleNotificationServerBasicImpl(keystore, password, true);
            pushManager.initializeConnection(server);

            BasicDevice device = new BasicDevice(deviceToken);

            BasicDevice.validateTokenFormat(device.getToken());
            PushedNotification notification = pushManager.sendNotification(device, payload, true);

            return notification;

        } finally {
            pushManager.stopConnection();
        }
    }

    @Test
    public void sendTest() throws InvalidDeviceTokenFormatException, CommunicationException, KeystoreException {

        PushedNotification pushedNotification = sendPayload(PushNotificationPayload.test());
        printPushedNotification(pushedNotification);

    }

    @Test
    public void feedback() throws CommunicationException, KeystoreException {
        List<Device> deviceList = Push.feedback(keystore, password, true);

        for (Device device : deviceList) {
            System.out.println(device.getToken());
        }
    }

    @Test
    public void sendMessage_withSoundAndBadge() throws JSONException, CommunicationException, KeystoreException, InvalidDeviceTokenFormatException {

        PushNotificationPayload message = PushNotificationPayload.complex();
        message.addAlert("안녕");
        message.addSound("default");
        message.addBadge(100);

        PushedNotification pushed = sendPayload(message);
        printPushedNotification(pushed);
    }

    private void printPushedNotification(PushedNotification pushed) {
        System.out.println("  " + pushed.toString());
    }

}
