package com.cloud.cloud.opentelemetry.exporter.util;

import com.google.protobuf.ByteString;
import org.springframework.util.StringUtils;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

/**
 * @author AGoodMan
 */
public class GoodStringUtil {

    public static String bytesToString(ByteString src) {
        return bytesToString(src.toByteArray(), "GB2312");
    }

    public static String bytesToString(ByteString src, String charSet) {
        if(!StringUtils.hasLength(charSet)) {
            charSet = "zh_CN.UTF-8";
        }
        return bytesToString(src.toByteArray(), charSet);
    }

    public static String bytesToString(byte[] input, String charSet) {
        if(input.length==0) {
            return "";
        }

        ByteBuffer buffer = ByteBuffer.allocate(input.length);
        buffer.put(input);
        buffer.flip();

        Charset charset = null;
        CharsetDecoder decoder = null;
        CharBuffer charBuffer = null;

        try {
            charset = Charset.forName(charSet);
            decoder = charset.newDecoder();
            charBuffer = decoder.decode(buffer.asReadOnlyBuffer());

            return charBuffer.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }
}
