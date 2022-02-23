package com.shumeit.sdk.api.utils;

import com.alibaba.fastjson.util.ParameterizedTypeImpl;
import com.shumeit.sdk.api.entity.Request;
import com.shumeit.sdk.api.entity.Response;
import com.shumeit.sdk.api.exceptions.SDKException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.Objects;

/**
 * 请求处理工具类
 */
public class RequestUtil {
    private static final Logger logger = LoggerFactory.getLogger(RequestUtil.class);
    private static final Type responseType = new ParameterizedTypeImpl(new Type[]{String.class}, null, Response.class);

    /**
     * <p>发起交易请求</p>
     * <p>说明：</p>
     * <ul>
     * <li>1.调用此方法前请先调用SecurityUtil.init方法完成公私钥的加载</li>
     * <li>2.调用此方法后,request.data如果不是String,方法内会将其序列化成json字符串,并设置到request.data中。</li>
     * <li>3.调用此方法后,方法内将会对request生成签名,并设置到request.sign中。</li>
     * <li>4.调用此方法后,request.secKey如果不为空,方法内会将对secKey进行RSA加密,并设置到request.secKey中。</li>
     * <li>5.调用此方法并得到响应后,方法内将对响应进行验签,如果验签不通过,则会抛出SDKException</li>
     * <li>6.调用此方法并得到响应后,response.secKey如果不为空，方法内将会对secKey进行RSA解密,并设置到response.secKey中。</li>
     * </ul>
     *
     * @param url     .
     * @param request .
     * @return .
     * @throws SDKException .
     */
    public static Response<String> doRequest(String url, Request request) throws SDKException {
        //1.参数简单校验
        if (StringUtil.isEmpty(request.getRandStr())) {
            request.setRandStr(StringUtil.gen32LenRand());
        }
        requestParamValid(request);

        //2.添加签名
        if (!(request.getData() instanceof String)) {
            request.setData(JsonUtil.toString(request.getData()));
        }
        SecurityUtil.sign(request);

        //3.对secKey执行rsa加密
        if (!StringUtil.isEmpty(request.getSecKey())) {
            request.setSecKey(SecurityUtil.encryptSecKey(request.getSecKey()));
        }

        //5.发起http(s)请求
        String respJson = SimpleHttpClient.post(url, JsonUtil.toString(request));
        if (StringUtil.isEmpty(respJson)) {
            throw new SDKException(SDKException.COMMON_EX_CODE, "请求完成，但响应信息为空");
        }

        //6.对响应数据进行对象转换
        Response<String> response;
        try {
            response = JsonUtil.toBean(respJson, responseType);
        } catch (Exception e) {
            throw new SDKException(SDKException.COMMON_EX_CODE, "请求完成，但响应信息转换失败: " + e.getMessage() + "，respJson=" + respJson, e);
        }

        //7.对响应数据进行验签
        boolean isSignOk;
        try {
            isSignOk = SecurityUtil.verify(response);
        } catch (Exception e) {
            System.out.println(respJson);
            throw new SDKException(SDKException.RESP_SECURITY_VERIFY_FAIL_CODE, "请求完成，但响应信息验签出现异常", e);
        }
        if (!isSignOk) {
            logger.error("验签失败:respJson={}", respJson);
            throw new SDKException(SDKException.RESP_SECURITY_VERIFY_FAIL_CODE, "响应数据验签不通过");
        } else if (!Objects.equals(response.getRandStr(), request.getRandStr())) {
            throw new SDKException(SDKException.RESP_SECURITY_VERIFY_FAIL_CODE, "randStr校验不通过");
        }

        //8.对响应数据中的sec_key进行rsa解密
        if (!StringUtil.isEmpty(response.getSecKey())) {
            String secKeyPlainText = SecurityUtil.decryptSecKey(response.getSecKey());
            response.setSecKey(secKeyPlainText);
        }

        return response;
    }

    private static void requestParamValid(Request<?> request) {
        if (request == null) {
            throw new SDKException(SDKException.PARAM_CHECK_FAIL_CODE, "request不能为空");
        } else if (StringUtil.isEmpty(request.getMethod())) {
            throw new SDKException(SDKException.PARAM_CHECK_FAIL_CODE, "request.method不能为空");
        } else if (StringUtil.isEmpty(request.getVersion())) {
            throw new SDKException(SDKException.PARAM_CHECK_FAIL_CODE, "request.version不能为空");
        } else if (request.getData() == null) {
            throw new SDKException(SDKException.PARAM_CHECK_FAIL_CODE, "request.data不能为空");
        } else if (StringUtil.isEmpty(request.getRandStr())) {
            throw new SDKException(SDKException.PARAM_CHECK_FAIL_CODE, "request.randStr不能为空");
        } else if (Objects.equals("RSA", request.getSignType())) {
            throw new SDKException(SDKException.PARAM_CHECK_FAIL_CODE, "request.signType仅支持RSA");
        } else if (StringUtil.isEmpty(request.getMchNo())) {
            throw new SDKException(SDKException.PARAM_CHECK_FAIL_CODE, "request.mchNo不能为空");
        }
    }

}
