package com.tomgs.springboot;

import com.tomgs.springboot.entity.User;
import com.tomgs.springboot.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author tangzhongyuan
 * @since 2019-07-22 17:41
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class SampleTest {

    @Resource
    private UserMapper userMapper;

    @Test
    public void testSelect() {
        final List<User> users = userMapper.selectList(null);
        users.forEach(System.out::println);
    }

}
