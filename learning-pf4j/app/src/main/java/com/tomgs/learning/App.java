package com.tomgs.learning;

import com.tomgs.learning.api.Greeting;
import org.pf4j.DefaultPluginManager;
import org.pf4j.JarPluginManager;
import org.pf4j.PluginManager;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;

/**
 * Hello world!
 */
public class App {

    public static void main(String[] args) {
        //使用默认的插件管理器
        //PluginManager pluginManager = new DefaultPluginManager(new File("learning-pf4j/plugins/greeting-plugin/target/").toPath());
        //加载插件
        //pluginManager.loadPlugins();

        // 使用加载Jar插件包
        JarPluginManager pluginManager = new JarPluginManager();
        pluginManager.loadPlugin(Paths.get("learning-pf4j/plugins/greeting-plugin/target/greeting-plugin-1.0-SNAPSHOT-all.jar"));

        //启动插件
        pluginManager.startPlugin("greeting-plugin");

        //获取所有的Greeting的扩展实现
        List<Greeting> greetings = pluginManager.getExtensions(Greeting.class, "greeting-plugin");
        if (greetings.size() == 0) {
            System.err.println("未找到插件");
        }
        for (Greeting greeting : greetings) {
            System.out.println( greeting.message("张三"));
        }
    }

}
