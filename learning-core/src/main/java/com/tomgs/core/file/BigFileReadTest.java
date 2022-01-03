package com.tomgs.core.file;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;

/**
 * BigFileReadTest
 *
 * @author tomgs
 * @since 2022/1/3
 */
public class BigFileReadTest {

    public static void main(String[] args) throws IOException {
        String fileName = "F:\\dml_only.sql";
        InputStream inputStream = new FileInputStream(fileName);
        if (fileName.endsWith(".gz")) {
            inputStream = new GZIPInputStream(inputStream);
        }
        //BufferedReader reader = new BufferedReader(new InputStreamReader(gis));
        BufferedInputStream stream = new BufferedInputStream(inputStream);
        byte[] bytes = "\\.".getBytes(StandardCharsets.UTF_8);

        int read;
        while ((read = stream.read()) != -1) {
            if (read == '\\') {
                if (stream.read() == '.') {
                    System.out.println("end");
                    continue;
                }
                System.out.println(read);
            }
        }

    }

}
