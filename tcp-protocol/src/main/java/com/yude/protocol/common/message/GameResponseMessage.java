package com.yude.protocol.common.message;

import com.baidu.bjf.remoting.protobuf.Any;
import com.baidu.bjf.remoting.protobuf.annotation.EnableZigZap;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.yude.protocol.common.response.BaseResponse;
import com.yude.protocol.common.response.Response;

/**
 * @Author: HH
 * @Date: 2020/6/10 21:42
 * @Version： 1.0
 * @Declare：响应报文
 */
@ProtobufClass
@EnableZigZap
public class GameResponseMessage implements IMessage{

    private GameResponseMessageHead head;
    private Any any;


    public GameResponseMessageHead getHead() {
        return head;
    }

    public void setHead(GameResponseMessageHead head) {
        this.head = head;
    }

    public Any getAny() {
        return any;
    }

    public void setAny(Any any) {
        this.any = any;
    }

    @Override
    public String toString() {
        return "GameResponseMessage{" +
                "head=" + head +
                ", any=" + any +
                '}';
    }
}
