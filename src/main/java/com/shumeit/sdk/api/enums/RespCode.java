package com.shumeit.sdk.api.enums;

/**
 * 响应码枚举
 */
public enum RespCode {
    SUCCESS("success"),
    FAIL("fail"),
    UNKNOWN("unknown");

    private final String code;

    public String getCode() {
        return code;
    }

    RespCode(String code) {
        this.code = code;
    }
}
