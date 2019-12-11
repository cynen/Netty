package com.cynen.uchat.controller;


import com.cynen.uchat.dto.Result;
import com.cynen.uchat.dto.User;
import com.cynen.uchat.pojo.TbUser;
import com.cynen.uchat.service.UserService;
import com.cynen.uchat.utils.FastDFSClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
            return new Result(false,e.getMessage());
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

    /**
     * 上传图像
     * @param file
     * @param userid
     * @return
     */
    @RequestMapping("/upload")
    public Result upload(MultipartFile file,String userid){
        try {
            // 上传文件后,获得图片的url.不带http:// host....
            User user = userServcie.updatePic(file,userid);
            // 判断时候上传成功.
            if (user != null){
                return new Result(true,"上传成功",user);
            }else {
                return new Result(false,"上传失败");
            }
        } catch (Exception e){
            System.out.println("上传图片异常: "+e.getMessage());
            return new Result(false,"上传失败");
        }
    }

    /**
     *
     * @param tbUser 接收请求参数. 主要包含 id和nickname.
     * @return
     */
    @RequestMapping("/updateNickname")
    public Result updateNickname(@RequestBody TbUser tbUser){
        // 获取传递过来的参数,主要包含 id和新的nickname
        try {
            userServcie.updateNickname(tbUser);
            return new Result(true,"昵称更新成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"昵称更新失败!");
        }
    }

    /**
     * 根据id查询用户.
     * @param userid
     * @return
     */
    @RequestMapping("/findById")
    public User findById(String userid){
        // 直接返回用户对象
        return userServcie.findUserById(userid);
    }
}
