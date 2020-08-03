package com.yude.game.common.model;


import com.yude.game.common.constant.PlayerStatusEnum;
import com.yude.game.common.manager.IRoomManager;
import com.yude.game.common.timeout.TimeoutTaskPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: HH
 * @Date: 2020/6/17 17:00
 * @Version: 1.0
 * @Declare: 注意，房间内的任何操作都没有保证线程安全
 */
public abstract class AbstractRoomModel<T extends AbstractGameZoneModel, R extends AbstractSeatModel,TTP extends TimeoutTaskPool>{
    protected  TTP timeoutTaskPool;

    protected T gameZone;

    protected Long roomId;



    protected IRoomManager roomManager;

    /**
     * 用于标识打几轮，一轮打几局：似乎需要用减法来标识是否打完
     */
    protected int roundLimit;
    protected int inningLimit;
    /**
     * userId -> posId  玩家的位置信息应该来源于userId,而userId应该来源于登录验证后，存在channel里的值，不应该依赖客户端传上来的值，要不然客户端就可以伪造位置信息：机智的一批
     */
    protected Map<Long, Integer> userPosIdMap;
    /**
     * posId -> seat  【组合关系】
     */
    protected Map<Integer, R> posIdSeatMap;

    public void init(IRoomManager roomManager, Long roomId, List<Player> playerList,int roundLimit,int inningLimit) {
        this.roundLimit = roundLimit;
        this.inningLimit = inningLimit;
        this.roomId = roomId;
        this.roomManager = roomManager;
        userPosIdMap = new HashMap<>(playerList.size());
        posIdSeatMap = new HashMap<>(playerList.size());
        int i = 0;
        playerList.stream().forEach(player -> {
            roomManager.changePlayerStatus(player,PlayerStatusEnum.GAMEMING);

            int posId = posIdSeatMap.size();
            R baseSeat = getPracticalSeatModle(player, posId);
            baseSeat.setPlayer(player);
            baseSeat.setPosId(posId);

            posIdSeatMap.put(posId, baseSeat);
            userPosIdMap.put(player.getUserId(), posId);
        });

        startGame();
    }



    public int getPosId(Long userId) {
        return userPosIdMap.get(userId);
    }



    public abstract R getPracticalSeatModle(Player player, int posId);

    public abstract T getPracticalGameZoneModel();

    public abstract void startGame();
}
