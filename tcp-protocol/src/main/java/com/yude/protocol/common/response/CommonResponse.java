package com.yude.protocol.common.response;

import com.baidu.bjf.remoting.protobuf.annotation.EnableZigZap;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.yude.protocol.common.constant.StatusCodeEnum;
import com.yude.protocol.common.constant.StatusCodeI;


/**
 * @Author: HH
 * @Date: 2020/6/23 14:55
 * @Version: 1.0
 * @Declare:
 */
@ProtobufClass
@EnableZigZap
public class CommonResponse implements Response{

    private String msg;

    private int code;

    public CommonResponse() {
    }

    public CommonResponse(int code){
        this.code = code;
    }

    public CommonResponse(String msg, int code) {
        this.msg = msg;
        this.code = code;
    }

    public CommonResponse(StatusCodeI status){
        this.msg = status.msg();
        this.code = status.code();
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public boolean isSuccess() {
        return code == StatusCodeEnum.SUCCESS.code();
    }


    @Override
    public String toString() {
        return "CommonResponse{" +
                "msg='" + msg + '\'' +
                ", code=" + code +
                '}';
    }
}
