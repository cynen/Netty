package com.cynen.nettydemo;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class WsServerInitializer extends ChannelInitializer {

    protected void initChannel(Channel channel) throws Exception {

        ChannelPipeline pipeline = channel.pipeline();
        // 用于支持http协议.

        // websocket基于http协议,需要有http的编解码器.
        pipeline.addLast(new HttpServerCodec());
        // 对写大数据流的支持.
        pipeline.addLast(new ChunkedWriteHandler());
        // 添加对 HTTP 请求和响应的聚合器:只要使用 Netty 进行 Http 编程都需要使用
        //// 对 HttpMessage 进行聚合，聚合成 FullHttpRequest 或者 FullHttpResponse
        // 在Netty编程中都使用到handler
        pipeline.addLast(new HttpObjectAggregator(1024 * 64));

        // 支持 websocket
        // websocket 服务器处理的协议，用于指定给客户端连接访问的路由: /ws
        // 本 handler 会帮你处理一些握手动作: handshaking(close, ping, pong) ping + pong = 心跳
        // 对于 websocket 来讲，都是以 frames 进行传输的，不同的数据类型对应的 frames 也不 同
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));

        // 添加自定义的handler,前面一大堆都是系统已经有的,我们只需要添加即可.
        // 一下才是重点.
        pipeline.addLast(new ChatHandler());
    }
}
