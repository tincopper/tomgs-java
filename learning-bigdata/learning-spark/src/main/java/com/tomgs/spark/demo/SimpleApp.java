package com.tomgs.spark.demo;

import org.apache.spark.api.java.function.FilterFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.SparkSession;

/**
 * @author tangzy
 * @since 1.0
 */
public class SimpleApp {

  public static void main(String[] args) {
    String logFile = "./learning-bigdata/learning-spark/test.txt"; // Should be some file on your system
    // mster解释： http://spark.apache.org/docs/latest/submitting-applications.html#master-urls
    SparkSession spark = SparkSession.builder().master("local[2]").appName("Simple Application").getOrCreate();
    Dataset<String> logData = spark.read().textFile(logFile).cache();

    long numAs = logData.filter((FilterFunction<String>) s -> s.contains("a")).count();
    long numBs = logData.filter((FilterFunction<String>) s -> s.contains("b")).count();

    System.out.println("Lines with a: " + numAs + ", lines with b: " + numBs);

    spark.stop();
  }

}
