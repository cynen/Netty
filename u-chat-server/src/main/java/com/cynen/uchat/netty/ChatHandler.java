package com.cynen.uchat.netty;

import com.alibaba.fastjson.JSON;
import com.cynen.uchat.dto.Message;
import com.cynen.uchat.pojo.TbChatRecord;
import com.cynen.uchat.service.ChatRecordService;
import com.cynen.uchat.utils.SpringUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;

/**
 * 处理消息的handler
 * TextWebSocketFrame: 在netty中，是用于为websocket专门处理文本的对象，frame是消息的载体
 */
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    // 用户记录和管理所有客户端的Channel
    private  static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:MM:ss");

    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        // 获取从客户端传输过来的消息.
        String text = msg.text();
        // System.out.println("接受到的消息: "+text);
        // 通过SpringUtils获取指定的Bean
        ChatRecordService chatRecordService = SpringUtil.getBean(ChatRecordService.class);
        // 将接受到的消息发送到所有的客户端.
       // 判断消息类型,根据不同类型执行不同处理.
        Message message = JSON.parseObject(text, Message.class);
        Integer type = message.getType();
        // System.out.println("消息类型为: " + type);
        switch (type){
            case 0:
                // 初始化链接时,将当前userid 纳入到userChannelMap管理.
                String userid = message.getChatRecord().getUserid();
                // 添加到userchannelmap进行管理.
                UserChanelMap.put(userid,ctx.channel()); // 添加到集合中.
                UserChanelMap.printAll();
                break;
            case 1:
                // 聊天记录,保存到数据库中,并标记为 未签收.
                TbChatRecord chatRecord = message.getChatRecord();
                chatRecordService.saveRecord(chatRecord);
                // 发送消息.
                // 获得接受消息的channel
                Channel channel = UserChanelMap.get(chatRecord.getFriendid());
                if (channel != null){
                    // 用户在线.
                    channel.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(message)));
                }else {
                    System.out.println("用户不在线!");
                }
                break;
            case 2:
                break;
            case 3:
                break;
        }

        /*for (Channel channel: clients){
            // 所有的Websocket数据都应该以TextWebSocketFrame 进行封装.
            // 此消息是直接写回客户端的.
            channel.writeAndFlush(new TextWebSocketFrame("[服务器接受到消息:]" + sdf.format(new Date()) + ",消息为:" + text));
        }*/
    }


    /**
     * 当客户端链接到服务器之后(打开链接.)
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        // System.out.println("客户端已连接,链接的客户端长ID:" + ctx.channel().id().asLongText());
        // 将Channel 添加到客户端
        clients.add(ctx.channel());
    }

    /**
     * 客户端断开连接时,触发
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        // 当触发handlerRemoved，ChannelGroup会自动移除对应客户端的channel
        UserChanelMap.removeChannelByChannelId(ctx.channel().id().asLongText());
    }

    /**
     * 客户端异常时触发.
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 从map中移除对应的通道.
        UserChanelMap.removeChannelByChannelId(ctx.channel().id().asLongText());
        // 服务端关闭通道.
        ctx.channel().close();
    }
}
