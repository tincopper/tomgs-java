package com.tomgs.core.appachetools.commonsio;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.NameFileFilter;
import org.apache.commons.io.filefilter.NotFileFilter;
import org.apache.commons.text.AlphabetConverter;
import org.apache.commons.text.StringEscapeUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author tomgs
 * @since 2021/8/17
 */
public class FileListDemo {

    private final String loggerName = "hodor-job-execute-record.log";

    @Test
    public void listFiles() throws IOException {
        final File logsDir = new File("E:\\data\\hodor-scheudler\\recordset\\logs");
        if (!logsDir.exists() || !logsDir.isDirectory()) {
            return;
        }
        File[] files = logsDir.listFiles((FileFilter) new NotFileFilter(new NameFileFilter(loggerName)));
        assert files != null;
        for (File file : files) {
            System.out.println(file.getName() + "<>" + file.lastModified());
            List<String> lines = FileUtils.readLines(file, StandardCharsets.UTF_8);
            lines.forEach(System.out::println);
            for (String line : lines) {
                if (!line.startsWith("I") & !line.startsWith("U")) {
                    continue;
                }

            }
        }

    }

    @Test
    public void testAc() throws UnsupportedEncodingException {
        Character[] originals = new Character[] {'\n'};   // a, b, c, d
        Character[] encoding = new Character[] {'n'};    // 0, 1, d
        Character[] doNotEncode = new Character[]{}; // d

        AlphabetConverter ac = AlphabetConverter.createConverterFromChars(originals, encoding, doNotEncode);

        System.out.println(ac.encode("a"));
        System.out.println(ac.encode("b"));
        System.out.println(ac.encode("c"));
        System.out.println(ac.encode("d"));
        System.out.println(ac.encode("abcd"));
    }

    @Test
    public void testEscape() {
        String str = "a\nb\nc";
        String escapeCsv = StringEscapeUtils.escapeCsv(str);
        System.out.println(escapeCsv);

        String translate = StringEscapeUtils.ESCAPE_JAVA.translate(str);
        System.out.println(translate);
    }

}
