package com.tomgs.core.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import org.junit.Test;

/**
 * @author tomgs
 * @since 2021/3/23
 */
public class FileChannelTest {

  @Test
  public void testFileChannel() throws IOException {
    File file = new File("./test.txt");
    FileInputStream fileInputStream = new FileInputStream(file);
    FileChannel fileChannelInput = fileInputStream.getChannel();
    //将fileChannelInput通道的数据，写入到fileChannelOutput通道
    //fileChannelInput.transferTo(0, fileChannelInput.size(), fileChannelOutput);
    FileChannel position = fileChannelInput.position(1);

  }

}
