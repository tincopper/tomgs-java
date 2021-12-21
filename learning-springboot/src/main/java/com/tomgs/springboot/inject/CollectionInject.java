package com.tomgs.springboot.inject;

import com.tomgs.springboot.entity.User;
import com.tomgs.springboot.service.UserService;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * CollectionInject
 *
 * @author tomgs
 * @since 2021/12/17
 */
@Component
public class CollectionInject {

    private Map<String, UserService<User>> userServiceMap;

}
