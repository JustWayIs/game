package com.yude.game.common.dispatcher.event;

import com.yude.protocol.common.message.GameResponseMessage;
import io.netty.channel.ChannelHandlerContext;

/**
 * @Author: HH
 * @Date: 2020/6/22 21:29
 * @Version: 1.0
 * @Declare:
 */
public class MessagePushEvent {
    private GameResponseMessage message;
    private ChannelHandlerContext context;

    public GameResponseMessage getMessage() {
        return message;
    }

    public void setMessage(GameResponseMessage message) {
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
        return "MessagePushEvent{" +
                "message=" + message +
                ", context=" + context +
                '}';
    }
}
