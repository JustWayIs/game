package com.yude.game.common.model;

import com.yude.game.common.constant.Status;
import com.yude.game.exception.BizException;

/**
 * @Author: HH
 * @Date: 2020/6/17 17:01
 * @Version: 1.0
 * @Declare:
 */
public abstract class AbstractGameZoneModel<T extends AbstractSeatModel,S extends Status>{


    //聚合关系 : 游戏结束要解除这个关联关系
    protected T[] playerSeats;

    //因为一个房间可以存在多轮、多局游戏，所以还需要 具体游戏域的标识。倒序的第几轮第几局：实际上根本看不出来
    protected Integer zoneId;

    protected int round;

    protected int inning;

    protected volatile int stepCount;

    protected S gameStatus;

    public AbstractGameZoneModel(T[] playerSeats,int round,int inning) {
        this.playerSeats = playerSeats;
        this.round = round;
        this.inning = inning;
        String zoneIdStr = round + "" + inning;
        this.zoneId = Integer.valueOf(zoneIdStr);
    }

    /**
     * 【赛事玩法有要求同一轮不同房间获得的是同一组牌，所以把牌组打乱顺序之后，要以赛事轮次为key,把牌组存进分布式缓存中。游戏结束再持久化】
     */
    public abstract void init();

    public abstract void clean();

    public Integer getZoneId() {
        return zoneId;
    }

    public AbstractSeatModel getSeatByPosId(int posId){
        for(AbstractSeatModel seatModel : playerSeats){
            if(posId == seatModel.getPosId()){
                return seatModel;
            }
        }
        throw new BizException("没有获取到该位置的信息："+posId);
    }

    public int getRound() {
        return round;
    }

    public int getInning() {
        return inning;
    }

    public  int getStepCount(){
        return stepCount;
    }

    public S getGameStatus(){
        return gameStatus;
    }
}
