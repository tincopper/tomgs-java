package com.tomgs.storage.db;

import java.sql.SQLException;

/**
 * @author tomgs
 * @since 2021/3/9
 */
public class H2DBDemo {

  public static void main(String[] args) throws SQLException {
    H2FileDataSource dataSource = new H2FileDataSource();
    DBOperator operator = new DBOperator(dataSource);

    String sql =  "CREATE TABLE REGISTRATION " +
        "(id INTEGER not NULL, " +
        " first VARCHAR(255), " +
        " last VARCHAR(255), " +
        " age INTEGER, " +
        " PRIMARY KEY ( id ))";

    operator.createTableIfNeeded("REGISTRATION", sql);
  }

}
