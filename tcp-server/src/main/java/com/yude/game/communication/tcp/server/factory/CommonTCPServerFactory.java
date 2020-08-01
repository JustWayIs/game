package com.yude.game.communication.tcp.server.factory;

import com.yude.game.communication.tcp.server.CommonTCPServer;
import io.netty.channel.ChannelInitializer;

public class CommonTCPServerFactory {
    public static CommonTCPServer getDefaultCommonTCPServerInstance(int port, ChannelInitializer serverChannelHandlerInitializer){
        return new CommonTCPServer(port,serverChannelHandlerInitializer);
    }

    public static CommonTCPServer getCustomsThreadNumCommonTCPServerInstance(int bossThreadNum,int workThreadNum,int port,ChannelInitializer serverChannelHandlerInitializer){

        return new CommonTCPServer(bossThreadNum,workThreadNum,port,serverChannelHandlerInitializer);
    }
}
