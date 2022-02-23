package com.shumeit.sdk.api.exceptions;

public class SDKException extends RuntimeException {
    public static final String COMMON_EX_CODE = "SHUMEIT.EX001";
    public static final String PARAM_CHECK_FAIL_CODE = "SHUMEIT.EX002";
    public static final String HTTP_CONNECT_FAIL_CODE = "SHUMEIT.EX003";
    public static final String HTTP_COMMUNICATION_EX_CODE = "SHUMEIT.EX004";
    public static final String RESP_SECURITY_VERIFY_FAIL_CODE = "SHUMEIT.EX005";

    private final String code;

    public String getCode() {
        return code;
    }

    public SDKException(String code, String message) {
        super(code + "," + message);
        this.code = code;
    }

    public SDKException(String code, String message, Throwable cause) {
        super(code + "," + message, cause);
        this.code = code;
    }
}
