package com.cynen.nettydemo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class WebSocketNettyServer {

    public static void main(String[] args) {

        // 初始化主线程池.
        NioEventLoopGroup mainGroup = new NioEventLoopGroup();
        // 初始化从线程池
        NioEventLoopGroup subGroup = new NioEventLoopGroup();
        // 创建服务器启动器.
        ServerBootstrap bootstrap = new ServerBootstrap();
        try {
            //
            bootstrap.group(mainGroup,subGroup)
            // 指定使用NIO通道类型
            .channel(NioServerSocketChannel.class)
            // 指定通道初始化器加载通道处理器.
                    // 自定义实现.只需要继承对应的类即可.
            .childHandler(new WsServerInitializer());

            // 绑定端口号,启动服务器,并等待服务器启动
            // ChannelFuture 是Netty的回调消息.
            ChannelFuture future = bootstrap.bind(9090).sync();
            // 等待服务器socket关闭.
            future.channel().closeFuture().sync();
        } catch (InterruptedException e){
            // 回调可能出现异常.一般是直接抛出.这里做抓捕.
            System.out.println("抛异常:" + e.getLocalizedMessage());
        } finally {
            // 优雅的关闭主从线程池.
            mainGroup.shutdownGracefully();
            subGroup.shutdownGracefully();
        }

    }

}
