package com.yude.protocol.common;

import com.alibaba.fastjson.JSONObject;
import com.baidu.bjf.remoting.protobuf.Any;
import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.yude.protocol.common.message.GameRequestMessage;
import com.yude.protocol.common.message.GameResponseMessage;
import com.yude.protocol.common.message.GameResponseMessageHead;
//import com.yude.protocol.poker.ddz.response.LoginResponse;


import java.io.IOException;

/**
 * @Author: HH
 * @Date: 2020/6/10 19:49
 * @Version 1.0
 * @Declare
 */
public class SerializerTest {

    public static void main(String[] args) throws IOException {
        /*Codec<GameRequestMessage> dcodec = ProtobufProxy.create(GameRequestMessage.class);
        Codec<GameResponseMessage> codec = ProtobufProxy.create(GameResponseMessage.class);


        OperationCardRequest body = handler();
        GameResponseMessage message =  new GameResponseMessage();
        GameResponseMessageHead head = new GameResponseMessageHead();
        //gameRequestMessageHead.setIp("6666.66.66.66");
        head.setCmd(000001);
        //gameRequestMessageHead.setUserId(121100);
        message.setHead(head);


        long beforeJson = System.currentTimeMillis();
        JSONObject.toJSONString(message);
        long timeJson = System.currentTimeMillis() - beforeJson;
        System.out.println("json编码"+timeJson);

        long before = System.currentTimeMillis();
        Any any = Any.pack(body);

        message.setAny(any);

//        System.out.println("------编码前："+ gameRequestMessage);
        byte[] encode = codec.encode(message);
        long time = System.currentTimeMillis() - before;
        System.out.println("编码消耗时间："+time);

        before = System.nanoTime();
        GameResponseMessage messageD = codec.decode(encode);
        time = System.nanoTime() - before;
        System.out.println("解码消耗时间:"+time);

        System.out.println("-------解码后：");
        System.out.println(messageD);

        System.out.println("------Any：");
        System.out.println(messageD.getAny());

        //解析报文体
        OperationCardRequest operationCardRequest = messageD.getAny().unpack(OperationCardRequest.class);

        System.out.println("-----业务数据：");
        System.out.println(operationCardRequest);
    }

    public static OperationCardRequest handler(){
        OperationCardRequest operationCardRequest = new OperationCardRequest();
       *//* outCardRequest.setCard(11);
        outCardRequest.setPosId(0);*//*

        return operationCardRequest;*/
    }
}
