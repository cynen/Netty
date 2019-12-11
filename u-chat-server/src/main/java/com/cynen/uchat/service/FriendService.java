package com.cynen.uchat.service;

import com.cynen.uchat.dto.FriendReq;
import com.cynen.uchat.dto.User;
import com.cynen.uchat.pojo.TbFriendReq;

import java.util.List;

public interface FriendService {
    void sendRequest(TbFriendReq friendReq);

    List<FriendReq> findFriendReqByUserid(String userid);

    void handlerFriendReq(String reqid, int result);

    List<User> findFriendByUserid(String userid);
}
