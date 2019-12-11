package com.cynen.uchat.controller;


import com.cynen.uchat.dto.FriendReq;
import com.cynen.uchat.dto.Result;
import com.cynen.uchat.dto.User;
import com.cynen.uchat.pojo.TbFriendReq;
import com.cynen.uchat.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 好友相关的请求.
 */
@RestController
@RequestMapping("/friend")
public class FriendController {

    @Autowired
    private FriendService friendService;

    /**
     * 发送好友申请.
     * @param friendReq
     * @return
     */
    @RequestMapping("/sendRequest")
    public Result sendRequest(@RequestBody TbFriendReq friendReq){
        try {
            friendService.sendRequest(friendReq);
            return new Result(true,"发送好友请求成功!");
        }catch (RuntimeException e){
            return new Result(false,e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"发送请求异常!");
        }
    }

    /**
     * 查询当前用户所有的添加好友请求
     * @param userid
     * @return
     */
    @RequestMapping("/findFriendReqByUserid")
    public List<FriendReq> findFriendReqByUserid(String userid){
        return friendService.findFriendReqByUserid(userid);
    }

    /**
     * 处理好友申请.
     * @param reqid 好友请求id
     * @param result 页面处理结果, 0表示拒绝, 1表示通过.
     * @return
     */
    @RequestMapping("/handlerFriendReq")
    public Result handlerFriendReq(String reqid,int result ){
        // 好友请求处理,根据result. 其次,变更好友请求的状态.
        try {
            friendService.handlerFriendReq(reqid,result);
            return new Result(true,"好友申请处理成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"好友申请处理异常!");
        }
    }

    /**
     * g根据用户id查询当前用户所有的好友.
     * @param userid
     * @return
     */
    @RequestMapping("/findFriendByUserid")
    public List<User> findFriendByUserid(String userid){
        return friendService.findFriendByUserid(userid);
    }
}
