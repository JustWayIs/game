package com.yude.game.communication.tcp.server.handler;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.yude.protocol.common.message.GameResponseMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @Author: HH
 * @Date: 2020/6/11 14:12
 * @Version 1.0
 * @Declare
 */
@ChannelHandler.Sharable
@Component
public class ProtobufEncoder extends MessageToByteEncoder<GameResponseMessage> {

    // ===========================================================
    // Constants
    // ===========================================================
    public static final Codec<GameResponseMessage> responseCodec = ProtobufProxy.create(GameResponseMessage .class);

    private static final Logger log = LoggerFactory.getLogger(ProtobufEncoder.class);

    // ===========================================================
    // Fields
    // ===========================================================


    // ===========================================================
    // Constructors
    // ===========================================================


    // ===========================================================
    // Getter &amp; Setter
    // ===========================================================


    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================
    @Override
    protected void encode(ChannelHandlerContext ctx, GameResponseMessage msg, ByteBuf out) throws Exception {
        Codec<GameResponseMessage> codec = responseCodec;
        byte[] encode = codec.encode(msg);
        out.writeBytes(encode);
    }

    // ===========================================================
    // Methods
    // ===========================================================


    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}