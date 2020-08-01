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

    //H2 具体属性需要与客户端协调

    private String sessionId;

    private int type;

    private int cmd;

    private Long userId;

    /**
     * 接收到请求的时间，可用纳秒表示。用来做时间同步 ，仅当clientSendTime存在值的时候才记录 -- 写在这里倒是可以省略时间同步这条协议，根据客户端需求，响应时间给客户端做同步。
     */
    private long requestTime;
    /**
     * 响应的时间，可用纳秒表示。用来做时间同步 ，仅当clientSendTime存在值的时候才记录
     */
    private long responseTime;


    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public long getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(long requestTime) {
        this.requestTime = requestTime;
    }

    public long getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(long responseTime) {
        this.responseTime = responseTime;
    }

    @Override
    public String toString() {
        return "GameResponseMessageHead{" +
                "sessionId='" + sessionId + '\'' +
                ", type=" + type +
                ", cmd=" + Integer.toHexString(cmd) +
                ", userId=" + userId +
                ", requestTime=" + requestTime +
                ", responseTime=" + responseTime +
                '}';
    }
}
