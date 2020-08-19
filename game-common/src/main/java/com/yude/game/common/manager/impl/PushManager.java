package com.yude.game.common.manager.impl;

import com.baidu.bjf.remoting.protobuf.Any;
import com.yude.game.common.dispatcher.event.DisruptorRegistrar;
import com.yude.game.exception.SystemException;
import com.yude.game.common.manager.IPushManager;
import com.yude.game.communication.dispatcher.IProducerWithTranslator;
import com.yude.game.communication.tcp.server.session.ISessionManager;
import com.yude.game.communication.tcp.server.session.Session;
import com.yude.protocol.common.MessageType;
import com.yude.protocol.common.constant.StatusCodeI;
import com.yude.protocol.common.message.GameResponseMessage;
import com.yude.protocol.common.message.GameResponseMessageHead;
import com.yude.protocol.common.response.CommonResponse;
import com.yude.protocol.common.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: HH
 * @Date: 2020/6/18 16:30
 * @Version: 1.0
 * @Declare:
 */
@Service
public class PushManager implements IPushManager {
    private static final Logger log = LoggerFactory.getLogger(PushManager.class);

    @Autowired
    private ISessionManager sessionManager;

   /* @Autowired
    private DisruptorRegistrar disruptorRegistrar;*/


    @Override
    public void pushToUser(Integer command,Long userId, Response response,Long... roomIdParam) {
        log.info("推送数据：roomId={} command={}  userId={}  response={}",roomIdParam,Integer.toHexString(command),userId,response);
        GameResponseMessage gameResponseMessage = new GameResponseMessage();
        GameResponseMessageHead head = new GameResponseMessageHead();
        Session session = sessionManager.getSession(userId);
        if(session == null){
            return;
        }
        head.setCmd(command);
        head.setSessionId(session.getSessionId());

        gameResponseMessage.setHead(head);
        try {
            Any any = Any.pack(response);
            gameResponseMessage.setAny(any);
        } catch (IOException e) {
            log.error("构建响应对象失败：{}",response,e);
            throw new SystemException("构建响应对象失败：",e);
        }

        Long roomId = null;
        if(roomIdParam.length > 0){
            roomId = roomIdParam[0];
        }
        //推送事件，没有按房间区分，所以roomId传null
        IProducerWithTranslator eventPublisher = DisruptorRegistrar.needEventPublisher(MessageType.PUSH_MESSAGE,roomId);
        try {
            if(!isOnline(userId)){
                log.info("该玩家不在线，不予推送： userId={}",userId);
                return;
            }
            eventPublisher.publish(gameResponseMessage,session.getContext());
        } catch (Exception e) {
            log.error("推送失败： command={}  userId={}  response={}",command,userId,response,e);
        }
        /*Session session = sessionManager.getSession(userId);
        session.writeAndFlushByContext(responseMessage);*/
    }

    @Override
    public void pushToUsers(Integer command,List<Long> userIds, Response response,Long... roomId) {
        for(Long userId : userIds){
            pushToUser(command,userId,response,roomId);
        }
    }

    @Override
    public List<Long> excludeUser(List<Long> userIds, Long excludeUserId) {
        List<Long> collect = userIds.stream().filter(userId -> !userId.equals(excludeUserId)).collect(Collectors.toList());
        return collect;
    }

    @Override
    public boolean isOnline(Long userId) {
        Session session = sessionManager.getSession(userId);
        if(session == null){
            return false;
        }
        return true;
    }

    public static GameResponseMessage buildCommonResponse(Integer cmd, StatusCodeI status) {
        GameResponseMessage responseMessage = new GameResponseMessage();
        GameResponseMessageHead responseMessageHead = new GameResponseMessageHead();
        responseMessageHead.setCmd(cmd);
        responseMessage.setHead(responseMessageHead);

        CommonResponse commonResponse = new CommonResponse(status);
        Any any = null;
        try {
            any = Any.pack(commonResponse);
        } catch (IOException e) {
            throw new SystemException("构建响应对象失败",e);
        }
        responseMessage.setAny(any);
        return responseMessage;
    }
}
