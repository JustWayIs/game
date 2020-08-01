package com.yude.game.communication.tcp.server.session;


import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

/**
 * @Author: HH
 * @Date: 2020/6/18 19:43
 * @Version: 1.0
 * @Declare:
 */
public interface ISessionManager {
    AttributeKey<Long> USER_ID = AttributeKey.valueOf("userId");
    AttributeKey<String> SESSION_ID = AttributeKey.valueOf("sessionId");

    void removeSession(Long userId);

    Session getSession(Long userId);

    String getSessionId(Long userId);

    String addSession(Long userId, ChannelHandlerContext context);
}
