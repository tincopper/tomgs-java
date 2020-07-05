package com.tomgs.springboot.configure;

import com.tomgs.springboot.extend.ServiceLoadRegistrar;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *  加载JavaService类
 *
 * @author tomgs
 * @version 2020/7/5 1.0 
 */
@Configuration
public class JavaServiceLoadConfig {


    @Bean
    public ServiceLoadRegistrar serviceLoadRegistrar() {
        return new ServiceLoadRegistrar();
    }


}
