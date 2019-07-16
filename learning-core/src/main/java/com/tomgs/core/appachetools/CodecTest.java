package com.tomgs.core.appachetools;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author tangzhongyuan
 * @since 2019-06-04 17:38
 **/
public class CodecTest {

    @Test
    public void testBase64() throws InterruptedException {
        String source = "username:pwd";
        //final String base64String = Base64.encodeBase64String(source.getBytes(StandardCharsets.UTF_8));
        //final byte[] decodeBase64 = Base64.decodeBase64(base64String);
        //String result = StringUtils.newStringUtf8(decodeBase64);
       // System.out.println(result);
        long sleepTimeMs = 1;
        sleepTimeMs = sleepTimeMs / 10 * 10;
        System.out.println(sleepTimeMs);

        final long toNanos = TimeUnit.MILLISECONDS.toNanos(1);
        System.out.println(toNanos);
        for (;;) {
            Thread.sleep(9);
            System.out.println(System.currentTimeMillis());
        }
    }
}
