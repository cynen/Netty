package com.cynen.uchat.dto;

/**
 * 好友请求视图.
 */
public class FriendReq {
    // 好友请求id号.
    private String id;
    // 请求用户的picSmall
    private String picSmall;
    private String picNormal;
    private String nickname;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPicSmall() {
        return picSmall;
    }

    public void setPicSmall(String picSmall) {
        this.picSmall = picSmall;
    }

    public String getPicNormal() {
        return picNormal;
    }

    public void setPicNormal(String picNormal) {
        this.picNormal = picNormal;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
