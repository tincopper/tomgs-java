package com.tomgs.spring.core;

import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static org.reflections.ReflectionUtils.*;

/**
 *  annotation request handler
 *
 * @author tomgs
 * @version 2020/12/6 1.0 
 */
public class AnnotationRequestHandler {

    private static final AnnotationRequestHandler HANDLER = new AnnotationRequestHandler();

    private final Map<String, Method> clickMaps;
    private final Map<String, Method> itemClickMaps;

    private AnnotationRequestHandler() {
        clickMaps = new ConcurrentHashMap<>();
        itemClickMaps = new ConcurrentHashMap<>();
    }

    public static AnnotationRequestHandler getInstance() {
        return HANDLER;
    }

    @SuppressWarnings("unchecked")
    public void doScanAnnotation(String... basePackages) {
        //入参 要扫描的包名
        Reflections f = new Reflections(new ConfigurationBuilder().forPackages(basePackages));

        //入参 目标注解类
        Set<Class<?>> pageServiceSet = f.getTypesAnnotatedWith(Page.class);
        for (Class<?> pageService : pageServiceSet) {
            System.out.println(pageService);

            Set<Method> clickMethods = getAllMethods(pageService, withModifier(Modifier.PUBLIC), withAnnotation(Click.class));
            Set<Method> itemClickMethods = getAllMethods(pageService, withModifier(Modifier.PUBLIC), withAnnotation(ItemClick.class));

            Page page = pageService.getAnnotation(Page.class);
            String pageIdentity = page.value();

            for (Method clickMethod : clickMethods) {
                Click click = clickMethod.getDeclaredAnnotation(Click.class);
                String clickIdentity = click.value();
                String id = pageIdentity + "_" + clickIdentity;
                clickMaps.put(id, clickMethod);
            }

            for (Method clickMethod : itemClickMethods) {
                ItemClick itemClick = clickMethod.getDeclaredAnnotation(ItemClick.class);
                String itemClickIdentity = itemClick.value();
                String id = pageIdentity + "_" + itemClickIdentity;
                itemClickMaps.put(id, clickMethod);
            }
        }

    }

}
