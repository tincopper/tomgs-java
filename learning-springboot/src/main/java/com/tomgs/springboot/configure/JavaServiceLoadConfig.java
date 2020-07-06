package com.tomgs.springboot.configure;

import com.tomgs.springboot.extend.LoadServiceProvider;
import com.tomgs.springboot.extend.ServiceLoadRegistrar;
import com.tomgs.springboot.extend.ServiceProvider;
import com.tomgs.springboot.service.impl.UserServiceImpl;
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
    public ServiceProvider serviceProvider() {
        LoadServiceProvider provider = new LoadServiceProvider();
        provider.loadService(UserServiceImpl.class);
        return provider;
    }

    @Bean
    public ServiceLoadRegistrar serviceLoadRegistrar() {
        return new ServiceLoadRegistrar(serviceProvider());
    }

}
