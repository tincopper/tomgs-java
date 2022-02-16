package com.tomgs.core.file;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * FileLinkTest
 *
 * @author tomgs
 * @since 2022/2/16
 */
public class FileLinkTest {

    /**
     * 创建单个文件链接
     */
    @Test
    public void testLink() throws IOException {
        File existing = new File("E:\\data\\test\\test.txt");
        File link = new File("E:\\data\\test1\\test.txt");

        Files.deleteIfExists(link.toPath());

        Files.createLink(Paths.get(link.getAbsolutePath()), existing.toPath());
    }

    /**
     * 创建目录下面所有文件的链接
     */
    @Test
    public void testLink2() throws IOException {
        File sourceDir = new File("E:\\data\\test");
        File destDir = new File("E:\\data\\test1");
        FileIOUtils.createDeepHardlink(sourceDir, destDir);
    }

}
