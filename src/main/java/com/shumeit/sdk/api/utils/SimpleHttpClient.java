package com.shumeit.sdk.api.utils;

import com.shumeit.sdk.api.exceptions.SDKException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

/**
 * Author: Cmf
 * Date: 2022.2.22
 * Time: 14:14
 * Description:
 */
public class SimpleHttpClient {
    private static final Logger logger = LoggerFactory.getLogger(SimpleHttpClient.class);
    private static final int DEFAULT_READ_BUF_SIZE = 16 * 1024;       //16K
    private static final int DEFAULT_CONN_TIME_OUT = 10 * 1024;       //10s
    private static final int DEFAULT_READ_TIME_OUT = 20 * 1024;       //20s

    public static String post(String url, String content) {
        return post(url, content, DEFAULT_READ_BUF_SIZE, DEFAULT_CONN_TIME_OUT, DEFAULT_READ_TIME_OUT);
    }

    public static String post(String url, String content, int readBufSize) {
        return post(url, content, readBufSize, DEFAULT_CONN_TIME_OUT, DEFAULT_READ_TIME_OUT);
    }

    public static String post(String url, String content, int readBufSize, int connTimeOut, int readTimeOut) {
        checkParam(url, content, readBufSize, connTimeOut, readTimeOut);

        char[] readBuf = new char[readBufSize];
        URLConnection conn;
        try {
            conn = new URL(url).openConnection();
            conn.setConnectTimeout(connTimeOut);
            conn.setReadTimeout(readTimeOut);
            conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
            conn.connect();
        } catch (Exception ex) {
            throw new SDKException(SDKException.HTTP_CONNECT_FAIL_CODE, "创建http连接失败", ex);
        }

        StringBuilder sb = new StringBuilder();
        OutputStream os = null;
        InputStreamReader is = null;
        try {
            os = conn.getOutputStream();
            os.write(content.getBytes(StandardCharsets.UTF_8));
            is = new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8);
            int nRead;
            while ((nRead = is.read(readBuf)) != -1) {
                sb.append(readBuf, 0, nRead);
            }
        } catch (Exception ex) {
            throw new SDKException(SDKException.HTTP_COMMUNICATION_EX_CODE, "http通信过程中出现异常", ex);
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                logger.error("", e);
            }
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                logger.error("", e);
            }
        }
        return sb.toString();
    }

    private static void checkParam(String url, String content, int readBufSize, int connTimeOut, int readTimeOut) {
        if (url == null) {
            throw new SDKException(SDKException.PARAM_CHECK_FAIL_CODE, "url不能为null");
        }
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            throw new SDKException(SDKException.PARAM_CHECK_FAIL_CODE, "url格式不正确");
        }
        if (content == null) {
            throw new SDKException(SDKException.PARAM_CHECK_FAIL_CODE, "content不能为null");
        }
        if (readBufSize <= 0) {
            throw new SDKException(SDKException.PARAM_CHECK_FAIL_CODE, "readBufSize必须大于0");
        }
        if (connTimeOut <= 0 || readTimeOut <= 0) {
            throw new SDKException(SDKException.PARAM_CHECK_FAIL_CODE, "connTimeOut,readTimeOut必须大于0");
        }

    }


    public static void main(String[] args) {
        String r = SimpleHttpClient.post("http://192.168.0.236:58151/trade/doPay", "");
        System.out.println(r);
    }
}
