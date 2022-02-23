package com.shumeit.sdk.api.utils;

import com.shumeit.sdk.api.exceptions.SDKException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * Author: shumei
 * Date: 2022.2.23
 * Time: 11:29
 * Description: AES加解密工具类
 */
public class AESUtil {
    public static final String ALG_AES = "AES";
    private static final String ECB_MODE = "AES/ECB/PKCS5Padding";//算法/模式/补码方式

    public static String encryptECB(String content, String secKey) {
        try {
            Cipher cipher = Cipher.getInstance(ECB_MODE);
            SecretKeySpec secSpec = genSecretKeySpec(secKey);
            cipher.init(Cipher.ENCRYPT_MODE, secSpec);
            byte[] encrypted = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
            return StringUtil.base64Encode(encrypted);
        } catch (Throwable e) {
            throw new SDKException(SDKException.COMMON_EX_CODE, "AES加密异常", e);
        }
    }

    public static String decryptECB(String content, String secKey) {
        try {
            Cipher cipher = Cipher.getInstance(ECB_MODE);
            SecretKeySpec secSpec = genSecretKeySpec(secKey);
            cipher.init(Cipher.DECRYPT_MODE, secSpec);
            byte[] encrypted1 = StringUtil.base64Decode(content);
            byte[] original = cipher.doFinal(encrypted1);
            return new String(original, StandardCharsets.UTF_8);
        } catch (Throwable e) {
            throw new SDKException(SDKException.COMMON_EX_CODE, "AES解密异常", e);
        }
    }

    /**
     * 生成密钥对象 密钥可支持16位
     *
     * @param secKey .
     * @return .
     */
    private static SecretKeySpec genSecretKeySpec(String secKey) {
        if (secKey == null || (secKey.length() != 16)) {
            throw new SDKException(SDKException.PARAM_CHECK_FAIL_CODE, "密钥长度须为16位");
        }
        return new SecretKeySpec(secKey.getBytes(StandardCharsets.UTF_8), ALG_AES);
    }
}
