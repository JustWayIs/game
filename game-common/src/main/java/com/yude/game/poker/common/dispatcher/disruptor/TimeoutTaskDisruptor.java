package com.yude.game.poker.common.dispatcher.disruptor;

import com.yude.game.poker.common.dispatcher.event.DisruptorLoadBalance;
import com.yude.protocol.common.MessageType;
import org.springframework.stereotype.Component;

/**
 * @Author: HH
 * @Date: 2020/7/15 11:13
 * @Version: 1.0
 * @Declare:
 */
@Component
public class TimeoutTaskDisruptor implements EventDisruptor {

/*    @Autowired
    @Qualifier("serviceDisruptor")
    EventDisruptor serviceDisruptor;*/

    @Override
    public DisruptorLoadBalance init() {
        return null;
    }

    @Override
    public EventDisruptor match(MessageType messageType) {
        return null;
    }
}
