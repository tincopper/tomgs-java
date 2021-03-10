package com.tomgs.storage.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.SQLException;
import org.apache.commons.dbutils.QueryRunner;

/**
 * @author tomgs
 * @since 2021/3/8
 */
public class Demo1 {

  public static void main(String[] args) throws SQLException {
    //h2 org.h2.jdbcx.JdbcDataSource
    //HikariConfig config = new HikariConfig();
    //config.setJdbcUrl("jdbc:h2:mem:testdb");
    //config.setUsername("bart");
    //config.setPassword("51mp50n");
    //config.addDataSourceProperty("cachePrepStmts", "true");
    //config.addDataSourceProperty("prepStmtCacheSize", "250");
    //config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

    HikariConfig config = new HikariConfig();
    config.setDataSourceClassName("org.h2.jdbcx.JdbcDataSource");
    config.setConnectionTestQuery("VALUES 1");
    config.addDataSourceProperty("URL", "jdbc:h2:~/test");
    config.addDataSourceProperty("user", "sa");
    config.addDataSourceProperty("password", "sa");

    HikariDataSource ds = new HikariDataSource(config);

    QueryRunner runner = new QueryRunner(ds);
    //runner.query("select 1 from dual;");

    String sql =  "CREATE TABLE REGISTRATION " +
        "(id INTEGER not NULL, " +
        " first VARCHAR(255), " +
        " last VARCHAR(255), " +
        " age INTEGER, " +
        " PRIMARY KEY ( id ))";

    //int update = runner.update(sql);
    //System.out.println(update);

    DBOperator operator = new DBOperator(ds);
    operator.createTableIfNeeded("REGISTRATION", sql);
  }

}
