package com.tomgs.core.base.yaml;

import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

/**
 * YamlTest
 *
 * @author tomgs
 * @since 2022/9/22
 */
public class YamlTest {

    private File loadYamlFileFromResource(String yamlName) {
        //final ClassLoader loader = getClass().getClassLoader();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL resource = loader.getResource(yamlName);
        return new File(resource.getFile());
    }

    @Test
    public void read() throws IOException {
        final File file = loadYamlFileFromResource("hello_world_flow.flow");
        Yaml yml = new Yaml();
        FileReader reader = new FileReader(file);
        BufferedReader buffer = new BufferedReader(reader);
        Properties map = yml.loadAs(buffer, Properties.class);

        System.out.println(map.get("nodes"));
        buffer.close();
        reader.close();
    }

}
