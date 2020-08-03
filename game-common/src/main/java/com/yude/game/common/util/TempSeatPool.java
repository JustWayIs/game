package com.yude.game.common.util;

import java.util.LinkedList;

/**
 * @Author: HH
 * @Date: 2020/7/21 16:31
 * @Version: 1.0
 * @Declare:
 */
public enum TempSeatPool {
    /**
     * 单例
     */
    INSTANCE;


    private volatile LinkedList<AtomicSeatDown> tempSeats = new LinkedList<>();

    {
        AtomicSeatDown last = new AtomicSeatDown(3);
        tempSeats.addLast(last);
    }

    public static TempSeatPool getInstance() {
        return INSTANCE;
    }

    //创建房间从头开始拿，匹配从尾开始拿
    public synchronized AtomicSeatDown getAtomicSeatDownInstanceToMatch() {
        AtomicSeatDown last = tempSeats.getLast();
        if (last.canStartGame()) {
            AtomicSeatDown newLast = new AtomicSeatDown(3);
            tempSeats.addLast(newLast);
            last = newLast;
        }
        return last;
    }

    public synchronized void removeTempSeat(AtomicSeatDown seatDown) {
        tempSeats.remove(seatDown);
        AtomicSeatDown newLast = new AtomicSeatDown(3);
        tempSeats.addLast(newLast);
    }

    /**
     * 如果第一个都不能开始，后面的一定也不能开始
     *
     * @return
     */
    public AtomicSeatDown getAtomicSeatDownToCreate() {
        AtomicSeatDown first = tempSeats.getFirst();
        if (first.canStartGame()) {
            tempSeats.removeFirst();
            if (tempSeats.size() == 0) {
                AtomicSeatDown last = new AtomicSeatDown(3);
                tempSeats.addLast(last);
            }

            return first;
        }
        return null;
    }

    public synchronized void resetAtomicSeatDown(AtomicSeatDown seatDown) {
        //重复利用
        seatDown.resetZone();
        tempSeats.addLast(seatDown);

    }
}
