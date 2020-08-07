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

    private int cmd;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }


    @Override
    public String toString() {
        return "GameResponseMessageHead{" +
                "sessionId='" + sessionId + '\'' +
                ", cmd=" + Integer.toHexString(cmd) +
                '}';
    }
}
