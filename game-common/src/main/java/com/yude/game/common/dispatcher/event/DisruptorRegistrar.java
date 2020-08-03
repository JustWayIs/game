package com.yude.game.common.dispatcher.event;

import com.yude.game.communication.dispatcher.IDisruptorRegistrar;

import com.yude.game.communication.dispatcher.IProducerWithTranslator;
import com.yude.game.common.dispatcher.disruptor.EventDisruptor;

import com.yude.protocol.common.MessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: HH
 * @Date: 2020/6/19 15:29
 * @Version: 1.0
 * @Declare:
 */
@PropertySource("classpath:config/core.properties")
@Component
public class DisruptorRegistrar implements IDisruptorRegistrar,ApplicationContextAware {
    private static final Logger log = LoggerFactory.getLogger(DisruptorRegistrar.class);
    private ApplicationContext applicationContext;


    /**
     * 初始化完成后只读
     */
    private static Map<MessageType, DisruptorLoadBalance> messageTypeMap = new HashMap();

    /**
     * roomId -> IProducerWithTranslator
     * 当从messageTypeMap获得了一个事件发布者之后，就把roomId与事件发布者建立绑定关系
     */
    private static Map<Long, IProducerWithTranslator> roomTranslatorMap = new ConcurrentHashMap<>();


    /**
     * @param messageType
     * @param roomId   为null时，代表非游戏内的操作业务:表述有误，推送也没有roomId，推送是在游戏内的。其作用是为同一个房间的操作请求指定同一个线程
     * @return
     */
    @Override
    public IProducerWithTranslator getEventPublisher(MessageType messageType, Long roomId) {
        IProducerWithTranslator publisher = needEventPublisher( messageType, roomId);
        return publisher;
    }

    public static IProducerWithTranslator needEventPublisher(MessageType messageType, Long roomId){
        IProducerWithTranslator producerWithTranslator;
        /**
         * 游戏内的业务才会有roomId
         */
        if (roomId != null) {
            producerWithTranslator = roomTranslatorMap.get(roomId);
            if (producerWithTranslator == null) {
                producerWithTranslator = messageTypeMap.get(messageType).getTranslatorByPolling();
                roomTranslatorMap.put(roomId, producerWithTranslator);
            }
            return producerWithTranslator;
        }
        producerWithTranslator = messageTypeMap.get(messageType).getTranslatorByPolling();
        return producerWithTranslator;
    }

    @PostConstruct
    public void init() {
        Map<String, EventDisruptor> eventDisruptorMap = applicationContext.getBeansOfType(EventDisruptor.class);
        log.info("-----------eventDisruptorMap-----------:{}",eventDisruptorMap);
        for (MessageType messageType : MessageType.values()) {
            Optional<Map.Entry<String, EventDisruptor>> curDisruptorEntry = eventDisruptorMap.entrySet().stream().filter(entry -> {
                EventDisruptor value = entry.getValue();
                EventDisruptor match = value.match(messageType);
                return match != null;
            }).findFirst();
            if(curDisruptorEntry.isPresent()){
                EventDisruptor matchDisruptor = curDisruptorEntry.get().getValue();
                DisruptorLoadBalance loadBalance = matchDisruptor.init();
                messageTypeMap.put(messageType, loadBalance);
            }

        }

    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
