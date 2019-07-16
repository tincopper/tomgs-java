package com.tomgs.core.java8;

import java.io.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *  用Java实现shell命令cat 1.log | grep a | sort | uniq -c | sort -rn的功能
 *
 * @author tomgs
 * @version 2019/6/8 1.0 
 */
public class TestLambda {

    public static void main(String[] args) throws IOException {
        final File logFile = new File("E:\\workspace\\sourcecode\\tomgs-java\\learning-core" +
                "\\src\\main\\java\\com\\tomgs\\core\\java8\\1.txt");
        final FileReader fileReader = new FileReader(logFile);
        final BufferedReader bufferedReader = new BufferedReader(fileReader);

        List<String> lines = new ArrayList<>();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            lines.add(line);
        }
        System.out.println(lines);

        // grep a ==> filter
        lines = lines.stream().filter(t -> t.contains("a")).collect(Collectors.toList());

        // sort
        lines = lines.stream().sorted().collect(Collectors.toList());

        // uniq -c 统计重复字段次数
        final Map<String, Long> stringLongMap =
                lines.stream().sorted().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        // sort -rn 按数值进行倒序排序
        //final List<Long> collect = stringLongMap.values().stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        Map<String, Long> finalMap = new LinkedHashMap<>();
        //Sort a map and add to finalMap
        final Comparator<Map.Entry<String, Long>> reversed = Map.Entry.<String, Long>comparingByValue().reversed();
        stringLongMap.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue()
                        .reversed()).forEachOrdered(e -> finalMap.put(e.getKey(), e.getValue()));

        System.out.println(finalMap);
    }
}
