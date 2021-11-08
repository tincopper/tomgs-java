package com.tomgs.proto.gen;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import org.junit.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * TemplateTest
 *
 * @author tomgs
 * @since 2021/11/8
 */
public class TemplateTest {

    private final MustacheFactory MUSTACHE_FACTORY = new DefaultMustacheFactory();

    @Test
    public void testMustache() {

        String resourcePath = "test.mustache";

        Map<String, Object> subContext = new HashMap<>();
        subContext.put("subA", "a");
        subContext.put("subB", "b");

        Map<String, Object> generatorContext = new HashMap<>();
        generatorContext.put("a", "msg");
        generatorContext.put("subA", "666");
        generatorContext.put("b", null);

        Preconditions.checkNotNull(resourcePath, "resourcePath");
        Preconditions.checkNotNull(generatorContext, "generatorContext");

        InputStream resource = MustacheFactory.class.getClassLoader().getResourceAsStream(resourcePath);
        if (resource == null) {
            throw new RuntimeException("Could not find resource " + resourcePath);
        }

        InputStreamReader resourceReader = new InputStreamReader(resource, Charsets.UTF_8);
        Mustache template = MUSTACHE_FACTORY.compile(resourceReader, resourcePath);
        String result = template.execute(new StringWriter(), generatorContext).toString();
        System.out.println(result);
    }

}
