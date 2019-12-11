package com.cynen.uchat.service.impl;

import com.cynen.uchat.dto.User;
import com.cynen.uchat.mapper.TbUserMapper;
import com.cynen.uchat.pojo.TbUser;
import com.cynen.uchat.pojo.TbUserExample;
import com.cynen.uchat.service.UserService;
import com.cynen.uchat.utils.FastDFSClient;
import com.cynen.uchat.utils.IdWorker;
import com.cynen.uchat.utils.QRCodeUtils;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private TbUserMapper userMapper;

    @Autowired
    private IdWorker idWorker;
    @Autowired
    private FastDFSClient fastDFSClient;

    @Autowired
    private Environment env;

    // 生成二维码
    @Autowired
    private QRCodeUtils qrCodeUtils;

    @Override
    public List<TbUser> findAll() {
        return  userMapper.selectByExample(null);
    }


    /**
     * 返回用户对象视图
     * @param userid
     * @return
     */
    @Override
    public User findUserById(String userid) {
        User user = new User();
        TbUser tbUser = userMapper.selectByPrimaryKey(userid);
        BeanUtils.copyProperties(tbUser,user);
        return user;
    }

    /**
     * 更新昵称.
     * @param tbUser
     */
    @Override
    public void updateNickname(TbUser tbUser) {
        // 1.先查询出对应的user.
        // 2.更新对应的昵称.
        TbUser user = userMapper.selectByPrimaryKey(tbUser.getId());
        user.setNickname(tbUser.getNickname());
        userMapper.updateByPrimaryKey(user);

    }

    /**
     * 上传图片,上传成功就返回当前用户对象.
     *  上传失败就返回null
     * @param file
     * @param userid
     * @return
     */
    @Override
    public User updatePic(MultipartFile file,String userid) {
        // 通过userid查询指定的用户.
        // 更新图片信息.
        // 返回userVO
        try {
            String url = fastDFSClient.uploadFace(file);
            System.out.println("上传图片:"+ url);
            // 处理小图
            String suffix = "_150x150.";
            String[] paths = url.split("\\.");
            // 小图.
            String picsmall = paths[0] + suffix + paths[1];
            // 更新图片信息.将图片的完全路径放到系统中,不要只放置一半.
            TbUser tbUser = userMapper.selectByPrimaryKey(userid);
            tbUser.setPicNormal(env.getProperty("fdfs.httpurl") + url);
            tbUser.setPicSmall(env.getProperty("fdfs.httpurl")+ picsmall);
            userMapper.updateByPrimaryKey(tbUser);
            // 返回给客户端的信息:
            User user = new User();
            BeanUtils.copyProperties(tbUser,user);
            System.out.println(user);
            return user;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("上传异常!");
        }





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

            // 生成二维码名片
            String tmpFoder = env.getProperty("hcat.tmpdir");
            // 文件夹不存在就创建.
            File file = new File(tmpFoder);
            if (!file.exists()){
                // 如果文件夹不存在,就主动创建.
                    file.mkdirs();
            }
            String qrCodeFile = tmpFoder + "/" + user.getUsername() + ".png";
            // 生成的二维码信息: userCode:id:username
            qrCodeUtils.createQRCode(qrCodeFile,"userCode:" + user.getId()+":"+user.getUsername());
            // 上传二维码
            try {
                String url = fastDFSClient.uploadFile(new File(qrCodeFile));
                user.setQrcode(env.getProperty("fdfs.httpurl") + url);
            }catch (Exception e) {
                System.out.println(e.getMessage());
                throw new RuntimeException("上传文件失败!");
            }

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

    /**
     * 查询可以添加为好友的用户.
     * @param userid
     * @param friendUsername
     * @return
     */
    @Override
    public User findFrindByUsername(String userid, String friendUsername) {
        // 1.本人不能添加本人.
        // 2.已经添加过的不可以再添加.
        // 如果查到符合的,返回对象,未查到,返回null
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        // 做完全匹配.
        // TODO 这里前台待优化,查询的是多条记录需要处理.
        criteria.andUsernameEqualTo(friendUsername);
        List<TbUser> tbUsers = userMapper.selectByExample(example);
        if (tbUsers.size()>0){
            if (userid.equals(tbUsers.get(0).getId())){
                // 是本人.
                return null;
            }else {
                // 不是本人.
                User user  = new User();
                BeanUtils.copyProperties(tbUsers.get(0),user);
                return user;
            }
        }else {
            return null;
        }
    }
}
