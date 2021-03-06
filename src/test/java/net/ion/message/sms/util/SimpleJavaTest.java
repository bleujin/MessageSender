package net.ion.message.sms.util;

import org.apache.commons.lang.CharUtils;
import org.testng.annotations.Test;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.testng.Assert.assertFalse;
import static org.testng.AssertJUnit.assertTrue;

public class SimpleJavaTest {

    @Test
    public void simpleTest() {
        String data = "010";
        StringBuilder dataBuffer = new StringBuilder(data);
        int length = 4;

        for(int inx = length - data.getBytes().length; inx > 0; inx--) {
            dataBuffer.append('\0');
        }

        System.out.println(dataBuffer.toString());
        System.out.println(dataBuffer.length());
        System.out.println(dataBuffer.toString().getBytes().length);
    }

    @Test
    public void simpleTest2() {
        String data = "";
        StringBuilder dataBuffer = new StringBuilder(data);
        int length = 1;

        for(int inx = length - data.getBytes().length; inx > 0; inx--) {
            dataBuffer.append('\0');
        }

        System.out.println(dataBuffer.toString());
        System.out.println(dataBuffer.length());
        System.out.println(dataBuffer.toString().getBytes().length);
    }

    @Test
    public void test() {
        System.out.println(String.valueOf(System.currentTimeMillis()).length());
        System.out.println(StandardCharsets.ISO_8859_1.name());
    }

    @Test
    public void charEncodeTest() throws UnsupportedEncodingException {
        String hello = "안녕하세요";

        String converted = new String(hello.getBytes("UTF-8"), "UNICODE");

        char[] buffer = hello.toCharArray();
        StringBuilder builder = new StringBuilder();

        for(char c: buffer) {
            String s = CharUtils.unicodeEscaped(c).replaceAll("\\\\u", "").toUpperCase();

            builder.append(s);
            System.out.println();
        }

        System.out.println(builder.toString());
    }

    @Test
    public void tokenRE_valid() {
        // token is APNS format
        String token = "a7303190e155b41450e7ce0d7262114b3fa4d2fb2081da2f9786356e973114e8";
        Pattern p = Pattern.compile("^[0-9a-f]{64}$");

        Matcher matcher = p.matcher(token);

        assertTrue(matcher.matches());
    }

    @Test
    public void tokenRE_invalid1() {
        // token is APNS format but length is 63 ( APNS token should be 64bytes string )
        String token = "a7303190e155b41450e7ce0d7262114b3fa4d2fb2081da2f9786356e973";
        Pattern p = Pattern.compile("^[0-9a-f]{64}$");

        Matcher matcher = p.matcher(token);

        assertFalse(matcher.matches());
    }


    @Test
    public void tokenRE_invalid2() {
        // token is GCM format
        String token = "APA91bFz7oI4kgcK4dt12fOPcovNv9hPrhC5Q_eFRnEu79maAsJTgjJ2Jl-n3c3kl7aoWuiCk7F0vT9VTl5GJFzVM1mG0Dxwzm0dpo0amhyp6rwKGe2a8MyFTkaf9CnMOUVYHddkeoWyk3QiOglvjOqbvhXs73Yx2XNelT_AOoHeyRCkYF9ZUY0";
        Pattern p = Pattern.compile("^[0-9a-f]{64}$");

        Matcher matcher = p.matcher(token);

        assertFalse(matcher.matches());
    }

}
