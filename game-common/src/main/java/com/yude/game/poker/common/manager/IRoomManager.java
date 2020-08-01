package com.yude.game.poker.common.manager;

import com.yude.game.poker.common.constant.Status;
import com.yude.game.poker.common.mode.AbstractRoomModel;
import com.yude.game.poker.common.mode.seat.Player;

import java.util.concurrent.ExecutorService;

/**
 * @Author: HH
 * @Date: 2020/6/18 19:43
 * @Version: 1.0
 * @Declare:
 */
public interface IRoomManager {
    void match(Long userId);

    void init();

    void destroyRoom(Long roomId);

    AbstractRoomModel getRoomByRoomId(Long roomId);

    Long getRoomIdByUserId(Long userId);

    AbstractRoomModel getRoomByUserId(Long userId);

    IPushManager getPushManager();

    /**
     *
     * @param player
     */
    void changePlayerStatus(Player player, Status status);

    void restartMath(ExecutorService matchThreadPool);

}
