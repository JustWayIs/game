package com.yude.game.common.dispatcher.event.handler;

import com.yude.game.common.dispatcher.event.MessageReceiveEvent;
import com.yude.game.common.manager.IPushManager;
import com.yude.game.common.manager.impl.SessionManager;
import com.yude.game.exception.AbstractBaseException;
import com.yude.protocol.common.response.CommonResponse;
import io.netty.channel.ChannelHandlerContext;

import java.util.Optional;

/**
 * @Author: HH
 * @Date: 2020/7/1 15:01
 * @Version: 1.0
 * @Declare:
 */
public interface CommonActionEventHandler {

    default int buildAndPushCustomException(MessageReceiveEvent event, AbstractBaseException e){
        Integer cmd = Optional.ofNullable(event).flatMap(messageReceiveEvent -> Optional.ofNullable(messageReceiveEvent.getMessage())).flatMap(message -> Optional.ofNullable(message.getHead())).map(head -> head.getCmd()).orElse(0);
        ChannelHandlerContext context = event.getContext();
        if(context != null){
            //超时任务为null
            Long userId = context.channel().attr(SessionManager.USER_ID).get();
            getPushManager().pushToUser(cmd,userId,new CommonResponse(e.getErrorCode()));
        }

        return cmd;
    }

    IPushManager getPushManager();
}
