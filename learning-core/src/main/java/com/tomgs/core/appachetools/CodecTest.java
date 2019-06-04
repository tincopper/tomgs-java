package com.tomgs.core.appachetools;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

/**
 * @author tangzhongyuan
 * @since 2019-06-04 17:38
 **/
public class CodecTest {

    @Test
    public void testBase64() {
        String source = "username:pwd";
        final String base64String = Base64.encodeBase64String(source.getBytes(StandardCharsets.UTF_8));
        final byte[] decodeBase64 = Base64.decodeBase64(base64String);
        String result = StringUtils.newStringUtf8(decodeBase64);
        System.out.println(result);
    }
}
