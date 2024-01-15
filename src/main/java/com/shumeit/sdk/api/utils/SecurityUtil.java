package com.shumeit.sdk.api.utils;

import com.shumeit.sdk.api.entity.Callback;
import com.shumeit.sdk.api.entity.Request;
import com.shumeit.sdk.api.entity.Response;
import com.shumeit.sdk.api.exceptions.SDKException;

import java.lang.reflect.Field;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.*;

/**
 * Author:
 * Date: 2022.2.23
 * Time: 11:36
 * Description: 签名、验签的工具类
 */
public class SecurityUtil {

    public final static String SIGN_SEPARATOR = "&";//分隔符
    public final static String SIGN_EQUAL = "=";//等于号
    public final static String DATA_KEY = "data";
    public final static Set<String> NOT_SIGN_PARAM = new HashSet<>(Arrays.asList("sign", "secKey"));//不参与签名/验签的参数

    private PublicKey publicKey;
    private PrivateKey privateKey;
    private volatile boolean inited;

    public SecurityUtil(String platPubKey, String mchPriKey) {
        this.init(platPubKey, mchPriKey);
    }

    private synchronized void init(String platPubKey, String mchPriKey) {
        if (inited) {
            throw new SDKException(SDKException.COMMON_EX_CODE, "SecurityUtil已初始化,请勿重复调用");
        }
        try {
            publicKey = RSAUtil.parsePublicKey(platPubKey);
            privateKey = RSAUtil.parsePrivateKey(mchPriKey);
            inited = true;
        } catch (Exception e) {
            throw new SDKException(SDKException.COMMON_EX_CODE, "SecurityUtil初始化失败", e);
        }
    }

    private void checkInit() {
        if (!inited) {
            throw new SDKException(SDKException.COMMON_EX_CODE, "SecurityUtil未初始化,请先调用SecurityUtil.init完成初始化");
        }
    }

    /**
     * 同步响应时验证签名
     *
     * @param response .
     * @return .
     */
    public boolean verify(Response<String> response) {
        checkInit();
        String signStr = getSortedString(response);
        return RSAUtil.verify(signStr, publicKey, response.getSign());
    }

    /**
     * 异步回调时验证签名
     *
     * @param callback .
     * @return .
     */
    public boolean verify(Callback<String> callback) {
        checkInit();
        String signStr = getSortedString(callback);
        return RSAUtil.verify(signStr, publicKey, callback.getSign());

    }

    /**
     * 签名
     *
     * @param request .
     */
    public void sign(Request<?> request) {
        checkInit();
        if (request == null) {
            throw new SDKException(SDKException.PARAM_CHECK_FAIL_CODE, "request为null");
        }

        String signStr = getSortedString(request);
        String sign = RSAUtil.sign(signStr, privateKey);
        request.setSign(sign);
    }

    /**
     * 使用平台公钥加密secKey明文进行加密
     *
     * @param secKeyPlainText .
     * @return .
     */
    public String encryptSecKey(String secKeyPlainText) {
        return RSAUtil.encryptByPublicKey(secKeyPlainText, publicKey);
    }

    /**
     * 使用商户密钥对secKey密文进行解密
     *
     * @param secKeyCipherText .
     * @return .
     */
    public String decryptSecKey(String secKeyCipherText) {
        return RSAUtil.decryptByPrivateKey(secKeyCipherText, privateKey);
    }

    private static String getSortedString(Object obj) {
        Field[] fields = obj.getClass().getDeclaredFields();

        Map<String, Object> signMap = new TreeMap<>();
        for (Field filed : fields) {
            String name = filed.getName();
            if (NOT_SIGN_PARAM.contains(name)) {//不参与签名或验签的参数直接跳过
                continue;
            }
            filed.setAccessible(true);
            try {
                signMap.put(name, filed.get(obj));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        StringBuilder content = new StringBuilder();
        for (Map.Entry<String, Object> entry : signMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (Objects.equals(key, DATA_KEY) && !(value instanceof String)) {
                value = JsonUtil.toString(value);
            }
            content.append(key).append(SIGN_EQUAL);
            content.append(value);
            content.append(SIGN_SEPARATOR);
        }
        content.deleteCharAt(content.lastIndexOf(SIGN_SEPARATOR));
        return content.toString();
    }
}
