package com.tomgs.storage.db;

/**
 * PgsqlDataSource
 *
 * @author tomgs
 * @since 2021/9/7
 */
public class PgsqlDataSource extends DBDataSource {

    @Override
    public String getDBType() {
        return "pgsql";
    }

    public PgsqlDataSource() {
        super();
        final String url = "jdbc:postgresql://localhost:5432/test";
        setDataSourceClassName("org.postgresql.ds.PGSimpleDataSource");
        addDataSourceProperty("URL", url);
        setUsername("root");
        setPassword("test");
        setMinimumIdle(4);
        setMaximumPoolSize(16);
        setAutoCommit(false);
    }

}
