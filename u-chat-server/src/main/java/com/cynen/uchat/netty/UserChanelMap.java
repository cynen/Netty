package com.cynen.uchat.netty;

import io.netty.channel.Channel;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 *  用来保存 userid和channel的对应关系.
 *  1 对1 聊天对象.
 */
public class UserChanelMap {

    // 并发包的使用.
    // public static ConcurrentHashMap<String, Channel> userChannelMap = new ConcurrentHashMap();
    public static HashMap<String, Channel> userChannelMap = new HashMap();

    public static void put(String userid,Channel channel){
        userChannelMap.put(userid,channel);
    }

    public static Channel get(String userid){
        return userChannelMap.get(userid);
    }

    /**
     * 根据channelid移除.
     * @param channelLongId
     */
    public static void removeChannelByChannelId(String channelLongId){
        if(StringUtils.isBlank(channelLongId)){
            return;
        }
        for (String s: userChannelMap.keySet()){
            // 遍历所有的key,找到对应的channel移除.
            Channel channel = userChannelMap.get(s);
            if (channelLongId.equals(channel.id().asLongText())) {
                System.out.println("客户端断开连接,取消用户 "+s+"与通道"+channelLongId+"关联");
                userChannelMap.remove(s);
                break;
            }
        }
    }

    public static void printAll(){
        for (String uid : userChannelMap.keySet()){
            System.out.println("用户ID:" +uid+ ", 通道短ID:" + userChannelMap.get(uid).id().asShortText());
        }
    }
}
