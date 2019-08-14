package com.tomgs.springboot.service;

import java.util.List;

/**
 * @author tangzhongyuan
 * @since 2019-08-05 17:01
 **/
public interface UserService<T> {

    List<T> getUserList();

}
