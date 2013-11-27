package net.ion.message.sms.util;

import com.google.common.collect.Lists;
import org.testng.annotations.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MessageIDTest {

    @Test
    public void generate() {
        String generated = MessageID.generate();
        System.out.println(generated);

        assertThat(generated.length()).isEqualTo(9);
    }

    @Test
    public void duplicateIDPercent() {

        List<String> generated = Lists.newArrayList();
        int total = 1000000;

        for(int i = 0; i < total; i++) {
            generated.add(MessageID.generate());
        }

        int actual = generated.size();

        System.out.printf("%.2f", (float)(actual/total));
    }
}
