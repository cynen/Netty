package com.cynen.uchat.controller;


import com.cynen.uchat.dto.Result;
import com.cynen.uchat.dto.User;
import com.cynen.uchat.pojo.TbUser;
import com.cynen.uchat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户相关的控制器.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userServcie;

    // 获取所有用户.
    @RequestMapping("/findAll")
    public List<TbUser> getAllUser(){
        return  userServcie.findAll();
    }

    // 用户登录.
    @RequestMapping("/login")
    public Result login(@RequestBody  TbUser user){

        try {
            User _user = userServcie.login(user);
            if (_user == null) {
                return new Result(false,"登录失败,请检查用户名或密码");
            }else {
                return new Result(true,"登录成功",_user);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"登陆失败");
        }

    }

    // 用户注册
    @RequestMapping("/register")
    public Result register(@RequestBody TbUser user){
        try {
            userServcie.register(user);
            return new Result(true, "注册成功");
        } catch (RuntimeException e) {
            return new Result(false,e.getMessage());
        }

    }

}
