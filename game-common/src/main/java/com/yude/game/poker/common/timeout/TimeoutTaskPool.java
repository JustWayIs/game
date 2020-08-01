package com.yude.game.poker.common.timeout;

/**
 * @Author: HH
 * @Date: 2020/7/14 11:25
 * @Version: 1.0
 * @Declare:
 */
public interface TimeoutTaskPool<T extends TimeoutTask> {

    /**
     * 增加任务到延时队列:服务器每个操作完成后
     * @param t
     */
    void addTask(T t);


}
