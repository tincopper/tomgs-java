package com.tomgs.spring.service;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 *  
 *
 * @author tomgs
 * @version 2019/11/12 1.0 
 */
@Service
public class UserService {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  public void selectUsers() {
    String sql = "select * from user";

    Map<String, Object> map = jdbcTemplate.queryForMap(sql);
    System.out.println(map);
  }

}
