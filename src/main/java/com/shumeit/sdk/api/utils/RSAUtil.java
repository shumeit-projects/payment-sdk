package com.shumeit.sdk.api.utils;

import com.shumeit.sdk.api.exceptions.SDKException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * RSA工具类
 */
public class RSAUtil {
    private static final Logger logger = LoggerFactory.getLogger(RSAUtil.class);
    public static final String SIGNATURE_ALGORITHM_SHA1 = "SHA1withRSA";
    public static final String RSA = "RSA";
    public static final String DEFAULT_ENCRYPT_ALGORITHM = "RSA/ECB/PKCS1Padding";

    /**
     * 生成RSA签名串
     *
     * @param data       需要生成签名串的数据
     * @param privateKey 私钥
     * @return .
     * @throws SDKException .
     */
    public static String sign(String data, PrivateKey privateKey) throws SDKException {
        try {
            byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM_SHA1);
            signature.initSign(privateKey);
            signature.update(dataBytes);
            return StringUtil.base64Encode(signature.sign());
        } catch (Throwable e) {
            logger.error("==>sign err:", e);
            throw new SDKException(SDKException.COMMON_EX_CODE, "生成RSA签名失败", e);
        }
    }

    /**
     * 验证RSA签名串
     *
     * @param data      需要验签的数据
     * @param publicKey 公钥
     * @param sign      用户传过来的签名串
     * @return .
     * @throws SDKException .
     */
    public static boolean verify(String data, PublicKey publicKey, String sign) throws SDKException {
        try {
            byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
            byte[] signBytes = StringUtil.base64Decode(sign);

            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM_SHA1);
            signature.initVerify(publicKey);
            signature.update(dataBytes);
            return signature.verify(signBytes);
        } catch (Throwable e) {
            logger.error("==>verify err:", e);
            throw new SDKException(SDKException.COMMON_EX_CODE, "RSA验签失败", e);
        }
    }

    /**
     * 对称密钥公钥加密
     *
     * @param content   需要加密的明文内书
     * @param publicKey 公钥
     * @return 加密密文
     */
    public static String encryptByPublicKey(String content, PublicKey publicKey) {
        try {
            byte[] dataBytes = content.getBytes(StandardCharsets.UTF_8);
            dataBytes = doCipher(dataBytes, publicKey, true);
            return StringUtil.base64Encode(dataBytes);
        } catch (Throwable e) {
            throw new SDKException(SDKException.COMMON_EX_CODE, "RSA加密失败", e);
        }
    }

    /**
     * 对称密钥密文解密
     *
     * @param content    需要解密的内容
     * @param privateKey 私钥
     * @return 对称密钥明文
     * @throws SDKException .
     */
    public static String decryptByPrivateKey(String content, PrivateKey privateKey) {
        try {
            byte[] dataBytes = StringUtil.base64Decode(content);
            dataBytes = doCipher(dataBytes, privateKey, false);
            return new String(dataBytes, StandardCharsets.UTF_8);
        } catch (Throwable e) {
            throw new SDKException(SDKException.COMMON_EX_CODE, "RSA解密失败", e);
        }
    }

    private static byte[] doCipher(byte[] dataBytes, Key key, boolean isEncrypt) throws Exception {
        Cipher cipher;
        int maxBlock;

        if (isEncrypt) {
            RSAPublicKey publicKey = (RSAPublicKey) key;
            maxBlock = publicKey.getModulus().bitLength() / 8 - 11;
            cipher = Cipher.getInstance(DEFAULT_ENCRYPT_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        } else {
            RSAPrivateKey privateK = ((RSAPrivateKey) key);
            maxBlock = privateK.getModulus().bitLength() / 8;
            cipher = Cipher.getInstance(RSA);
            cipher.init(Cipher.DECRYPT_MODE, privateK);
        }

        int offSet = 0, inputLen = dataBytes.length;
        byte[] cache;
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            // 对数据分段加密/解密
            while (offSet < inputLen) {
                if (inputLen - offSet > maxBlock) {
                    cache = cipher.doFinal(dataBytes, offSet, maxBlock);
                } else {
                    cache = cipher.doFinal(dataBytes, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                offSet += maxBlock;
            }
            return out.toByteArray();
        } finally {
            out.close();
        }
    }

    public static RSAPublicKey parsePublicKey(String publicKeyBase64) throws Exception {
        byte[] keyBytes = StringUtil.base64Decode(publicKeyBase64);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        return (RSAPublicKey) keyFactory.generatePublic(x509KeySpec);
    }

    public static RSAPrivateKey parsePrivateKey(String privateKeyBase64) throws Exception {
        byte[] keyBytes = StringUtil.base64Decode(privateKeyBase64);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        return (RSAPrivateKey) keyFactory.generatePrivate(pkcs8KeySpec);
    }
}