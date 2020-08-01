package com.yude.game.poker.common.dispatcher.event.factory;

import com.lmax.disruptor.EventFactory;
import com.yude.game.poker.common.dispatcher.event.MessagePushEvent;
import org.springframework.stereotype.Component;

/**
 * @Author: HH
 * @Date: 2020/6/22 21:54
 * @Version: 1.0
 * @Declare:
 */
@Component("messagePushEventFactory")
public class MessagePushEventFactory implements EventFactory<MessagePushEvent> {
    @Override
    public MessagePushEvent newInstance() {
        return new MessagePushEvent();
    }
}
