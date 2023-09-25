package com.tomgs.springboot.controller;

import com.tomgs.springboot.entity.User;
import com.tomgs.springboot.mapper.UserMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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

    @GetMapping(value = "/sse")
    @ResponseBody
    public String getMessage(HttpServletResponse response) {
        // System.out.println("请求进入了");
        response.setContentType("text/event-stream");   // 指定ContentType，不可变
        response.setCharacterEncoding("utf-8");         // 指定响应字符集，是否可变，没测试，但建议指定utf-8
        while (true) {
            String s = "";
            s += "retry: 5000\n";                   // 客户端没有获取到数据，就不断发送新的请求（间隔5秒），直到有数据。
            s += "id: " + UUID.randomUUID() + "\n"; // 这里指定消息ID
            s += "event: eventType\n";              // 这里定义事件类型，自定义！
            s += "data: " + new Date() + "\n\n";    // 这里设置返回的数据
            try {
                PrintWriter pw = response.getWriter();
                Thread.sleep(1000L);
                pw.write(s);
                pw.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
