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
    // ===========================================================
    // Constants
    // ===========================================================


    // ===========================================================
    // Fields
    // ===========================================================
    //H2 需要与客户端协调
    /**
     * 协议号
     */
    private int cmd;
    private Integer type;
    private String sessionId;
    /**
     * 客户端发送请求时的时间，由客户端发送
     */
    private long clientSendTime;
    private Long roomId;

    // ===========================================================
    // Constructors
    // ===========================================================


    // ===========================================================
    // Getter &amp; Setter
    // ===========================================================

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
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

    public long getClientSendTime() {
        return clientSendTime;
    }

    public void setClientSendTime(long clientSendTime) {
        this.clientSendTime = clientSendTime;
    }



    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }


    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================


    // ===========================================================
    // Methods
    // ===========================================================

    @Override
    public String toString() {
        return "GameRequestMessageHead{" +
                "cmd=" + Integer.toHexString(cmd) +
                ", type=" + type +
                ", sessionId='" + sessionId + '\'' +
                ", clientSendTime=" + clientSendTime +
                ", roomId=" + roomId +
                '}';
    }


    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}