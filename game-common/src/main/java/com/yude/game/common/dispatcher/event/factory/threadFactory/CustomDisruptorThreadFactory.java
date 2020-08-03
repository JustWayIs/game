package com.yude.game.common.dispatcher.event.factory.threadFactory;

import com.yude.protocol.common.MessageType;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: HH
 * @Date: 2020/6/22 21:45
 * @Version: 1.0
 * @Declare:
 */
public enum  CustomDisruptorThreadFactory implements ThreadFactory {
    /**
     * 用于游戏业务的disruptor线程工厂，后面可以进行细粒度拆分
     */
    SERVICE{
        private  final AtomicInteger THREAD_ID = new AtomicInteger(0);
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setName("Disruptor-Event-Thread-" + MessageType.SERVICE.name() + "-" + THREAD_ID.getAndIncrement());
            thread.setDaemon(true);
            return thread;
        }
    },
    /**
     * 用于登录/鉴权的disruptor线程工厂
     */
    LOGIN{
        private  final AtomicInteger THREAD_ID = new AtomicInteger(0);
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setName("Disruptor-Event-Thread-" + MessageType.LOGIN.name() + "-" + THREAD_ID.getAndIncrement());
            thread.setDaemon(true);
            return thread;
        }
    },
    /**
     * 用于推送
     */
    PUSH_MESSAGE{
        private  final AtomicInteger THREAD_ID = new AtomicInteger(0);
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setName("Disruptor-Event-Thread-" + MessageType.PUSH_MESSAGE.name() + "-" + THREAD_ID.getAndIncrement());
            thread.setDaemon(true);
            return thread;
        }
    }
}
