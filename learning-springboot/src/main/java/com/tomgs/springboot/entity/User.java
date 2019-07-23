package com.tomgs.springboot.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * @author tangzhongyuan
 * @since 2019-07-22 17:23
 **/
@Data
public class User {

    private int id;

    private String name;

    //若没有开启驼峰命名，或者表中列名不符合驼峰规则，可通过该注解指定数据库表中的列名，exist标明数据表中有没有对应列
    @TableField(value = "grade_id", exist = true)
    private String gradeId;
}
