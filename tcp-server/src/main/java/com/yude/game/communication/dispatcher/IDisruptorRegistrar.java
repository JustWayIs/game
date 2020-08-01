package com.yude.game.communication.dispatcher;

import com.yude.protocol.common.MessageType;

/**
 * @Author: HH
 * @Date: 2020/7/31 11:45
 * @Version: 1.0
 * @Declare:
 */
public interface IDisruptorRegistrar {
    /**
     * 根据消息类型和roomId获得一个事件发布器
     * @param messageType
     * @param roomId
     * @return
     */
    IProducerWithTranslator getEventPublisher(MessageType messageType, Long roomId);
}
