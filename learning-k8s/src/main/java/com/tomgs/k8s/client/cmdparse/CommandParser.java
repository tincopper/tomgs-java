package com.tomgs.k8s.client.cmdparse;

import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * @author tomgs
 * @since 2021/8/25
 */
public class CommandParser {

    public CommandLine parser(String[] args) throws ParseException {
        Options options = new Options();
        options.addOption("h", false, "list help");//false代表不强制有
        options.addOption("t", true, "set time on system");

        CommandLineParser parser = new DefaultParser();
        org.apache.commons.cli.CommandLine cmd = parser.parse(options, args);

        return null;
    }

}
