package com.tomgs.core.base;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * @author tangzy
 * @since 1.0
 */
public class Processor {

  public static void main(String[] args) throws Exception {
    String hostIp = "172.20.182.49";
    String databaseName = "cq_mc";
    String userName = "cosmic";
    String password = "rZ$YCQ6s2F";
    String port = "3306";
    String tools_dir = "";

    String[] cmds = new String[]{"/bin/bash", "-c", "DB_HOST=\"" + hostIp + "\"\nDB_NAME=\"" + databaseName + "\"\nDB_USER=\"" + userName + "\"\nDB_PASS=\"" + password + "\"\nDB_PORT=\"" + port + "\"\n" + tools_dir + "mysql -u$DB_USER -P$DB_PORT -p$DB_PASS -h $DB_HOST --default-character-set=utf8 $DB_NAME <<EOF \nselect current_date();\nEOF\nexit; \n"};
    for (String cmd : cmds) {
      System.out.println(cmd);
    }
    Process process = Runtime.getRuntime().exec(cmds);

    if (process.waitFor() == 0) {
      System.out.println("success ...");
    } else {
      System.out.println("failure ...");
      Reader reader = new InputStreamReader(process.getErrorStream(), "gbk");
      BufferedReader bf = new BufferedReader(reader);
      String errorStr = "";

      for(String line = ""; (line = bf.readLine()) != null; errorStr = errorStr + line) {
      }

      throw new Exception(errorStr);
    }
  }

}
