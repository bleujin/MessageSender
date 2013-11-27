package net.ion.message.sms.util;

import org.apache.commons.lang.CharUtils;
import org.testng.annotations.Test;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;

public class BufferHandlerTest {

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

}
