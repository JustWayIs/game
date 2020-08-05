package com.yude.game.communication.tcp.server;


import com.yude.game.communication.tcp.server.handler.ProtobufDecoder;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.MessageToByteEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@ChannelHandler.Sharable
@Component
public class ServerChannelHandlerInitializer extends ChannelInitializer<SocketChannel> {
    /**
     * 这里用@Resource = @Qualifier + @Autowired : 因为没有单独为不同的handler类型抽象出不同的接口
     */
    @Resource(name="tcpCoreHandler")
    private ChannelInboundHandler tcpCoreHandler;
    @Resource(name="heartBeathandler")
    private ChannelInboundHandler heartBeathandler;
    @Resource(name="loginHandler")
    private ChannelInboundHandler loginHandler;

    @Autowired
    private MessageToByteEncoder messageToByteEncoder;


    /*@Autowired
    private ByteToMessageDecoder byteToMessageDecoder;*/

    public static final int TIME_OUT = 2;

    public ServerChannelHandlerInitializer(ChannelInboundHandler heartBeathandler,ChannelInboundHandler loginHandler,ChannelInboundHandler tcpCoreHandler) {
        this.heartBeathandler = heartBeathandler;
        this.loginHandler = loginHandler;
        this.tcpCoreHandler = tcpCoreHandler;
    }

    public ServerChannelHandlerInitializer() {
    }

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
        
        //TCP传输报文信息日志
        //pipeline.addLast("messageLog",new LoggingHandler(LogLevel.DEBUG));
        // proto - 解码器
        pipeline.addLast("frameDecoder",new LengthFieldBasedFrameDecoder(65535,0,2,0,2));

        //ByteToMessageDecoder不能是Sharable，即使用spring多例也不行，所以这里只能每次new出来
        pipeline.addLast("customMessageDcoder",new ProtobufDecoder());

        // proto - 编码器
        pipeline.addLast("frameEncoder",new LengthFieldPrepender(2));
        pipeline.addLast("customMessageEncoder",messageToByteEncoder);

        /**
         * 给一个网络波动时间：3次超时时间后再处理channel
         */
        //超时检测:会抛出异常，自动关闭channel
        //pipeline.addLast("readTimeOutHandler",new ReadTimeoutHandler(TIME_OUT * 3));
        //超时检测：不会抛出异常，会触发一个事件，需要手动关闭channel
        //pipeline.addLast("read-time-out",new IdleStateHandler(0,0,TIME_OUT * 3, TimeUnit.SECONDS));
        
        //登录业务handler  登录-鉴权 的应答
        pipeline.addLast("authUserHandler",loginHandler);
        
       //心跳应答
        pipeline.addLast(heartBeathandler);
        
        //服务端业务处理
        pipeline.addLast("serviceHandler",tcpCoreHandler);
    }
}
