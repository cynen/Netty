package com.cynen.uchat.controller;


import com.cynen.uchat.dto.Result;
import com.cynen.uchat.pojo.TbChatRecord;
import com.cynen.uchat.service.ChatRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/chatrecord")
public class ChatRecordController {

    @Autowired
    private ChatRecordService chatRecordService;

    /**
     * 根据用户id和好友id查询聊天记录.
     * @param userid
     * @param friendid
     * @return
     */
    @RequestMapping("/findByUserIdAndFriendId")
    public List<TbChatRecord> findByUserIdAndFriendId(String userid, String friendid){
        try {
            return  chatRecordService.findByUserIdAndFriendId(userid, friendid);
        } catch (Exception e) {
            e.printStackTrace();
            return  new ArrayList<TbChatRecord>(); // 异常就返回为空.
        }
    }

    /**
     * 根据userid查询未读消息.
     * @param userid
     * @return
     */
    @RequestMapping("/findUnreadByUserid")
    public List<TbChatRecord> findUnreadByUserid(String userid){
        try {
            return chatRecordService.findUnreadByUserid(userid);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<TbChatRecord>();
        }
    }

}
