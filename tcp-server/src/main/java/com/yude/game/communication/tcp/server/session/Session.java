package com.yude.game.communication.tcp.server.session;

import com.yude.protocol.common.message.IMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

/**
 * @Author: HH
 * @Date: 2020/6/18 17:59
 * @Version: 1.0
 * @Declare:
 */
public class Session {

    private String SessionId;
    private ChannelHandlerContext context;

    public String getSessionId() {
        return SessionId;
    }

    public void setSessionId(String sessionId) {
        SessionId = sessionId;
    }

    public ChannelHandlerContext getContext() {
        return context;
    }

    public void setContext(ChannelHandlerContext context) {
        this.context = context;
    }

    public void close(){
        Channel channel = context.channel();
        //if (channel.isActive()) {
        if (channel.isOpen()) {
            channel.close();
        }


    }

    public void writeByContext(IMessage pushMessage){
        context.write(pushMessage);
    }

    /**
     * 在pipeline中向前找出站handler
     * @param pushMessage
     */
    public void writeAndFlushByContext(IMessage pushMessage){
        context.writeAndFlush(pushMessage);
    }

    public void writeByChannel(IMessage pushMessage){
        context.channel().write(pushMessage);
    }

    /**
     * 从pipeline末端开始执行出站handler
     * @param pushMessage
     */
    public void writeAndFlushByChannel(IMessage pushMessage){
        context.channel().writeAndFlush(pushMessage);
    }

    @Override
    public String toString() {
        return "Session{" +
                "SessionId='" + SessionId + '\'' +
                '}';
    }
}
