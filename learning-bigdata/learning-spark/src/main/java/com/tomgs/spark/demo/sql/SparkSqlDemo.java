package com.tomgs.spark.demo.sql;

import static org.apache.spark.sql.functions.col;

import org.apache.spark.sql.AnalysisException;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

/**
 * http://spark.apache.org/docs/latest/sql-getting-started.html
 *
 * @author tangzy
 * @since 1.0
 */
public class SparkSqlDemo {

  public static void main(String[] args) throws AnalysisException {
    SparkSession spark = SparkSession.builder().appName("demo").master("local[2]").getOrCreate();
    // DataFrames from an existing RDD, from a Hive table, or from Spark data sources.
    Dataset<Row> df = spark.read().json("learning-bigdata/learning-spark/src/main/resources/people.json");
    df.show();
    /*
      +----+-------+
      | age|   name|
      +----+-------+
      |null|Michael|
      |  30|   Andy|
      |  19| Justin|
      +----+-------+
     */
    // print schema info
    df.printSchema();
    // use select col
    df.select("name").show();
    // Select everybody, but increment the age by 1
    df.select(col("name"), col("age").plus(1)).show();
    // select age from t where age > 20
    df.filter(col("age").gt(20)).show();
    // Count people by age
    df.groupBy("age").count().show();

    // Register the DataFrame as a SQL temporary view
    df.createOrReplaceTempView("people");
    Dataset<Row> sqlDF = spark.sql("SELECT * FROM people");
    sqlDF.show();

    // Register the DataFrame as a global temporary view
    df.createGlobalTempView("people");
    // Global temporary view is tied to a system preserved database `global_temp`
    spark.sql("SELECT * FROM global_temp.people").show();
    // Global temporary view is cross-session
    spark.newSession().sql("SELECT * FROM global_temp.people").show();

  }
}
