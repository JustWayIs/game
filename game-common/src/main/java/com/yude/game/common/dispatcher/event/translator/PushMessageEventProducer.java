package com.yude.game.common.dispatcher.event.translator;

import com.lmax.disruptor.EventTranslatorTwoArg;
import com.lmax.disruptor.RingBuffer;
import com.yude.game.communication.dispatcher.IProducerWithTranslator;
import com.yude.game.common.dispatcher.event.MessagePushEvent;
import com.yude.protocol.common.MessageType;
import com.yude.protocol.common.message.GameResponseMessage;
import com.yude.protocol.common.message.GameResponseMessageHead;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: HH
 * @Date: 2020/6/22 21:01
 * @Version: 1.0
 * @Declare:
 */
public class PushMessageEventProducer implements IProducerWithTranslator<GameResponseMessage> {
    private static final Logger log = LoggerFactory.getLogger(PushMessageEventProducer.class);

    private static final EventTranslatorTwoArg<MessagePushEvent, GameResponseMessage,ChannelHandlerContext> translator = (((event, sequence, gameResponseMessage, context) -> {
        event.setMessage(gameResponseMessage);
        event.setContext(context);
    }));
    private RingBuffer<MessagePushEvent> ringBuffer;

    public PushMessageEventProducer(RingBuffer<MessagePushEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }


    @Override
    public void publish(GameResponseMessage gameResponseMessage, ChannelHandlerContext context) {
        GameResponseMessageHead messageHead = gameResponseMessage.getHead();
        int cmd = messageHead.getCmd();
        //由于推送事件的命令码没有用到Controller上，所以这里是找不到的
        // MessageType messageType = RequestMappingInfo.getHandlerMethodByCmd(cmd).getMessageType();
        log.info("发布推送事件：{}  cmd = {}",MessageType.PUSH_MESSAGE,Integer.toHexString(cmd));
        ringBuffer.publishEvent(translator,gameResponseMessage,context);
    }
}
