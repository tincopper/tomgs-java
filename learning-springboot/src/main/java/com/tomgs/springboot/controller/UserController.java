package com.tomgs.springboot.controller;

import com.tomgs.springboot.entity.User;
import com.tomgs.springboot.mapper.UserMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author tangzhongyuan
 * @since 2019-07-22 18:25
 **/
@RestController
@Api("测试api")
public class UserController {

    @Resource
    private UserMapper userMapper;

    @ApiOperation(value = "获取用户列表", notes = "获取用户列表")
    @GetMapping("users")
    public List<User> getUserList() {
        return userMapper.selectList(null);
    }

    @ApiOperation(value = "查询指定用户", notes = "根据url的id来查询用户")
    @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "Long", paramType = "path")
    @GetMapping("user/{id}")
    public User getUserById(@PathVariable(value = "id") Integer id) {
        return userMapper.selectById(id);
    }

}
