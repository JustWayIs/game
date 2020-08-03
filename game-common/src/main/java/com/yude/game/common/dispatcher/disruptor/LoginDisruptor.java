package com.yude.game.common.dispatcher.disruptor;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.yude.game.common.dispatcher.event.DisruptorLoadBalance;
import com.yude.game.common.dispatcher.event.MessageReceiveEvent;
import com.yude.game.common.dispatcher.event.factory.threadFactory.CustomDisruptorThreadFactory;
import com.yude.game.common.dispatcher.event.translator.ReceiveMessageEventProducer;
import com.yude.game.communication.dispatcher.IProducerWithTranslator;
import com.yude.protocol.common.MessageType;
import org.springframework.stereotype.Component;


import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.concurrent.ThreadFactory;

/**
 * @Author: HH
 * @Date: 2020/6/22 21:07
 * @Version: 1.0
 * @Declare:
 */
@Component
public class LoginDisruptor implements EventDisruptor {
    private final int disruptorNum = 1;

    private final int bufferSize = 2 << 14;

    /*@Autowired()
    @Qualifier("messageReceiveEventHandler")*/
    @Resource(name="loginEventHandler")
    private EventHandler messageEventHandler;


    /*@Autowired
    @Qualifier("messageReceiveEventFactory")*/
    @Resource(name="messageReceiveEventFactory")
    private EventFactory messageReceiveEventFactory;

    private final ThreadFactory threadFactory = CustomDisruptorThreadFactory.LOGIN;

    //还有一种实现方式,在接口层添加 get 上面所有属性的方法，然后传参进接口的init，在接口层做实现
    @Override
    public DisruptorLoadBalance init() {
        DisruptorLoadBalance loadBalance = new DisruptorLoadBalance(new ArrayList<>());
        WaitStrategy waitStrategy = new BlockingWaitStrategy();
        for(int i = 0 ; i < disruptorNum ; ++i){
            Disruptor<MessageReceiveEvent> disruptor = new Disruptor<>(messageReceiveEventFactory,bufferSize,threadFactory, ProducerType.MULTI,waitStrategy);

            //disruptor既支持并行，也支持串行
            //handleEventsWith(handler1,handler2).handleEventsWith(handler3) : 1 和 2是并行，3是要等到1和2执行完再执行
            disruptor.handleEventsWith(messageEventHandler);
            RingBuffer<MessageReceiveEvent> ringBuffer = disruptor.start();
            IProducerWithTranslator producerWithTranslator = new ReceiveMessageEventProducer(ringBuffer);
            loadBalance.addTranslator(producerWithTranslator);

        }
        return loadBalance;
    }

    @Override
    public EventDisruptor match(MessageType messageType) {
        return MessageType.LOGIN.equals(messageType) ? this : null;
    }
}
