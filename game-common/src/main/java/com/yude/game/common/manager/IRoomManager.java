package com.yude.game.common.manager;

import com.yude.game.common.constant.Status;
import com.yude.game.common.model.AbstractRoomModel;
import com.yude.game.common.model.Player;
import com.yude.protocol.common.response.Response;

import java.util.concurrent.ExecutorService;

/**
 * @Author: HH
 * @Date: 2020/6/18 19:43
 * @Version: 1.0
 * @Declare:
 */
public interface IRoomManager<R extends AbstractRoomModel> {
    void match(Long userId);

    void init();

    void destroyRoom(Long roomId);

    R getRoomByRoomId(Long roomId);

    Long getRoomIdByUserId(Long userId);

    R getRoomByUserId(Long userId);

    IPushManager getPushManager();

    /**
     * getPushManager没有存在的必要性，应该对调用方屏蔽IPushManager的存在
     * @param command
     * @param userId
     * @param response
     * @param roomId
     */
    void pushToUser(Integer command, Long userId, Response response, Long roomId);

    /**
     *
     * @param player
     */
    void changePlayerStatus(Player player, Status status);

    void restartMath(ExecutorService matchThreadPool);

}
