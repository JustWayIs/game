package com.yude.game.common.dispatcher.disruptor;

import com.yude.game.common.dispatcher.event.DisruptorLoadBalance;
import com.yude.protocol.common.MessageType;
import org.springframework.stereotype.Component;

/**
 * @Author: HH
 * @Date: 2020/7/15 11:12
 * @Version: 1.0
 * @Declare:
 */
@Component
public class HeartBeatDisruptor implements EventDisruptor {
    @Override
    public DisruptorLoadBalance init() {
        return null;
    }

    @Override
    public EventDisruptor match(MessageType messageType) {
        return null;
    }
}
