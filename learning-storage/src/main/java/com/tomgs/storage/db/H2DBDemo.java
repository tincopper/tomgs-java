package com.tomgs.storage.db;

import java.net.InetSocketAddress;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Date;
import org.junit.Test;

/**
 * @author tomgs
 * @since 2021/3/9
 */
public class H2DBDemo {

  @Test
  public void testCreateTable() throws SQLException {
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

  @Test
  public void testDML() {
    HodorJobExecution execution = new HodorJobExecution();
    execution.setRequestId("123");
    execution.setGroupName("test");
    execution.setJobName("test");
    execution.setSchedulerTag("1");
    execution.setClientHostname("123");
    execution.setClientIp("127.0.0.1");
    execution.setParameters("");
    execution.setStartTime(new Date());
    execution.setStatus(1);
    execution.setComments("testtttt");

    StringBuilder sqlBuilder = new StringBuilder();
    StringBuilder sqlBuilder1 = new StringBuilder();
    if (execution.getCompleteTime() != null) {
      sqlBuilder.append(", complete_time");
      sqlBuilder1.append(",?");
    }
    if (execution.getComments() != null) {
      sqlBuilder.append(", comments");
      sqlBuilder1.append(",?");
    }
    if (execution.getResult() != null) {
      sqlBuilder.append(", result");
      sqlBuilder1.append(",?");
    }

    // request_id, group_name, job_name, parameters, scheduler_tag, client_hostname, client_ip,"
    //        + " start_time, complete_time, status, comments, result
    //?,?,?,?,?,?,?,?,?,?,?,?
    String format = MessageFormat.format(
        "INSERT INTO hodor_job_execution (request_id, group_name, job_name, parameters, scheduler_tag, client_hostname, client_ip, start_time, status{0})"
            + " VALUES (?,?,?,?,?,?,?,?,?{1})", sqlBuilder.toString(), sqlBuilder1.toString());
    System.out.println(format);
  }

  @Test
  public void test() {
    //InetAddress inetAddress = new InetAddress();
    InetSocketAddress socketAddress = new InetSocketAddress("localhost", 8080);
    System.out.println(socketAddress.toString());
    System.out.println(socketAddress.getHostString());
    System.out.println(socketAddress.getHostName());
    System.out.println(socketAddress.getAddress().toString());
    System.out.println(socketAddress.getAddress().getHostName());
    System.out.println(socketAddress.getAddress().getHostAddress());
    int port = socketAddress.getPort();
    String hostAddress = socketAddress.getAddress().getHostAddress();
    System.out.println(hostAddress + ":" + port);

  }

}
