package com.tomgs.learning;

import com.tomgs.learning.api.Greeting;
import org.pf4j.DefaultPluginManager;
import org.pf4j.PluginManager;

import java.io.File;
import java.util.List;

/**
 * Hello world!
 */
public class App {

    public static void main(String[] args) {
        //使用默认的插件管理器
        PluginManager pluginManager = new DefaultPluginManager(new File("/app/plugins").toPath());

        //加载插件
        pluginManager.loadPlugins();

        //启动插件
        pluginManager.startPlugins();

        //获取所有的Greeting的扩展实现
        List<Greeting> greetings = pluginManager.getExtensions(Greeting.class);
        for (Greeting greeting : greetings) {
            System.out.println( greeting.message("张三"));
        }
    }

}
