package com.cynen.uchat.service.impl;

import com.cynen.uchat.dto.User;
import com.cynen.uchat.mapper.TbUserMapper;
import com.cynen.uchat.pojo.TbUser;
import com.cynen.uchat.pojo.TbUserExample;
import com.cynen.uchat.service.UserService;
import com.cynen.uchat.utils.IdWorker;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private TbUserMapper userMapper;

    @Autowired
    private IdWorker idWorker;

    @Override
    public List<TbUser> findAll() {
        return  userMapper.selectByExample(null);
    }


    /**
     * 注册用户
     * @param user
     */
    @Override
    public void register(TbUser user) {
        // 1.先判断用户是否已经存在.
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(user.getUsername());
        List<TbUser> tbUsers = userMapper.selectByExample(example);
        // 用户已存在
        if(tbUsers != null && tbUsers.size() > 0){
            throw  new RuntimeException("用户已存在!");
        }else {
        // 不存在就正常插入.
            user.setId(idWorker.nextId());
            // 对密码进行加密.
            user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
            user.setPicNormal("");
            user.setPicSmall("");
            user.setNickname(user.getUsername());
            user.setCreatetime(new Date());
            userMapper.insert(user);
        }

    }

    /**
     * 用户登录
     * @param user
     * @return
     */
    @Override
    public User login(TbUser user) {
        // 1.使用用户名和密码查询.
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(user.getUsername());
        List<TbUser> tbUsers = userMapper.selectByExample(example);
        if(tbUsers != null && tbUsers.size() > 0){
            TbUser userIndb = tbUsers.get(0);
            //MD5加密认证.
            if(userIndb.getPassword().equals(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()))){
                User userdto = new User();
                BeanUtils.copyProperties(userIndb,userdto);
                return userdto;
            }else {
                throw new RuntimeException("密码错误!");
            }
        }else {
            // 没有查到对应的用户
            throw new RuntimeException("用户不存在!");
        }
    }
}
