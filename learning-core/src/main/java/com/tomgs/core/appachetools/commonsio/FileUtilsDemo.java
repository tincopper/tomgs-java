package com.tomgs.core.appachetools.commonsio;

import java.io.File;
import java.util.Collection;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.FileFileFilter;

/**
 * @author tomgs
 * @since 2021/3/15
 */
public class FileUtilsDemo {

  public static void main(String[] args) {
    Collection<File> files = FileUtils
        .listFiles(new File("E:\\typescript_workspace\\xc-ide\\plugins\\bos-ext-plugin"), FileFileFilter.FILE, null);
    System.out.println(files);

    Collection<File> files1 = FileUtils
        .listFiles(new File("E:\\typescript_workspace\\xc-ide\\plugins\\bos-ext-plugin"), FileFileFilter.FILE,
            DirectoryFileFilter.DIRECTORY);
    System.out.println(files1);
  }

}
