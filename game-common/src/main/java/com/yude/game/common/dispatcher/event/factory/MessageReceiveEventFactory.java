package com.yude.game.common.dispatcher.event.factory;

import com.lmax.disruptor.EventFactory;
import com.yude.game.common.dispatcher.event.MessageReceiveEvent;
import org.springframework.stereotype.Component;

/**
 * @Author: HH
 * @Date: 2020/6/17 16:19
 * @Version: 1.0
 * @Declare:
 */
@Component("messageReceiveEventFactory")
public class MessageReceiveEventFactory implements EventFactory<MessageReceiveEvent> {
    @Override
    public MessageReceiveEvent newInstance() {
        return new MessageReceiveEvent();
    }

}
