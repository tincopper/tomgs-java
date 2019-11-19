package com.tomgs.spring.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 *  
 *
 * @author tomgs
 * @version 2019/11/11 1.0 
 */
@Configuration
@ComponentScan(value = {"com.tomgs.spring.service"})
public class AppConfig {
    
}
