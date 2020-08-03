package com.yude.game.common.dispatcher.disruptor;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.yude.game.communication.dispatcher.IProducerWithTranslator;
import com.yude.game.common.dispatcher.event.DisruptorLoadBalance;
import com.yude.game.common.dispatcher.event.MessagePushEvent;
import com.yude.game.common.dispatcher.event.factory.threadFactory.CustomDisruptorThreadFactory;
import com.yude.game.common.dispatcher.event.translator.PushMessageEventProducer;
import com.yude.protocol.common.MessageType;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.concurrent.ThreadFactory;

/**
 * @Author: HH
 * @Date: 2020/6/22 21:08
 * @Version: 1.0
 * @Declare:
 */
@Component
public class PushMessageDisruptor implements EventDisruptor {
    //H2 可以通过配置文件设置
    private int disruptorNum = 6;

    private int bufferSize = 2 << 13;

    @Resource(name="messagePushEventHandler")
    private EventHandler messageEventHandler;

    @Resource(name="messagePushEventFactory")
    private EventFactory messageEventFactory;

    private final ThreadFactory threadFactory = CustomDisruptorThreadFactory.PUSH_MESSAGE;

    @Override
    public DisruptorLoadBalance init() {
        DisruptorLoadBalance loadBalance = new DisruptorLoadBalance(new ArrayList<>());
        WaitStrategy waitStrategy = new BlockingWaitStrategy();
        for(int i = 0 ; i < disruptorNum ; ++i){
            Disruptor<MessagePushEvent> disruptor = new Disruptor<>(messageEventFactory,bufferSize,threadFactory, ProducerType.MULTI,waitStrategy);

            disruptor.handleEventsWith(messageEventHandler);
            RingBuffer<MessagePushEvent> ringBuffer = disruptor.start();
            IProducerWithTranslator producerWithTranslator = new PushMessageEventProducer(ringBuffer);
            loadBalance.addTranslator(producerWithTranslator);

        }
        return loadBalance;
    }

    @Override
    public EventDisruptor match(MessageType messageType) {
        return MessageType.PUSH_MESSAGE.equals(messageType) ? this : null;
    }
}
