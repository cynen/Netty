package com.cynen.uchat.dto;

import com.cynen.uchat.pojo.TbChatRecord;

import java.io.Serializable;

public class Message implements Serializable {
    private Integer type;
    private TbChatRecord chatRecord;
    private String ext;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public TbChatRecord getChatRecord() {
        return chatRecord;
    }

    public void setChatRecord(TbChatRecord chatRecord) {
        this.chatRecord = chatRecord;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }
}
