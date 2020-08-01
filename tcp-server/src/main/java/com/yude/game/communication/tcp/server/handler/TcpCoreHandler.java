package com.yude.game.communication.tcp.server.handler;


import com.yude.game.communication.dispatcher.IProducerWithTranslator;
import com.yude.protocol.common.MessageType;
import com.yude.protocol.common.message.GameRequestMessage;
import com.yude.protocol.common.message.GameRequestMessageHead;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: HH
 * @Date: 2020/6/17 15:57
 * @Version: 1.0
 * @Declare:
 */
@ChannelHandler.Sharable
@Component("tcpCoreHandler")
public class TcpCoreHandler extends ChannelInboundHandlerAdapter {
    private static final Logger log = LoggerFactory.getLogger(TcpCoreHandler.class);

    @Autowired
    private BaseHandler baseHandler;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            GameRequestMessage requestMessage = (GameRequestMessage) msg;
            GameRequestMessageHead messageHead = requestMessage.getHead();
            int cmd = messageHead.getCmd();
            MessageType messageType = baseHandler.getMessageTypeByCommand(cmd);

            log.info("Receive service message --> requestMessage = {}", requestMessage);
            IProducerWithTranslator eventPublisher = baseHandler.getEventPublisher(messageType, messageHead.getRoomId());
            log.info("使用的事件发布器： roomId={}  publisher={}",messageHead.getRoomId(),eventPublisher);
            eventPublisher.publish(requestMessage, ctx);

        } catch (Exception e) {
            log.error("业务请求分发异常:", e);
        } finally {
            ReferenceCountUtil.release(msg);
        }
        //否则往下传递
        ctx.fireChannelRead(msg);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }
}
