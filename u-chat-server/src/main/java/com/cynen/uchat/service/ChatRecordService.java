package com.cynen.uchat.service;

import com.cynen.uchat.pojo.TbChatRecord;

import java.util.List;

public interface ChatRecordService {
    void saveRecord(TbChatRecord chatRecord);

    List<TbChatRecord> findByUserIdAndFriendId(String userid, String friendid);

    List<TbChatRecord> findUnreadByUserid(String userid);

    void updateReadSatus(String chatRecordId);
}
