package com.demo.i18nmgt.apis.common.constants;

/**
 * RespCode
 *
 * @author Z
 * @date 2018/10/21
 */
public enum RespCode {
    /**
     * 成功
     */
    SUCCESS("00000000", "Success."),

    /**
     * 失败
     */
    FAILURE("00000001", "Internal Error."),

    /**
     * 请求参数为空
     */
    EMPTY_PARAMETER("00000002", "Empty parameter."),

    /**
     * 请求参数非法
     */
    INVALID_PARAMETER("00000003", "Invalid parameter."),

    /**
     * 用户已存在
     */
    USER_EXIST("00000100", "User does exist."),

    /**
     * 用户不存在
     */
    USER_NOT_EXIST("00000101", "User does not exist."),

    /**
     * 鉴权失败
     */
    AUTHENTICATE_FAILURE("00000200", "Failed to authenticate."),

    /**
     * 命名空间不存在
     */
    NAMESPACE_NOT_EXIST("00000300", "Namespace does not exist."),

    /**
     * 语言不存在
     */
    LANGAUGE_NOT_EXIST("00000310", "Language does not exist."),

    /**
     * 语言已存在
     */
    LANGAUGE_EXIST("00000311", "Language does exist."),

    /**
     * 记录不存在
     */
    RECORD_NOT_EXIST("00000320", "Record does not exist.");

    RespCode(String code, String description) {
        this.code = code;
        this.description = description;
    }

    private String code;

    private String description;

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
