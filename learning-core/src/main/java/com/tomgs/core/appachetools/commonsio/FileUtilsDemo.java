package com.tomgs.core.appachetools.commonsio;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.junit.Test;

/**
 * @author tomgs
 * @since 2021/3/15
 */
public class FileUtilsDemo {

    @Test
    public void testListFiles() {
        Collection<File> files = FileUtils
                .listFiles(new File("E:\\typescript_workspace\\xc-ide\\plugins\\bos-ext-plugin"), FileFileFilter.FILE, null);
        System.out.println(files);

        Collection<File> files1 = FileUtils
                .listFiles(new File("E:\\typescript_workspace\\xc-ide\\plugins\\bos-ext-plugin"), FileFileFilter.FILE,
                        DirectoryFileFilter.DIRECTORY);
        System.out.println(files1);
    }

    @Test
    public void testFileMove() throws IOException {
        File srcFile = new File("E:\\data\\hodor-scheudler\\recordset\\logs\\hodor-job-execute-record.log_20210817205632");
        File destFile = new File("E:\\data\\hodor-scheudler\\recordset\\backup\\hodor-job-execute-record.log_20210817205632");
        FileUtils.moveFile(srcFile, destFile);
    }

}
