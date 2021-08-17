package com.tomgs.core.appachetools.commonscli;

import org.apache.commons.cli.*;
import org.junit.Test;

import java.util.List;

/**
 * CommandCliDemo
 *
 * @author tomgs
 * @since 2021/8/13
 */
public class CommandCliDemo {

    @Test
    public void testCliParser() throws ParseException {
        String args []={"-t 1000"};
        //定义
        Options options = new Options();
        options.addOption("h", false, "list help");//false代表不强制有
        options.addOption("t", true, "set time on system");

        //解析
        //1.3.1中已经弃用针对不同格式入参对应的解析器
        //CommandLineParser parser = new PosixParser();
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        //查询交互
        //你的程序应当写在这里，从这里启动
        if (cmd.hasOption("h")) {
            String formatstr = "CLI  cli  test";
            HelpFormatter hf = new HelpFormatter();
            hf.printHelp(formatstr, "", options, "");
            return;
        }

        if (cmd.hasOption("t")) {
            String value = cmd.getOptionValue("t");
            System.out.printf("system time has setted %s\n", value);
            return;
        }

        System.out.println("error");
    }

    @Test
    public void testKubectlParser() throws ParseException {
        // kubectl [commands] [resource] [resourceName] [options]
        String args []={"kubectl", "get", "pods", "1123123123", "-n", "test"};
        //定义
        Options options = new Options();
        options.addOption("kubectl", false, "list help");//false代表不强制有
        options.addOption("get", false, "list help");//false代表不强制有
        options.addOption("n", true, "set time on system");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
        List<String> argList = cmd.getArgList();
        String optionValue = cmd.getOptionValue("n");

        if ("kubectl".equals(argList.get(0))) {
            System.out.println("kubectl command.");
            String commandName = argList.get(1);
            // commandName pod service deployment
            String resourceName = argList.get(2);
            String resourceValue = argList.get(3);
        }

        if (cmd.hasOption("kubectl")) {
            String formatstr = "CLI  cli  test";
            HelpFormatter hf = new HelpFormatter();
            hf.printHelp(formatstr, "", options, "");
            return;
        }

        if (cmd.hasOption("get")) {
            String value = cmd.getOptionValue("get");
            System.out.printf("system time has setted %s\n", value);
            return;
        }

        System.out.println("error");
    }

}
