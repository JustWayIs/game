package com.yude.game.poker.common.timeout;

import com.yude.game.poker.common.manager.IPushManager;
import com.yude.game.poker.common.mode.AbstractRoomModel;

import java.util.concurrent.Delayed;

/**
 * @Author: HH
 * @Date: 2020/7/14 11:27
 * @Version: 1.0
 * @Declare: 超时任务框架中所有对象用到的所有属性都要用volatile修饰,不允许修改任何值
 */
public interface TimeoutTask<T extends AbstractRoomModel,P extends IPushManager> extends Runnable, Delayed {

    /**
     * 延时后需要执行的任务
     */
    void execute();

    long getRemaining();

    @Override
    default void run(){
        execute();
    };
}
