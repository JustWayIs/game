package com.yude.game.poker.common.dispatcher.event;

import com.yude.protocol.common.message.GameRequestMessage;
import io.netty.channel.ChannelHandlerContext;

/**
 * @Author: HH
 * @Date: 2020/6/17 16:14
 * @Version: 1.0
 * @Declare:
 */
public class MessageReceiveEvent {

    private GameRequestMessage message;

    private ChannelHandlerContext context;

    public GameRequestMessage getMessage() {
        return message;
    }

    public void setMessage(GameRequestMessage message) {
        this.message = message;
    }


    public ChannelHandlerContext getContext() {
        return context;
    }

    public void setContext(ChannelHandlerContext context) {
        this.context = context;
    }

    @Override
    public String toString() {
        return "MessageEvent{" +
                "message=" + message +
                ", context=" + context +
                '}';
    }
}
