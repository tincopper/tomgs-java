package com.tomgs.springboot.service.impl;

import com.tomgs.springboot.entity.User;
import com.tomgs.springboot.mapper.UserMapper;
import com.tomgs.springboot.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author tangzhongyuan
 * @since 2019-08-05 17:01
 **/
@Service
public class UserServiceImpl implements UserService<User> {

    @Resource
    private UserMapper userMapper;

    @Override
    public List<User> getUserList() {
        return userMapper.selectList(null);
    }

}
