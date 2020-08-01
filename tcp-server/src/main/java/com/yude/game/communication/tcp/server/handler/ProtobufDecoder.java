package com.yude.game.communication.tcp.server.handler;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.yude.protocol.common.message.GameRequestMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @Author: HH
 * @Date: 2020/6/11 14:20
 * @Version 1.0
 * @Declare
 */

public class ProtobufDecoder  extends ByteToMessageDecoder {
    /**
     * 序列化的工具类
     */
    public static final Codec<GameRequestMessage> requestCodec = ProtobufProxy.create(GameRequestMessage.class);

    public static final Logger log = LoggerFactory.getLogger(ProtobufDecoder.class);

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        Codec<GameRequestMessage> codec = requestCodec;
        byte[] requestByte = new byte[in.readableBytes()];
        in.readBytes(requestByte);
        GameRequestMessage gameRequestMessage = codec.decode(requestByte);
        out.add(gameRequestMessage);
    }
}
