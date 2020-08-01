package com.yude.game.poker.common.dispatcher.event.handler;

import com.lmax.disruptor.EventHandler;
import com.yude.game.poker.common.dispatcher.event.MessagePushEvent;
import com.yude.protocol.common.message.GameResponseMessage;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @Author: HH
 * @Date: 2020/6/22 21:32
 * @Version: 1.0
 * @Declare:
 */
@Component("messagePushEventHandler")
public class MessagePushEventHandler implements EventHandler<MessagePushEvent> {
    private static final Logger log = LoggerFactory.getLogger(MessagePushEventHandler.class);


    @Override
    public void onEvent(MessagePushEvent event, long sequence, boolean endOfBatch) {
        try {
            log.debug("消息开始推送：{}",event);
            ChannelHandlerContext context = event.getContext();
            GameResponseMessage gameResponseMessage = event.getMessage();
            context.writeAndFlush(gameResponseMessage);
        } catch (Exception e) {
            log.error("消息推送异常：{}",event,e);
        }
    }
}
