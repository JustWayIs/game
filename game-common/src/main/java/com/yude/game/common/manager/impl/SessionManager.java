package com.yude.game.common.manager.impl;

import cn.hutool.core.util.IdUtil;
import com.yude.game.communication.tcp.server.session.ISessionManager;

import com.yude.game.communication.tcp.server.session.Session;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Attribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: HH
 * @Date: 2020/6/18 17:26
 * @Version: 1.0
 * @Declare:
 */
@Service
public class SessionManager implements ISessionManager {
    private static final Logger log = LoggerFactory.getLogger(SessionManager.class);


    /**
     * sessionId -> session
     */
    private Map<String, Session> sessionMap = new ConcurrentHashMap<>();
    /**
     * userId -> session
     */
    private Map<Long, String> userSessionMap = new ConcurrentHashMap<>();

    @Override
    public void removeSession(Long userId) {
        if (userId == null) {
            log.warn("会话删除 userId 不能为null");
            return;
        }
        String sessionId = userSessionMap.remove(userId);
        if (sessionId == null) {
            return;
        }
        Session session = sessionMap.get(sessionId);
        if(session != null){
            //能拿到sessioId的情况下这里应该是不为null的
            session.close();
        }
        sessionMap.remove(sessionId);
        log.info("会话已删除：userId={}  sessionId={}",userId,sessionId);
    }

    @Override
    public Session getSession(Long userId) {
        String sessionId = getSessionId(userId);
        if(sessionId == null){
            log.info("不存在该玩家的会话： userId={}",userId);
            return null;
        }
        Session session = sessionMap.get(sessionId);
        return session;
    }

    @Override
    public String getSessionId(Long userId) {
        return  userSessionMap.get(userId);
    }

    @Override
    public String addSession(Long userId, ChannelHandlerContext context) {
        log.info("添加session： userId={}",userId);
        String mapSessionId = userSessionMap.get(userId);
        if (mapSessionId != null) {
            log.info("session已存在  sessionId:{}",mapSessionId);
            String channelSessionId = context.channel().attr(SESSION_ID).get();
            if (mapSessionId == channelSessionId) {
                return mapSessionId;
            }
            //客户端进行了重连：channel产生了变化
            log.info("客户端进行重连： userId={} oldSessionId={}",userId,mapSessionId);
            Session session = sessionMap.get(mapSessionId);
            //关闭老的channel
            session.close();
            sessionMap.remove(mapSessionId);
        }
        Session session = new Session();

        //生成的是不带-的字符串，类似于：b17f24ff026d40949c85a24f4f375d42
        //String simpleUUID = IdUtil.simpleUUID();


        //生成的UUID是带-的字符串，类似于：a5c8a5e8-df2b-4706-bea4-08d0939410e3
        String sessionId = IdUtil.randomUUID();
        log.info("添加session  sessionId={}  userId={}",sessionId,userId);
        session.setSessionId(sessionId);
        session.setContext(context);
        sessionMap.put(sessionId, session);
        userSessionMap.put(userId, sessionId);

        //其意义在于如果能拿到当前channel就可以直接获得userId,sessionId
        Attribute<Long> attr = context.channel().attr(USER_ID);
        attr.set(userId);
        Attribute<String> sessionAttr = context.channel().attr(SESSION_ID);
        sessionAttr.set(sessionId);


        return sessionId;
    }


}
