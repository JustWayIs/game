package com.yude.game.common.util;

import com.yude.game.exception.SystemException;

import java.util.LinkedList;

/**
 * @Author: HH
 * @Date: 2020/7/21 16:31
 * @Version: 1.0
 * @Declare:
 */
public enum TempSeatPool {
    /**
     * 几人游戏
     */
    TOW_PLAYER(2),
    THREE_PLAYER(3),
    FOUR_PLAYER(4),
    FIVE_PLAYER(5),
    SIX_PLAYER(6),
    SEVEN_PLAYER(7),
    EIGHT_PLAYER(8);

    public int playerNum;

    private volatile LinkedList<AtomicSeatDown> tempSeats = new LinkedList<>();


    TempSeatPool(int playerNum) {
        this.playerNum = playerNum;
        AtomicSeatDown last = new AtomicSeatDown(playerNum);
        tempSeats.addLast(last);
    }

    public static TempSeatPool matchPlayerInstance(int playerNum){
        for(TempSeatPool tempSeat : TempSeatPool.values()){
            if(tempSeat.playerNum == playerNum){
                return tempSeat;
            }
        }
        throw new SystemException("没有可以匹配对应人数的临时区: "+playerNum);
    }


    //创建房间从头开始拿，匹配从尾开始拿
    public synchronized AtomicSeatDown getAtomicSeatDownInstanceToMatch() {
        AtomicSeatDown last = tempSeats.getLast();
        if (last.canStartGame()) {
            AtomicSeatDown newLast = new AtomicSeatDown(this.playerNum);
            tempSeats.addLast(newLast);
            last = newLast;
        }
        return last;
    }

    public synchronized void removeTempSeat(AtomicSeatDown seatDown) {
        tempSeats.remove(seatDown);
        AtomicSeatDown newLast = new AtomicSeatDown(this.playerNum);
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
