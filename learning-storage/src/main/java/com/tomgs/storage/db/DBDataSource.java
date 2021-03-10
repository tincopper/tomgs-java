package com.tomgs.storage.db;

import com.zaxxer.hikari.HikariDataSource;

/**
 * @author tomgs
 * @since 2021/3/8
 */
public abstract class DBDataSource extends HikariDataSource {

  public abstract String getDBType();

}
