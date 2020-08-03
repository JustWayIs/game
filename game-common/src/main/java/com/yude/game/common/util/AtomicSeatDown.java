package com.yude.game.common.util;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLongArray;

/**
 * @Author: HH
 * @Date: 2020/6/18 10:43
 * @Version: 1.0
 * @Declare:
 */
public class AtomicSeatDown {


    /**
     * 空闲位置索引
     */
    private  AtomicInteger currentIndex;
    private volatile AtomicLongArray zone;
    private final int playerNum;
    private  AtomicBoolean canStart;


    AtomicSeatDown(int playerNum) {
        this.playerNum = playerNum;
        currentIndex = new AtomicInteger(1);
        zone = new AtomicLongArray(playerNum);
        canStart = new AtomicBoolean(false);
    }

    /*public static AtomicSeatDown getInstance() {
        return INSTANCE_DDZ;
    }*/

    public boolean canStartGame() {
        return canStart.get();
    }

    public synchronized void resetZone() {
        currentIndex = new AtomicInteger(1);
        zone = new AtomicLongArray(playerNum);
        canStart = new AtomicBoolean(false);
    }

    public boolean trySeatDown(long userId) throws Exception {
        return execute(userId);
    }

    /**
     * 关于Atomic包的API补充：如果返回值是boolean类型，说明是非自旋的CAS操作。void的就....
     *
     * @param userId
     */
    public boolean seatDown(long userId) throws Exception {
        for (; ; ) {
            if (canStartGame()) {
                return false;
            }
            boolean isSuccess = execute(userId);
            if (isSuccess) {
                return true;
            }
        }
    }

    private boolean execute(long userId) throws Exception {

        int cur;
        boolean isSeatDown;
        synchronized (this) {
            cur = currentIndex.get();
            isSeatDown = zone.compareAndSet(cur - 1, 0, userId);
        }
        //因为对zone里面某一个位置的修改只有一个线程能够成功，所以接下来对currentIndex的修改一定会成功，因为只有一个线程修改
        /**
         * 当前位置如果被成功占用，修改空闲位置索引
         */
        if (isSeatDown) {
            //这两个逻辑里的cas操作最好保持一致，要么都用自旋，要么都不用
            if (cur < playerNum) {
                boolean isSuccess = currentIndex.compareAndSet(cur, cur + 1);
                if (!isSuccess) {
                    throw new Exception("意料之外的状况：空闲位置索引更新失败：未坐满");
                }
            } else {
                /*boolean isSuccess = currentIndex.compareAndSet(cur, 1);
                if(!isSuccess){
                    throw new Exception("意料之外的状况：空闲位置索引更新失：已坐满");
                }*/
                canStart.getAndSet(true);
            }
            /*if(seatPeopleNum == playerNum){
                    return true;
                }*/
            return true;
        }
        return false;

    }

    public AtomicLongArray getZone() {
        return zone;
    }


}
