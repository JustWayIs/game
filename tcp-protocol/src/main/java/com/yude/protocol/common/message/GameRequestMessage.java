package com.yude.protocol.common.message;


import com.baidu.bjf.remoting.protobuf.Any;
import com.baidu.bjf.remoting.protobuf.annotation.EnableZigZap;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;

/**
 * @Author: HH
 * @Date: 2020/6/9 21:05
 * @Version： 1.0
 * @Declare： 请求报文
 */
@ProtobufClass
@EnableZigZap
public  class GameRequestMessage implements IMessage{

    private GameRequestMessageHead head;

    private Any object;

    public GameRequestMessageHead getHead() {
        return head;
    }

    public void setHead(GameRequestMessageHead head) {
        this.head = head;
    }


    public Any getObject() {
        return object;
    }

    public void setObject(Any object) {
        this.object = object;
    }

    @Override
    public String toString() {
        return "GameRequestMessage{" +
                "head=" + head +
                ", object=" + object +
                '}';
    }
}
