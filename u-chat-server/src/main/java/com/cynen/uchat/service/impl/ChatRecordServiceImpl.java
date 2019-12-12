package com.cynen.uchat.service.impl;

import com.cynen.uchat.mapper.TbChatRecordMapper;
import com.cynen.uchat.pojo.TbChatRecord;
import com.cynen.uchat.pojo.TbChatRecordExample;
import com.cynen.uchat.service.ChatRecordService;
import com.cynen.uchat.utils.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ChatRecordServiceImpl implements ChatRecordService {

    @Autowired
    private TbChatRecordMapper chatRecordMapper;

    @Autowired
    private IdWorker idWorker;

    /**
     * 聊天记录的加载.
     * 加载完成后,应该将对应的消息标记为已读.
     * @param userid
     * @param friendid
     * @return
     */
    @Override
    public List<TbChatRecord> findByUserIdAndFriendId(String userid, String friendid) {
        // 1. 查询出聊天记录
        //2 .将friendid发送给userid的聊天记录全部更新为已读.
        System.out.println("查询 : "+userid +" 与 " + friendid +" 的聊天记录");
        TbChatRecordExample example = new TbChatRecordExample();
        TbChatRecordExample.Criteria criteria = example.createCriteria();
        TbChatRecordExample.Criteria criteria2 = example.createCriteria();
        // 查询出 user --> friend
        criteria.andHasDeleteEqualTo(0); //未删除的消息
        criteria.andUseridEqualTo(userid);
        criteria.andFriendidEqualTo(friendid);

        // 查询出 friend -- > user
        criteria2.andHasDeleteEqualTo(0); //未删除的消息
        criteria2.andUseridEqualTo(friendid);
        criteria2.andFriendidEqualTo(userid);

        // 都查询出来.
        example.or(criteria);
        example.or(criteria2);

        // 更新所有的发送给我的未读为已读.
        TbChatRecordExample exampleQuery = new TbChatRecordExample();
        TbChatRecordExample.Criteria queryCriteria = exampleQuery.createCriteria();
        queryCriteria.andHasReadEqualTo(0);
        queryCriteria.andUseridEqualTo(friendid); // 查询发送消息为好友.
        queryCriteria.andFriendidEqualTo(userid); // 查询接受消息为当前用户.
        List<TbChatRecord> records =  chatRecordMapper.selectByExample(exampleQuery);
        for (TbChatRecord record:records){
            record.setHasRead(1);
            chatRecordMapper.updateByPrimaryKey(record);
        }
        return chatRecordMapper.selectByExample(example);
    }

    /**
     * 打开app时,查询所有未读消息.
     * @param userid
     * @return
     */
    @Override
    public List<TbChatRecord> findUnreadByUserid(String userid) {
        TbChatRecordExample example = new TbChatRecordExample();
        TbChatRecordExample.Criteria criteria = example.createCriteria();
        criteria.andHasReadEqualTo(0); // 未读的消息
        criteria.andHasDeleteEqualTo(0);// 未删除的消息.
        criteria.andFriendidEqualTo(userid); // 别人发送给我的消息.
        return chatRecordMapper.selectByExample(example);
    }

    /**
     * 保存聊天记录.
     * @param chatRecord
     */
    @Override
    public void saveRecord(TbChatRecord chatRecord) {
        chatRecord.setId(idWorker.nextId());
        chatRecord.setCreatetime(new Date());
        chatRecord.setHasRead(0); // 0表示未读,1表示已读
        chatRecord.setHasDelete(0); // 0 表示未删, 1 表示已删除[逻辑删除]
        chatRecordMapper.insert(chatRecord);
    }

    /**
     * 根据消息id,更新消息为已读状态.
     * @param chatRecordId
     */
    @Override
    public void updateReadSatus(String chatRecordId) {
        System.out.println("准备更新消息已读状态的id:"+chatRecordId);
        TbChatRecord chatRecord = chatRecordMapper.selectByPrimaryKey(chatRecordId);
        chatRecord.setHasRead(1);
        System.out.println("查询后的消息体:" + chatRecord.getId());
        chatRecordMapper.updateByPrimaryKey(chatRecord);
    }
}
