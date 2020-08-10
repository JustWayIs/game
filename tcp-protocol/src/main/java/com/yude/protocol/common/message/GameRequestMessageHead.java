package com.yude.protocol.common.message;


import com.baidu.bjf.remoting.protobuf.annotation.EnableZigZap;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;

/**
 * @Author: HH
 * @Date: 2020/6/9 21:21
 * @Version： 1.0
 * @Declare： 请求报文头部
 */

@ProtobufClass
@EnableZigZap
public class GameRequestMessageHead {
    /**
     * 协议号
     */
    private Integer cmd;
    private Integer type;
    private String sessionId;
    private Long roomId;


    public Integer getCmd() {
        return cmd;
    }

    public GameRequestMessageHead setCmd(Integer cmd) {
        this.cmd = cmd;
        return this;
    }

    public Integer getType() {
        return type;
    }

    public GameRequestMessageHead setType(Integer type) {
        this.type = type;
        return this;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }



    @Override
    public String toString() {
        return "GameRequestMessageHead{" +
                "cmd=" + Integer.toHexString(cmd) +
                ", type=" + type +
                ", sessionId='" + sessionId + '\'' +
                ", roomId=" + roomId +
                '}';
    }

}