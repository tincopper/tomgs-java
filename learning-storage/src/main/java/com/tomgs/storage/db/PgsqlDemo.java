package com.tomgs.storage.db;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author tomgs
 * @since 2021/9/7
 */
public class PgsqlDemo {

    private final PgsqlDataSource dataSource = new PgsqlDataSource();

    @Test
    public void testQuery() throws SQLException {
        DBOperator operator = new DBOperator(dataSource);

        String sql = "select * from test";
        List<Map<String, Object>> query = operator.query(sql, new MapListHandler(), 1);
        System.out.println(query);
    }

    @Test
    public void testTransaction() throws SQLException {
        MapListHandler mapListHandler = new MapListHandler();
        String sql = "select * from test where name = $1";

        QueryRunner queryRunner = new QueryRunner(dataSource);
        DataSource dataSource = queryRunner.getDataSource();
        Connection connection = dataSource.getConnection();
        connection.setAutoCommit(false);

        PreparedStatement statement = connection.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        statement.setQueryTimeout(300);
        statement.setFetchDirection(ResultSet.FETCH_FORWARD);
        statement.setFetchSize(100);
        statement.setInt(1, 1);

        statement.execute();
        statement.execute();

        ResultSet resultSet = statement.getResultSet();
        List<Map<String, Object>> result = mapListHandler.handle(resultSet);

        System.out.println(result);

        connection.rollback();
        connection.rollback();

        connection.close();
        connection.close();
    }

    @Test
    public void testJdbc() throws ClassNotFoundException, SQLException, InterruptedException {
        Class.forName("org.postgresql.Driver");
        String url = "jdbc:postgresql://localhost:5432/test";
        Connection conn = DriverManager.getConnection(url, "root", "test");
        CountDownLatch latch = new CountDownLatch(1);
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        conn.setAutoCommit(false);
        for (int i = 1; i <= 5; i++) {
            executorService.submit(() -> {
                try {
                    latch.await();
                    threadOp(conn, "select ?");
                } catch (SQLException | InterruptedException exception) {
                    exception.printStackTrace();
                }
            });
        }
        for (int i = 1; i <= 5; i++) {
            executorService.submit(() -> {
                try {
                    latch.await();
                    threadOp(conn, "select * from test WHERE name = ?");
                } catch (SQLException | InterruptedException exception) {
                    exception.printStackTrace();
                }
            });
        }
        //conn.close();

        Thread.sleep(2000);
        latch.countDown();

        Thread.sleep(5000);
        conn.commit();
        conn.close();
    }

    private void threadOp(Connection conn, String sql) throws SQLException {
        PreparedStatement pstmt = conn.prepareStatement(sql);
        // cast to the pg extension interface
        org.postgresql.PGStatement pgstmt = pstmt.unwrap(org.postgresql.PGStatement.class);

        // on the third execution start using server side statements
        pgstmt.setPrepareThreshold(1);

        for (int i = 1; i <= 5; i++) {
            pstmt.setInt(1, i);
            boolean usingServerPrepare = pgstmt.isUseServerPrepare();
            ResultSet rs = pstmt.executeQuery();
            try {
                rs.next();
                System.out.println("Execution: " + i + ", Used server side: " + usingServerPrepare + ", Result: " + rs.getInt(1 ));
            } catch (Exception e) {
                e.printStackTrace();
                conn.rollback();
            }
            rs.close();
        }
        // pstmt.close();
    }

    @Test
    public void test() {
        int flags = 9;
        int i = flags & 1; // flags奇数结果为1，偶数结果为0
        System.out.println(i);
    }

}
