package com.cynen.uchat.service;

import com.cynen.uchat.dto.User;
import com.cynen.uchat.pojo.TbUser;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

public interface UserService {

    List<TbUser> findAll();

    void register(TbUser user);

    User login(TbUser user);

    User updatePic(MultipartFile file, String userid);

    void updateNickname(TbUser tbUser);

    User findUserById(String userid);
}
