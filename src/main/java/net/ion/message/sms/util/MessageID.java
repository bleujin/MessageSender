package net.ion.message.sms.util;

public class MessageID {

    public static String generate() {
        long number = (long) Math.floor(Math.random() * 900000000L) + 100000000L;
        return String.valueOf(number);
    }

}
