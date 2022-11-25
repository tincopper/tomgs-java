package com.tomgs.learning;

import cn.hutool.core.util.ReflectUtil;
import com.tomgs.learning.api.Greeting;
import org.pf4j.JarPluginManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Paths;
import java.util.List;

/**
 * Hello world!
 */
public class App {

    public static void main(String[] args) throws ClassNotFoundException {
        //使用默认的插件管理器
        //PluginManager pluginManager = new DefaultPluginManager(new File("learning-pf4j/plugins/greeting-plugin/target/").toPath());
        //加载插件
        //pluginManager.loadPlugins();

        // 使用加载Jar插件包
        JarPluginManager pluginManager = new JarPluginManager();
        pluginManager.loadPlugin(Paths.get("learning-pf4j/plugins/greeting-plugin/target/greeting-plugin-1.0-SNAPSHOT-all.jar"));

        //启动插件
        pluginManager.startPlugin("greeting-plugin");
        //final ClassLoader pluginClassLoader = pluginManager.getPluginClassLoader("greeting-plugin");
        //pluginClassLoader.loadClass("com.tomgs.learning.api.Greeting");

        //获取所有的Greeting的扩展实现
        final List<?> greetings = pluginManager.getExtensions("greeting-plugin");
        if (greetings.size() == 0) {
            System.err.println("未找到插件");
        }
        //for (Greeting greeting : greetings) {
        //    System.out.println( greeting.message("张三"));
        //}
        greetings.forEach(e -> {
            final Object result = ReflectUtil.invoke(e, "message", "tomgs");
            System.out.println(result);
            // or
            /*try {
                final Method message = e.getClass().getMethod("message", String.class);
                final Object result = message.invoke(e, "tomgs");
                System.out.println(result);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
                throw new RuntimeException(ex);
            }*/
        });

        pluginManager.stopPlugin("greeting-plugin");
        final List<?> greetings1 = pluginManager.getExtensions("greeting-plugin");
        System.out.println(greetings1);

        pluginManager.unloadPlugin("greeting-plugin");
        final List<?> greetings2 = pluginManager.getExtensions("greeting-plugin");
        System.out.println(greetings2);
    }

}
