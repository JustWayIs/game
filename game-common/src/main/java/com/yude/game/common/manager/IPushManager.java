package com.yude.game.common.manager;

import com.yude.protocol.common.response.Response;

import java.util.List;

/**
 * @Author: HH
 * @Date: 2020/6/18 19:43
 * @Version: 1.0
 * @Declare:
 */
public interface IPushManager {

    void pushToUser(int command,Long userId, Response response,Long... roomId);

    void pushToUsers(int command,List<Long> userIds, Response response,Long... roomId);

    List<Long> excludeUser(List<Long> userIds,Long excludeUserId);

    boolean isOnline(Long userId);

}
