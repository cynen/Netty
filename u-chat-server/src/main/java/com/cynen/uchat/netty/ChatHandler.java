package com.cynen.uchat.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 处理消息的handler
 * TextWebSocketFrame: 在netty中，是用于为websocket专门处理文本的对象，frame是消息的载体
 */
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    // 用户记录和管理所有客户端的Channel
    private  static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        // 获取从客户端传输过来的消息.
        String text = msg.text();
        System.out.println("接受到的消息: "+text);
        // 将接受到的消息发送到所有的客户端.
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:MM:ss");
        for (Channel channel: clients){
            // 所有的Websocket数据都应该以TextWebSocketFrame 进行封装.
            // 此消息是直接写回客户端的.
            channel.writeAndFlush(new TextWebSocketFrame("[服务器接受到消息:]" + sdf.format(new Date()) + ",消息为:" + text));
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
        // 当触发handlerRemoved，ChannelGroup会自动移除对应客户端的channel
        clients.remove(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("客户端链接异常,channel对应的长连接ID为:" + ctx.channel().id().asLongText());
    }
}
