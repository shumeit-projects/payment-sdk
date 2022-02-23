package com.shumeit.sdk.api.utils;

import com.shumeit.sdk.api.exceptions.SDKException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;
import java.util.UUID;

public class StringUtil {
    public static boolean isEmpty(String value) {
        return value == null || value.trim().length() == 0;
    }

    public static String gen16LenRand() {
        if (new Random().nextInt(2) == 0) {
            return StringUtil.getMD5Hex(UUID.randomUUID().toString()).substring(16);
        } else {
            return StringUtil.getMD5Hex(UUID.randomUUID().toString()).substring(1, 17);
        }
    }

    public static String gen32LenRand() {
        return StringUtil.getMD5Hex(UUID.randomUUID().toString());
    }


    public static String base64Encode(byte[] value) {
        return Base64.getEncoder().encodeToString(value);
    }

    public static byte[] base64Decode(String value) {
        return Base64.getDecoder().decode(value);
    }

    /**
     * 生成16进制的MD5字符串
     *
     * @param str .
     * @return .
     */
    public static String getMD5Hex(String str) {
        return byte2Hex(getMD5(str));
    }

    private static byte[] getMD5(String str) {
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            if (!StringUtil.isEmpty(str)) {
                messageDigest.update(str.getBytes(StandardCharsets.UTF_8));
            }
        } catch (NoSuchAlgorithmException e) {
            throw new SDKException(SDKException.COMMON_EX_CODE, "生成MD5信息时异常", e);
        }
        return messageDigest.digest();
    }

    private static String byte2Hex(byte[] bytes) {
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        int j = bytes.length;
        char[] str = new char[j * 2];
        int k = 0;
        for (byte byte0 : bytes) {
            str[k++] = hexDigits[byte0 >>> 4 & 0xf];
            str[k++] = hexDigits[byte0 & 0xf];
        }
        return new String(str);
    }

}
