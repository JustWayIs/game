package com.yude.game.communication.tcp.server.handler;

import com.baidu.bjf.remoting.protobuf.Any;
import com.yude.protocol.common.MessageType;
import com.yude.protocol.common.message.GameRequestMessage;
import com.yude.protocol.common.message.GameRequestMessageHead;
import com.yude.protocol.common.message.GameResponseMessage;
import com.yude.protocol.common.message.GameResponseMessageHead;
import com.yude.protocol.common.response.HeartBeatResponse;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Author: HH
 * @Date: 2020/6/11 15:15
 * @Version 1.0
 * @Declare
 */
@ChannelHandler.Sharable
@Component("heartBeathandler")
public class HeartBeatRespHandler extends ChannelInboundHandlerAdapter {
    private static final Logger log = LoggerFactory.getLogger(HeartBeatRespHandler.class);

    private static GameResponseMessage heartBeatMessage;

    @Autowired
    private BaseHandler baseHandler;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        GameRequestMessage requestMessage = (GameRequestMessage) msg;
        GameRequestMessageHead head = requestMessage.getHead();
        //心跳没有在Controller设置，所以不能用MessageType。如果需要做一定处理，还是需要Controller
        int cmd = head.getCmd();
        MessageType type = baseHandler.getMessageTypeByCommand(cmd);
        //if(head != null && head.getType() == MessageType.HEARTBEAT.value()){
        if(MessageType.HEARTBEAT.equals(type)){
            //log.debug("Receive ping message  --> ",head.getSessionId());
            if(heartBeatMessage == null){
                buildResponse(cmd,ctx);
            }
            ctx.writeAndFlush(heartBeatMessage);
            //log.debug("Send pong message to --> ",head.getSessionId());
            ReferenceCountUtil.release(msg);
            return;
        }
        ctx.fireChannelRead(msg);
    }

    private GameResponseMessage buildResponse(int cmd,ChannelHandlerContext context){
        heartBeatMessage = new GameResponseMessage();
        GameResponseMessageHead head = new GameResponseMessageHead();
        head.setCmd(cmd);
        //用单例的心跳响应，就绝对不能再设置sessionId了
        //head.setSessionId(context.channel().attr(ISessionManager.SESSION_ID).get());

        heartBeatMessage.setHead(head);
        HeartBeatResponse response = new HeartBeatResponse();
        try {
            Any any = Any.pack(response);
            heartBeatMessage.setAny(any);
        } catch (IOException e) {
            log.error("构建响应对象失败：",e);
        }
        return heartBeatMessage;
    }

}
