package com.yude.protocol.common.message;

import com.baidu.bjf.remoting.protobuf.annotation.EnableZigZap;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;

/**
 * @Author: HH
 * @Date: 2020/6/10 21:43
 * @Version： 1.0
 * @Declare： 响应报文头部
 */
@ProtobufClass
@EnableZigZap
public class GameResponseMessageHead {

    private String sessionId;

    private Integer cmd;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Integer getCmd() {
        return cmd;
    }

    public GameResponseMessageHead setCmd(Integer cmd) {
        this.cmd = cmd;
        return this;
    }

    @Override
    public String toString() {
        return "GameResponseMessageHead{" +
                "sessionId='" + sessionId + '\'' +
                ", cmd=" + Integer.toHexString(cmd) +
                '}';
    }
}
