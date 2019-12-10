package com.cynen.uchat.service;

import com.cynen.uchat.dto.User;
import com.cynen.uchat.pojo.TbUser;

import java.util.List;

public interface UserService {

    List<TbUser> findAll();

    void register(TbUser user);

    User login(TbUser user);
}
