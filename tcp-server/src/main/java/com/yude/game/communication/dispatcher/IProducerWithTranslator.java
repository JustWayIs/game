package com.yude.game.communication.dispatcher;

import com.yude.protocol.common.message.IMessage;
import io.netty.channel.ChannelHandlerContext;

/**
 * @Author: HH
 * @Date: 2020/6/19 15:37
 * @Version: 1.0
 * @Declare: 事件发布者
 */
public interface IProducerWithTranslator<T extends IMessage> {

    void publish(T t, ChannelHandlerContext context) throws Exception;
}
