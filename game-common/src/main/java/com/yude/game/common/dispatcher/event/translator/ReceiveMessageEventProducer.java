package com.yude.game.common.dispatcher.event.translator;

import com.lmax.disruptor.EventTranslatorTwoArg;
import com.lmax.disruptor.RingBuffer;
import com.yude.game.communication.dispatcher.IProducerWithTranslator;
import com.yude.game.common.dispatcher.RequestMappingInfo;
import com.yude.game.common.dispatcher.event.MessageReceiveEvent;
import com.yude.protocol.common.MessageType;
import com.yude.protocol.common.message.GameRequestMessage;
import com.yude.protocol.common.message.GameRequestMessageHead;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * @Author: HH
 * @Date: 2020/6/17 16:20
 * @Version: 1.0
 * @Declare:
 */
public class ReceiveMessageEventProducer implements IProducerWithTranslator<GameRequestMessage> {
    private static final Logger log = LoggerFactory.getLogger(ReceiveMessageEventProducer.class);

    private static final EventTranslatorTwoArg<MessageReceiveEvent, GameRequestMessage,ChannelHandlerContext> translator = (((event, sequence, gameRequestMessage, context) -> {
            event.setMessage(gameRequestMessage);
            event.setContext(context);
    }));
    private RingBuffer<MessageReceiveEvent> ringBuffer;

    public ReceiveMessageEventProducer(RingBuffer<MessageReceiveEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    @Override
    public void publish(GameRequestMessage gameRequestMessage, ChannelHandlerContext context) throws Exception {
        GameRequestMessageHead messageHead = gameRequestMessage.getHead();
        int cmd = messageHead.getCmd();
        MessageType messageType = null;
        if(Objects.nonNull(messageHead.getType())){
            /**
             * 说明是超时自动生成的事件：实际而言上面的判断不是很合理，每一种业务类型的type应该都有值
             */
            messageType = MessageType.matchMessageTypeInstance(messageHead.getType());
            log.info("发布超时任务事件：{}  cmd = {}",messageType,Integer.toHexString(cmd));
        }else{
             messageType = RequestMappingInfo.getHandlerMethodByCmd(cmd).getMessageType();
            log.info("发布事件：{}  cmd = {}",messageType,Integer.toHexString(cmd));
        }

        ringBuffer.publishEvent(translator, gameRequestMessage,context);
    }
}
