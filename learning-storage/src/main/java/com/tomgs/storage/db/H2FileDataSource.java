package com.tomgs.storage.db;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * h2 file datasource
 *
 * @author tomgs
 * @since 2021/3/8
 */
public class H2FileDataSource extends DBDataSource {

  @Override
  public String getDBType() {
    return "h2-file";
  }

  public H2FileDataSource() {
    super();
    final String filePath = System.getProperty("h2.path", "./h2/test");
    final Path h2DbPath = Paths.get(filePath).toAbsolutePath();
    final String url = "jdbc:h2:file:" + h2DbPath + ";IGNORECASE=TRUE";
    setDataSourceClassName("org.h2.jdbcx.JdbcDataSource");
    // jdbc:h2:[file:][<path>]<databaseName>
    addDataSourceProperty("URL", url);
    setMinimumIdle(4);
    setMaximumPoolSize(16);
  }

}
