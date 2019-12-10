package com.cynen.nettydemo;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.Date;

public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    // 用户记录和管理所有客户端的Channel
    private  static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        // 获取从客户端传输过来的消息.
        String text = msg.text();
        System.out.println("接受到的消息: "+text);
        // 将接受到的消息发送到所有的客户端.
        // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Channel channel: clients){
            // 所有的Websocket数据都应该以TextWebSocketFrame 进行封装.
            // 此消息是直接写回客户端的.
            channel.writeAndFlush(new TextWebSocketFrame("[服务器接受到消息:]" + new Date().toLocaleString() + ",消息为:" + text));
        }
    }


    /**
     * 当客户端链接到服务器之后(打开链接.)
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端已连接,链接的客户端长ID:" + ctx.channel().id().asLongText());
        // 将Channel 添加到客户端
        clients.add(ctx.channel());
    }

    /**
     * 客户端断开连接,触发
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端断开,channel对应的长连接ID为:" + ctx.channel().id().asLongText());
        System.out.println("客户端断开,channel对应的短连接ID为:" + ctx.channel().id().asShortText());
    }
}
