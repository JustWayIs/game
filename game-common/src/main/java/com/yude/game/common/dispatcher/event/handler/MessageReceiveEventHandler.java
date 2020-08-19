package com.yude.game.common.dispatcher.event.handler;

import com.baidu.bjf.remoting.protobuf.Any;
import com.lmax.disruptor.EventHandler;
import com.yude.game.common.command.BaseCommandCode;
import com.yude.game.common.dispatcher.HandlerMethod;
import com.yude.game.common.dispatcher.RequestMappingInfo;
import com.yude.game.common.dispatcher.event.MessageReceiveEvent;
import com.yude.game.common.manager.IPushManager;
import com.yude.game.common.manager.impl.PushManager;
import com.yude.game.common.manager.impl.SessionManager;
import com.yude.game.communication.tcp.server.ServerChannelHandlerInitializer;
import com.yude.game.exception.BizException;
import com.yude.game.exception.SystemException;
import com.yude.protocol.common.constant.StatusCodeEnum;
import com.yude.protocol.common.message.GameRequestMessage;
import com.yude.protocol.common.message.GameRequestMessageHead;
import com.yude.protocol.common.message.GameResponseMessage;
import com.yude.protocol.common.message.GameResponseMessageHead;
import com.yude.protocol.common.request.Request;
import com.yude.protocol.common.response.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;


/**
 * @Author: HH
 * @Date: 2020/6/17 16:19
 * @Version: 1.0
 * @Declare:
 */
@Component("messageReceiveEventHandler")
public class MessageReceiveEventHandler implements EventHandler<MessageReceiveEvent>,CommonActionEventHandler {
    private static final Logger log = LoggerFactory.getLogger(MessageReceiveEventHandler.class);

   /* @Autowired
    private RequestMappingInfo requestMappingInfo;*/

    @Autowired
    private IPushManager pushManager;

    @Override
    public void onEvent(MessageReceiveEvent event, long sequence, boolean endOfBatch) {
        try {
            log.debug("接收到业务事件：{}",event);
            ChannelHandlerContext context = event.getContext();
            GameRequestMessage message = event.getMessage();
            GameRequestMessageHead messageHead = message.getHead();
            Integer cmd = messageHead.getCmd();
            HandlerMethod handlerMethod = RequestMappingInfo.getHandlerMethodByCmd(cmd);
            if (handlerMethod == null) {
                log.warn("不存在相应接口： cmd={}", Integer.toHexString(cmd));
                GameResponseMessage gameResponseMessage = PushManager.buildCommonResponse(cmd, StatusCodeEnum.FAIL);
                context.writeAndFlush(gameResponseMessage);
                return;
            }
            Method method = handlerMethod.getMethod();
            Object instance = handlerMethod.getInstance();
            Class<? extends Request>[] paramTypes = handlerMethod.getParamTypes();
            Object result;
            Request request = null;
            int length = paramTypes.length;
            // 考虑无参的情况
            if (length > 0) {
                Class<? extends Request> requestType = paramTypes[0];
                 request = message.getObject().unpack(requestType);
                //手动从channel里面取出userId设置到request里面 但是如果是超时操作，context是null
                if (request.getUserIdByChannel() == null) {
                    request.setUserIdByChannel(context.channel().attr(SessionManager.USER_ID).get());
                }
                if(length == 1){
                    result = method.invoke(instance, request);
                }else {
                    //先简单处理，第二个参数只能是：ChannelHandlerContext
                    result = method.invoke(instance, request,context);
                }
            } else {
                result = method.invoke(instance);
            }

            if (result instanceof Response) {
                log.debug("请求直接响应：{}",result);
                Response response = (Response) result;

                if (cmd.equals(BaseCommandCode.LOGIN) && response.isSuccess()) {

                    log.info("用户验证成功，添加超时handler : userId={}",context.channel().attr(SessionManager.USER_ID).get());
                    //心跳机制应该在登录校验完成后才有意义 -> 在这里加的话，得加到管道头部，否则不生效
                    context.pipeline().addFirst("read-time-out",new IdleStateHandler(0,0, ServerChannelHandlerInitializer.TIME_OUT * 3, TimeUnit.SECONDS));
                    log.debug("------------pipeline: {}  ",context.pipeline());
                }

                GameResponseMessage responseMessage = new GameResponseMessage();
                GameResponseMessageHead responseMessageHead = new GameResponseMessageHead();
                responseMessageHead.setCmd(cmd);
                responseMessage.setHead(responseMessageHead);
                Any any = Any.pack(response);
                responseMessage.setAny(any);
                //PushManager.push.... 异步线程推送
                //context.write 用同一个线程推送：注意这里用的是ChannelHandlerContext.会向前找出站handler。所以要额外注意 handler 在pipeline 中的顺序
                if(context != null){
                    context.writeAndFlush(responseMessage);
                    return;
                }
                if(request != null){
                    log.info("超时推送： command={}",Integer.toHexString(cmd));
                    pushManager.pushToUser(cmd,request.getUserIdByChannel(),response);
                    return;
                }



            }

        }catch (InvocationTargetException e){
            Throwable targetException = e.getTargetException();
            /**
             * 标注注释：业务处理过程中，如果出现异常可以手动抛出两种自定义异常，会在这里统一对客户端进行响应
             */
            if(targetException instanceof BizException){
                log.warn("业务处理异常：{}",event, targetException);
                try {
                    buildAndPushCustomException(event,(BizException)targetException);
                } catch (Exception ex) {
                    log.info("可能是是超时任务导致的 ",e);
                }
                return;
            }
            if(targetException instanceof SystemException){
                log.error("系统异常：{}",event, targetException);
                try {
                    buildAndPushCustomException(event,(SystemException)targetException);
                } catch (Exception ex) {
                    log.info("可能是是超时任务导致的 ",e);
                }
                return;
            }
            log.error("意料之外的反射异常：",e);
        }catch (Exception e) {
            log.error("业务异常: {}",event, e);
        } catch (Throwable e) {
            log.error("系统异常: {}",event, e);
        }


    }

    @Override
    public IPushManager getPushManager() {
        return pushManager;
    }
}
