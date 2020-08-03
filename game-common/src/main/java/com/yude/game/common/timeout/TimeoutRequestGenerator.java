package com.yude.game.common.timeout;

import com.yude.game.common.constant.Status;
import com.yude.game.common.model.AbstractRoomModel;
import com.yude.protocol.common.request.Request;

import java.util.Optional;

/**
 * @Author: HH
 * @Date: 2020/7/14 17:02
 * @Version: 1.0
 * @Declare:
 */
public interface TimeoutRequestGenerator<T extends Request,K extends AbstractRoomModel,P extends Status> {
    /**
     * 根据游戏当前阶段构建不同的操作请求对象
     * @return
     */
    T build(K k) throws CloneNotSupportedException;

    /**
     * 用于匹配相同游戏状态的策略
     * @param gameStatusEnum
     * @return
     */
    Optional<? extends TimeoutRequestGenerator> match(P gameStatusEnum);

    int  getCmd();
}
