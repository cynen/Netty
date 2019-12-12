package com.cynen.uchat.service.impl;

import com.cynen.uchat.mapper.TbChatRecordMapper;
import com.cynen.uchat.pojo.TbChatRecord;
import com.cynen.uchat.service.ChatRecordService;
import com.cynen.uchat.utils.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
public class ChatRecordServiceImpl implements ChatRecordService {

    @Autowired
    private TbChatRecordMapper chatRecordMapper;
    @Autowired
    private IdWorker idWorker;

    @Override
    public void saveRecord(TbChatRecord chatRecord) {
        chatRecord.setId(idWorker.nextId());
        chatRecord.setCreatetime(new Date());
        chatRecord.setHasRead(0); // 0表示未读,1表示已读
        chatRecord.setHasDelete(0); // 0 表示未删, 1 表示已删除[逻辑删除]
        chatRecordMapper.insert(chatRecord);
    }
}
