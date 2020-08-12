package com.yude.game.communication.tcp.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

@Component
@PropertySource("classpath:config/core.properties")
public class CommonTCPServer implements Runnable{
    private Logger log = LoggerFactory.getLogger(CommonTCPServer.class);

    @Value("${netty.port}")
    private  int port;
    @Value("${netty.bossThreadNum:1}")
    private int bossThreadNum = 1;

    /**
     * 根据运行状态调整到最优。使用@Value意味着必须传值。CPU核心数又是不确定的，该不该把这个值交由properties还有待商榷
     * CPU逻辑核心数x2实际上是个默认值
     */
    private int workThreadNum = Runtime.getRuntime().availableProcessors();
    private ServerBootstrap serverBootstrap;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workGroup;

    @Autowired
    private ChannelInitializer serverChannelHandlerInitializer;

    //是否开启SSL标志位
    private boolean open_ssl = false;


    //后面补充进来的，本来打算使用静态工厂来创建服务器，现在使用spring
    public CommonTCPServer(){}

    public CommonTCPServer(int port, ChannelInitializer serverChannelHandlerInitializer) {
        this.port = port;
        this.serverChannelHandlerInitializer = serverChannelHandlerInitializer;
    }

    public CommonTCPServer(int bossThreadNum, int workThreadNum, int port, ChannelInitializer serverChannelHandlerInitializer) {
        this(port, serverChannelHandlerInitializer);
        this.bossThreadNum = bossThreadNum;
        this.workThreadNum = workThreadNum;
    }

    @Override
    public void run() {
        serverBootstrap = new ServerBootstrap();
        bossGroup = new NioEventLoopGroup(bossThreadNum);
        workGroup = new NioEventLoopGroup(workThreadNum);

        try {
            serverBootstrap
                    .group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    /**
                     * TCP选项：根据需求调整
                     */
                    //TCP的accept队列大小
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    //端口重用: 用于快速重启，也可能导致意料之外的多个进程占用一个端口
                    //.option(ChannelOption.SO_REUSEADDR, true)
                    //发送缓冲区
                    //.childOption(ChannelOption.SO_SNDBUF,1024)
                    //接收缓冲区
                    //.childOption(ChannelOption.SO_RCVBUF,1024)
                    //禁用nagle算法
                    //.childOption(ChannelOption.TCP_NODELAY,true)
                    .childHandler(serverChannelHandlerInitializer);
            ChannelFuture future = serverBootstrap.bind().sync();
            log.info("TCP服务器启动成功, 端口号：{}",port);
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("TCP服务器启动失败：", e);
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }


    }

}
