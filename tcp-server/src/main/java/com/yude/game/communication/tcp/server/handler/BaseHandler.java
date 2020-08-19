package com.yude.game.communication.tcp.server.handler;

import com.yude.game.communication.dispatcher.IDisruptorRegistrar;
import com.yude.game.communication.dispatcher.IProducerWithTranslator;
import com.yude.game.communication.dispatcher.IRequestMappingInfo;
import com.yude.protocol.common.MessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: HH
 * @Date: 2020/7/31 14:19
 * @Version: 1.0
 * @Declare:
 */

@Component
public class BaseHandler {

    @Autowired
    private IDisruptorRegistrar disruptorRegistrar;

    @Autowired
    private IRequestMappingInfo requestMappingInfo;

    public MessageType getMessageTypeByCommand(Integer cmd){
        return requestMappingInfo.getMessageTypByCommand(cmd);
    }

    public boolean canMultithreaded(Integer cmd){
        return requestMappingInfo.canMultithreaded(cmd);
    }

    public IProducerWithTranslator getEventPublisher(MessageType messageType, Long roomId){
        return disruptorRegistrar.getEventPublisher(messageType,roomId);
    }
}
