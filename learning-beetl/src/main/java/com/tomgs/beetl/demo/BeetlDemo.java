package com.tomgs.beetl.demo;

import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.StringTemplateResourceLoader;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * beetl demo
 *
 * beetl支持字符串模板、文件资源模板、ClassPath资源模板、Web资源模板、自定义资源模板，这里演示字符串资源模板
 *
 * @author tomgs
 * @date 2021/7/27 19:07
 * @since 1.0
 */
public class BeetlDemo {

    private GroupTemplate groupTemplate;

    @Before
    public void setup() throws IOException {
        //new一个模板资源加载器
        StringTemplateResourceLoader resourceLoader = new StringTemplateResourceLoader();
        /* 使用Beetl默认的配置。
         * Beetl可以使用配置文件的方式去配置，但由于此处是直接上手的例子，
         * 我们不去管配置的问题，只需要基本的默认配置就可以了。
         */
        Configuration config = Configuration.defaultConfiguration();
        //Beetl的核心GroupTemplate
        groupTemplate = new GroupTemplate(resourceLoader, config);
    }

    @Test
    public void testBeetlHelloWorld() {

        //我们自定义的模板，其中${title}就Beetl默认的占位符
        String testTemplate="<html>\n" +
                "<head>\n" +
                "\t<title>${title}</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "\t<h1>${name}</h1>\n" +
                "</body>\n" +
                "</html>";
        Template template = groupTemplate.getTemplate(testTemplate);
        template.binding("title","This is a test template Email.");
        template.binding("name", "beetl");
        //渲染字符串
        String str = template.render();
        System.out.println(str);
    }

    /**
     * 测试是否支持级联查找
     */
    @Test
    public void testBeetl2() {
        Map<String, Object> map = new HashMap<>();
        map.put("a", "1");
        map.put("b", map);

        String tm = "test ${a}, ${b.a}";
        Template template = groupTemplate.getTemplate(tm);
        template.binding(map);
        String render = template.render();
        System.out.println(render);
    }

}
