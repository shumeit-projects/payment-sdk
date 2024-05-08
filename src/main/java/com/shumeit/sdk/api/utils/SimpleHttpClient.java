package com.shumeit.sdk.api.utils;

import com.shumeit.sdk.api.exceptions.SDKException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Author: Cmf
 * Date: 2022.2.22
 * Time: 14:14
 * Description:
 */
public class SimpleHttpClient {
    private static final Logger logger = LoggerFactory.getLogger(SimpleHttpClient.class);
    private static final int DEFAULT_READ_BUF_SIZE = 32 * 1024;        //32K
    private static final int DEFAULT_CONN_TIME_OUT = 10 * 1000;        //10s
    private static final int DEFAULT_READ_TIME_OUT = 20 * 1000;        //20s

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
            os.flush();
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

    public static void uploadFile(String url, Map<String, String> stringParts, Map<String, File> fileParts) {
        checkParam(url, "{}", DEFAULT_READ_BUF_SIZE, DEFAULT_CONN_TIME_OUT, DEFAULT_READ_TIME_OUT);

        String boundary = ("Boundary" + UUID.randomUUID()).replaceAll("-", "");
        byte[] boundaryBytes = boundary.getBytes(StandardCharsets.UTF_8);
        long contentLength = writeOrCountBytes(null, boundaryBytes, stringParts, fileParts, true);

        HttpURLConnection conn;
        try {
            conn = ((HttpURLConnection) new URL(url).openConnection());
            conn.setFixedLengthStreamingMode(contentLength);
            conn.setConnectTimeout(DEFAULT_CONN_TIME_OUT);
            conn.setReadTimeout(DEFAULT_READ_TIME_OUT);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            conn.setDoOutput(true);
            conn.connect();
        } catch (Exception ex) {
            throw new SDKException(SDKException.HTTP_CONNECT_FAIL_CODE, "创建http连接失败", ex);
        }

        OutputStream os = null;
        InputStreamReader is = null;
        try {
            os = conn.getOutputStream();
            writeOrCountBytes(os, boundaryBytes, stringParts, fileParts, false);
            os.flush();
            if (conn.getResponseCode() != 200) {
                StringBuilder sb = new StringBuilder();
                char[] readBuf = new char[DEFAULT_READ_BUF_SIZE];
                is = new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8);
                int nRead;
                while ((nRead = is.read(readBuf)) != -1) {
                    sb.append(readBuf, 0, nRead);
                }
                throw new SDKException(SDKException.HTTP_COMMUNICATION_EX_CODE, "http通信过程中出现异常:" + sb);
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

    //region 用于文件上传
    private static final byte[] CRLF = {'\r', '\n'};
    private static final byte[] DASHDASH = {'-', '-'};

    private static long writeOrCountBytes(OutputStream os, byte[] boundary, Map<String, String> stringParts, Map<String, File> fileParts, boolean countBytes) {
        int fileContentSize = 0;
        LinkedHashMap<String, Object> allParts = new LinkedHashMap<>();
        allParts.putAll(stringParts);
        allParts.putAll(fileParts);
        if (countBytes) {
            os = new ByteArrayOutputStream();
        }

        try {
            for (Map.Entry<String, Object> entry : allParts.entrySet()) {
                String k = entry.getKey();
                Object v = entry.getValue();

                //写part头部
                os.write(DASHDASH);
                os.write(boundary);
                os.write(CRLF);
                os.write("Content-Disposition: form-data; name=\"".getBytes(StandardCharsets.UTF_8));
                os.write(k.getBytes(StandardCharsets.UTF_8));
                os.write("\"".getBytes(StandardCharsets.UTF_8));
                if (v instanceof File) {
                    os.write("; filename=\"".getBytes());
                    os.write(((File) v).getName().getBytes(StandardCharsets.UTF_8));
                    os.write("\"".getBytes(StandardCharsets.UTF_8));
                }
                os.write(CRLF);

                //part头部与内容分割
                os.write(CRLF);

                //part内容
                if (v instanceof String) {
                    os.write(((String) v).getBytes(StandardCharsets.UTF_8));
                } else {
                    File file = (File) v;
                    fileContentSize += file.length();
                    if (!countBytes) {
                        Files.copy(Paths.get(file.toURI()), os);
                    }
                }
                os.write(CRLF);
            }

            os.write(DASHDASH);
            os.write(boundary);
            os.write(DASHDASH);
            os.write(CRLF);

            if (countBytes) {
                return fileContentSize + ((ByteArrayOutputStream) os).size();
            } else {
                return 0;
            }
        } catch (IOException e) {
            throw new SDKException(SDKException.HTTP_COMMUNICATION_EX_CODE, "上传文件失败", e);
        }
    }
    //endregion
}
