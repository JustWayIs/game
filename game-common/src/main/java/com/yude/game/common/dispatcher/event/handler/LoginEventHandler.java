package com.yude.game.common.dispatcher.event.handler;

import com.baidu.bjf.remoting.protobuf.Any;
import com.lmax.disruptor.EventHandler;
import com.yude.game.common.dispatcher.HandlerMethod;
import com.yude.game.common.dispatcher.RequestMappingInfo;
import com.yude.game.common.dispatcher.event.MessageReceiveEvent;
import com.yude.game.common.manager.IPushManager;
import com.yude.game.common.manager.impl.SessionManager;
import com.yude.game.communication.tcp.server.ServerChannelHandlerInitializer;
import com.yude.game.communication.tcp.server.session.ISessionManager;
import com.yude.game.exception.BizException;
import com.yude.game.exception.SystemException;
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

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @Author: HH
 * @Date: 2020/6/23 14:25
 * @Version: 1.0
 * @Declare: 这个handler也可以不需要，改下MessageReceiveEventHandler就行了（根据事件类型）
 */
@Component("loginEventHandler")
public class LoginEventHandler implements EventHandler<MessageReceiveEvent> ,CommonActionEventHandler{
    private static final Logger log = LoggerFactory.getLogger(LoginEventHandler.class);

    /*@Autowired
    private RequestMappingInfo requestMappingInfo;*/

    @Autowired
    private IPushManager pushManager;

    @Autowired
    private ISessionManager sessionManager;
    

    @Override
    public void onEvent(MessageReceiveEvent event, long sequence, boolean endOfBatch) {
        ChannelHandlerContext context = event.getContext();
        try {
            log.debug("接收到登录事件：{}",event);
            GameRequestMessage message = event.getMessage();
            GameRequestMessageHead messageHead = message.getHead();
            Integer cmd = messageHead.getCmd();
            HandlerMethod handlerMethod = RequestMappingInfo.getHandlerMethodByCmd(cmd);
            Method method = handlerMethod.getMethod();
            Object instance = handlerMethod.getInstance();
            Class<? extends Request>[] paramTypes = handlerMethod.getParamTypes();
            Object result;
            if (paramTypes.length > 0) {
                Class<? extends Request> requestType = paramTypes[0];

                Request request = message.getObject().unpack(requestType);
                //直接加context
                result = method.invoke(instance, request,context);
            } else {
                result = method.invoke(instance);
            }
            if (result instanceof Response) {
                log.debug("请求直接响应：{}",result);
                Response response = (Response) result;
                //如果成功：
               if (response.isSuccess()) {

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
                //log.debug("响应数据：head:{}  response:{}",responseMessageHead,response);
                context.writeAndFlush(responseMessage);

            }


        } catch (BizException e) {
            log.error("业务处理异常：", e);
            buildAndPushCustomException(event,e);
            context.channel().close();
        } catch (SystemException e) {
            log.error("服务器异常",e);
            buildAndPushCustomException(event,e);
            context.channel().close();
        } catch (Exception e) {
            log.error("登录业务处理异常: {}",event, e);
            context.channel().close();
        } catch (Throwable e) {
            log.error("系统异常: {}",event, e);
            context.channel().close();
        }
    }

    @Override
    public IPushManager getPushManager() {
        return pushManager;
    }
}
