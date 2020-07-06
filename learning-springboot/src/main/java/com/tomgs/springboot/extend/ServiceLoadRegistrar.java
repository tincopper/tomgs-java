package com.tomgs.springboot.extend;

import java.beans.Introspector;
import java.util.Set;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.NonNull;
import org.springframework.util.CollectionUtils;

/**
 *  
 *
 * @author tomgs
 * @version 2020/7/5 1.0 
 */
public class ServiceLoadRegistrar implements ImportBeanDefinitionRegistrar {

    private final ServiceProvider serviceProvider;

    public ServiceLoadRegistrar(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    @Override
    public void registerBeanDefinitions(@NonNull AnnotationMetadata importingClassMetadata, @NonNull BeanDefinitionRegistry registry) {
        Set<Class<?>> services = serviceProvider.getServices();
        if (CollectionUtils.isEmpty(services)) {
            return;
        }
        services.forEach(service -> {
            String beanName = Introspector.decapitalize(service.getSimpleName());
            BeanDefinition beanDefinition = BeanDefinitionBuilder
                .genericBeanDefinition(service).getBeanDefinition();
            registry.registerBeanDefinition(beanName, beanDefinition);
        });
    }

}
