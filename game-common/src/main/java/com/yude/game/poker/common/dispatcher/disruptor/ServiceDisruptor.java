package com.yude.game.poker.common.dispatcher.disruptor;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.yude.game.communication.dispatcher.IProducerWithTranslator;
import com.yude.game.poker.common.dispatcher.event.DisruptorLoadBalance;
import com.yude.game.poker.common.dispatcher.event.MessageReceiveEvent;
import com.yude.game.poker.common.dispatcher.event.factory.threadFactory.CustomDisruptorThreadFactory;
import com.yude.game.poker.common.dispatcher.event.translator.ReceiveMessageEventProducer;
import com.yude.protocol.common.MessageType;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.concurrent.ThreadFactory;

/**
 * @Author: HH
 * @Date: 2020/6/22 21:07
 * @Version: 1.0
 * @Declare: 非线程安全的：只用于启动初始化
 */
@Component
public class ServiceDisruptor implements EventDisruptor {
    //H2 可以通过配置文件设置
    private int disruptorNum = 2;//Runtime.getRuntime().availableProcessors()-2;

    private int bufferSize = 2 << 13;

    @Resource(name="messageReceiveEventHandler")
    private EventHandler messageEventHandler;

    @Resource(name="messageReceiveEventFactory")
    private EventFactory messageReceiveEventFactory;

    private final ThreadFactory threadFactory = CustomDisruptorThreadFactory.SERVICE;


    @Override
    public DisruptorLoadBalance init() {
        DisruptorLoadBalance loadBalance = new DisruptorLoadBalance(new ArrayList<>());
        /**
         * BusySpinWaitStrategy：自旋等待，类似Linux Kernel使用的自旋锁。低延迟但同时对CPU资源的占用也多。
         *
         * BlockingWaitStrategy ：使用锁和条件变量。CPU资源的占用少，延迟大。
         *
         * SleepingWaitStrategy ：在多次循环尝试不成功后，选择让出CPU，等待下次调度，多次调度后仍不成功，尝试前睡眠一个纳秒级别的时间再尝试。这种策略平衡了延迟和CPU资源占用，但延迟不均匀。
         *
         * YieldingWaitStrategy ：在多次循环尝试不成功后，选择让出CPU，等待下次调度。平衡了延迟和CPU资源占用，但延迟比较均匀。
         *
         * PhasedBackoffWaitStrategy ：上面多种策略的综合，CPU资源的占用少，延迟大。
         */
        WaitStrategy waitStrategy = new SleepingWaitStrategy();
        for(int i = 0 ; i < disruptorNum ; ++i){
            Disruptor<MessageReceiveEvent> disruptor = new Disruptor<>(messageReceiveEventFactory,bufferSize,threadFactory, ProducerType.MULTI,waitStrategy);

            disruptor.handleEventsWith(messageEventHandler);
            RingBuffer<MessageReceiveEvent> ringBuffer = disruptor.start();
            IProducerWithTranslator producerWithTranslator = new ReceiveMessageEventProducer(ringBuffer);
            loadBalance.addTranslator(producerWithTranslator);

        }
        return loadBalance;
    }

    @Override
    public EventDisruptor match(MessageType messageType) {
        return MessageType.SERVICE.equals(messageType) ? this : null;
    }
}
