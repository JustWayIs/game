package com.yude.game.common.dispatcher.disruptor;

import com.yude.game.common.dispatcher.event.DisruptorLoadBalance;
import com.yude.protocol.common.MessageType;

/**
 * @Author: HH
 * @Date: 2020/6/22 22:10
 * @Version: 1.0
 * @Declare:
 */
public interface EventDisruptor {
    /**
     * 初始化
     * @return
     */
    DisruptorLoadBalance init();

    EventDisruptor match(MessageType messageType);
}
