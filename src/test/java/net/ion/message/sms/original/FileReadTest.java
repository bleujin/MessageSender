package net.ion.message.sms.original;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class FileReadTest {

    public static void main(String[] args) {

        File file = new File("/Users/airkjh/Downloads/java/sms/TEST.java");
        FileInputStream in = null;
        String line;
        try {
            in = new FileInputStream(file);
            List<String> strings = IOUtils.readLines(in, "euc-kr");
            for(String str: strings) {
                System.out.println(str);
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            IOUtils.closeQuietly(in);
        }


    }

}
