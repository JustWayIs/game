package com.yude.game.communication.tcp.server.handler;


import com.yude.game.communication.dispatcher.IProducerWithTranslator;
import com.yude.game.communication.tcp.server.session.ISessionManager;
import com.yude.protocol.common.MessageType;
import com.yude.protocol.common.message.GameRequestMessage;
import com.yude.protocol.common.message.GameRequestMessageHead;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: HH
 * @Date: 2020/6/11 15:31
 * @Version 1.0
 * @Declare
 */
@ChannelHandler.Sharable
@Component("loginHandler")
public class LoginRespHandler extends ChannelInboundHandlerAdapter {
    private static final Logger log = LoggerFactory.getLogger(LoginRespHandler.class);

    @Autowired
    private BaseHandler baseHandler;

    @Autowired
    private ISessionManager sessionManager;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception{
        GameRequestMessage requestMessage = (GameRequestMessage) msg;
        GameRequestMessageHead messageHead = requestMessage.getHead();
        int cmd = messageHead.getCmd();
        MessageType messageType  = baseHandler.getMessageTypeByCommand(cmd);
        //如果是登录业务:不应该有客户端发过来的type，应该由方法本身决定
        if (MessageType.LOGIN.equals(messageType)) {
            log.info("Receive login message --> cmd = {}", Integer.toHexString(messageHead.getCmd()));

            try {
//                MessageType messageType = MessageType.matchMessageTypeInstance(type);

                IProducerWithTranslator eventPublisher = baseHandler.getEventPublisher(messageType, messageHead.getRoomId());
                eventPublisher.publish(requestMessage, ctx);

            } catch (Exception e) {
                log.error("登录请求分发异常:", e);
                //是否应该关闭channel,要看客户端是什么时候进行长连接
                //ctx.close();
            } finally {
                ReferenceCountUtil.release(msg);
            }

            return;
        }
        //否则往下传递
        ctx.fireChannelRead(msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {


        super.channelActive(ctx);

    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //服务器主动关闭触发,没有心跳机制的话，TCP服务器是不知道客户端断开了的。所以这个是在服务器的主动监测机制下被触发，并不是客户端一断开就被调用
        log.info("{} 断开了连接 删除session:{}", ctx.channel().remoteAddress(), ctx.channel().attr(ISessionManager.SESSION_ID).get());
        sessionManager.removeSession(ctx.channel().attr(ISessionManager.USER_ID).get());

        super.channelInactive(ctx);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        //在channelInactive之后调用
        super.handlerRemoved(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        //配合 IdleStateHandler使用
        log.trace("----userEventTriggered----");
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if (idleStateEvent.state() == IdleState.ALL_IDLE) {
                log.debug("-------心跳检测超时------session: {}", ctx.channel().attr(ISessionManager.SESSION_ID).get());
                ctx.close();
                sessionManager.removeSession(ctx.channel().attr(ISessionManager.USER_ID).get());
            }
            return;
        }
        //不是 IdleStateEvent 事件，所以将它传递给下一个 ChannelInboundHandler
        super.userEventTriggered(ctx, evt);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("发生了异常事件：", cause);//比如ReadTimeoutHandler就会抛出异常,并且自动关闭channel
        //H2 正常解析后可以考虑去掉，上面有finally
        // ctx.channel().close();
        //.....
        if (cause instanceof ReadTimeoutException) {
            sessionManager.removeSession(ctx.channel().attr(ISessionManager.USER_ID).get());
            return;
        }
        super.exceptionCaught(ctx, cause);


    }
}
