package com.yude.protocol.common.constant;

/**
 * @Author: HH
 * @Date: 2020/6/23 14:59
 * @Version: 1.0
 * @Declare:
 */
public enum StatusCodeEnum implements StatusCodeI {


    /**
     * 成功
     */
    SUCCESS(200, "success"),

    /**
     * 失败
     */
    FAIL(-1, "fail"),;





    private int code;

    private String msg;

    StatusCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public int code() {
        return code;
    }

    @Override
    public String msg() {

        return msg;
    }


}
