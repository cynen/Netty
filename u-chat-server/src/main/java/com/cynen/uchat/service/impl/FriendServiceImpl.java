package com.cynen.uchat.service.impl;

import com.cynen.uchat.dto.FriendReq;
import com.cynen.uchat.dto.User;
import com.cynen.uchat.mapper.TbFriendMapper;
import com.cynen.uchat.mapper.TbFriendReqMapper;
import com.cynen.uchat.mapper.TbUserMapper;
import com.cynen.uchat.pojo.*;
import com.cynen.uchat.service.FriendService;
import com.cynen.uchat.utils.IdWorker;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class FriendServiceImpl implements FriendService {

    @Autowired
    private TbUserMapper userMapper;
    @Autowired
    private TbFriendReqMapper friendReqMapper;
    @Autowired
    private TbFriendMapper friendMapper;

    @Autowired
    private IdWorker idWorker;

    /**
     * 发送好友申请.
     * @param friendReq
     */
    @Override
    public void sendRequest(TbFriendReq friendReq) {
        //1.已经是好友的,不可以发送
        //2.已经发过了,未处理,不用再发.
        TbFriend tbFriend = friendMapper.selectByPrimaryKey(friendReq.getFromUserid());
        // 好友表不为空,并且好友id和请求id是同一个人.
        if (tbFriend !=null && tbFriend.getFriendsId().equals(friendReq.getToUserid())){
            throw new RuntimeException("你们已经是好友了!");
        }else {
            // 查询区请求表中是否有对应的请求.
            TbFriendReqExample example = new TbFriendReqExample();
            TbFriendReqExample.Criteria criteria = example.createCriteria();
            criteria.andFromUseridEqualTo(friendReq.getFromUserid());
            criteria.andToUseridEqualTo(friendReq.getToUserid());
            List<TbFriendReq> friendReqs = friendReqMapper.selectByExample(example);
            // 确定是否处理过.
            if(friendReqs.size() > 0){
                //TODO 已请求,需要区分是否已经处理过.这里默认未处理.
                throw new RuntimeException("已经请求过!");
            }else {
                // 没有记录.
                friendReq.setId(idWorker.nextId());
                friendReq.setStatus(0); // 0 表示未处理.1表示已处理.
                friendReq.setCreatetime(new Date());
                friendReqMapper.insert(friendReq);
            }
        }
    }

    /**
     * 根据当前登录用户id,查询所有的好友请求.
     * @param userid
     * @return
     */
    @Override
    public List<FriendReq> findFriendReqByUserid(String userid) {
        // 只能查询未处理的好友请求.
        TbFriendReqExample example = new TbFriendReqExample();
        TbFriendReqExample.Criteria criteria = example.createCriteria();
        criteria.andToUseridEqualTo(userid);
        criteria.andStatusEqualTo(0); // 未处理的请求
        List<TbFriendReq> tbFriendReqs = friendReqMapper.selectByExample(example);
        List<FriendReq> list = new ArrayList<>();
        for (TbFriendReq tbFriendReq:tbFriendReqs){
            FriendReq friendReq = new FriendReq();
            // 获得请求用户.
            TbUser tbUser = userMapper.selectByPrimaryKey(tbFriendReq.getFromUserid());
            BeanUtils.copyProperties(tbUser,friendReq);
            friendReq.setId(tbFriendReq.getId());
            list.add(friendReq);
        }
        return list;
    }

    /**
     * 处理好友请求.
     * @param reqid
     * @param result  0 表示忽略, 1表示通过
      */
    @Override
    public void handlerFriendReq(String reqid, int result) {
        // 1.通过,加互加好友.
        TbFriendReq tbFriendReq = friendReqMapper.selectByPrimaryKey(reqid);
        if (result == 1){
            // From --> To
            TbFriend friend = new TbFriend();
            friend.setId(idWorker.nextId());
            friend.setUserid(tbFriendReq.getFromUserid());
            friend.setFriendsId(tbFriendReq.getToUserid());
            friend.setCreatetime(new Date());
            friendMapper.insert(friend);
            // To --> From
            TbFriend friend2 = new TbFriend();
            friend2.setId(idWorker.nextId());
            friend2.setUserid(tbFriendReq.getToUserid());
            friend2.setFriendsId(tbFriendReq.getFromUserid());
            friend2.setCreatetime(new Date());
            friendMapper.insert(friend2);
        }
        //2.变更好友请求状态.已处理.
        tbFriendReq.setStatus(1);
        friendReqMapper.updateByPrimaryKey(tbFriendReq);
    }


    /**
     * s使用Userid查询当前用户所有的好友.
     * @param userid
     * @return
     */
    @Override
    public List<User> findFriendByUserid(String userid) {
        //
        TbFriendExample example = new TbFriendExample();
        TbFriendExample.Criteria criteria = example.createCriteria();
        criteria.andUseridEqualTo(userid);
        // TODO 后续考虑用户状态.
        List<TbFriend> friends = friendMapper.selectByExample(example);
        List<User> list = new ArrayList<>();
        for (TbFriend friend:friends){
            User user = new User();
            TbUser tbUser = userMapper.selectByPrimaryKey(friend.getFriendsId());
            if (tbUser != null){
                BeanUtils.copyProperties(tbUser,user);
            }
            list.add(user);
        }
        return list;
    }
}
