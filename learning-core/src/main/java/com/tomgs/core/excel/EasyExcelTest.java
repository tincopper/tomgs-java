package com.tomgs.core.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.util.DateUtils;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.WriteTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tomgs.core.util.JSONUtils;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;

/**
 * @author tangzy
 * @since 1.0
 */
public class EasyExcelTest {

  /**
   * 每行数据是List<String>无表头
   */
  @Test
  public void testEasyExcel() throws IOException {
    try (OutputStream out = new FileOutputStream("withoutHead.xlsx");) {
      ExcelWriter writer = new ExcelWriterBuilder().file(out).excelType(ExcelTypeEnum.XLSX).needHead(false).build();
      WriteSheet sheet1 = new WriteSheet();
      sheet1.setSheetNo(1);
      sheet1.setSheetName("sheet1");
      sheet1.setRelativeHeadRowIndex(0);

      List<List<String>> data = new ArrayList<>();
      for (int i = 0; i < 100; i++) {
        List<String> item = new ArrayList<>();
        item.add("item0" + i);
        item.add("item1" + i);
        item.add("item2" + i);
        data.add(item);
      }
      writer.write(data, sheet1);
      writer.finish();
    }
  }

  /**
   * 每行数据是List<String>无表头
   */
  @Test
  public void writeHasHead() throws IOException {
    try (OutputStream out = new FileOutputStream("withHead.xlsx");) {
      ExcelWriter writer = new ExcelWriterBuilder().file(out).excelType(ExcelTypeEnum.XLSX).needHead(true).build();
      WriteSheet sheet1 = new WriteSheet();
      sheet1.setSheetNo(1);
      sheet1.setSheetName("sheet1");
      sheet1.setRelativeHeadRowIndex(0);

      List<List<String>> data = new ArrayList<>();
      for (int i = 0; i < 100; i++) {
        List<String> item = new ArrayList<>();
        item.add("item0" + i);
        item.add("item1" + i);
        item.add("item2" + i);
        data.add(item);
      }
      List<List<String>> head = new ArrayList<List<String>>();
      List<String> headCoulumn1 = new ArrayList<String>();
      List<String> headCoulumn2 = new ArrayList<String>();
      List<String> headCoulumn3 = new ArrayList<String>();
      headCoulumn1.add("第一列");
      headCoulumn2.add("第二列");
      headCoulumn3.add("第三列");
      head.add(headCoulumn1);
      head.add(headCoulumn2);
      head.add(headCoulumn3);
      WriteTable table = new WriteTable();
      table.setTableNo(1);
      table.setHead(head);
      writer.write(data, sheet1, table);
      writer.finish();
    }
  }

  /**
   * 根据提供的数据明细字段快速生成实体类
   * @throws IOException exception
   */
  @Test
  public void testReadExcel() throws IOException {
    // 读取 excel 表格的路径
    String readPath = "src/test/java/com/tomgs/data/common/utils/tmp.xlsx";
    List<Object> objects = EasyExcel.read(new FileInputStream(readPath)).sheet("Sheet1").doReadSync();
    System.out.println(objects);
    StringBuilder sb = new StringBuilder();
    sb.append("@Data\n"
        + "public class ExponentDataDetailEntity {\n");
    for (int i = 0; i < objects.size(); i++) {
      LinkedHashMap<Integer, String> map = (LinkedHashMap<Integer, String>) objects.get(i);
      String name = map.get(0);
      String type = map.get(1);
      String fieldName = map.get(2);
      sb.append("   @ExcelProperty(value = \"").append(name).append("\", index = ").append(i)
          .append(")\n").append("   private ").append(type).append(" ").append(fieldName)
          .append(";\n");
    }
    sb.append("\n}");
    System.out.println(sb.toString());
    FileOutputStream outputStream = new FileOutputStream("ExponentDataDetailEntity.java");
    outputStream.write(sb.toString().getBytes(StandardCharsets.UTF_8));
    outputStream.close();
  }

  /**
   * 将excel内容转为sql server建表sql文件
   */
  @Test
  public void testReadExcel2SqlServerScriptFile() throws IOException {

    Map<String, String> dataType = Maps.newHashMap();
    dataType.put("string", "nvarchar(200)");
    dataType.put("double", "decimal(18,2)");
    dataType.put("bigint", "bigint");

    // -- 这里进行修改
    String schema = "dbo";
    String tableName = "t_data_exponent";
    String tableDesc = "企业指数汇总表";

    //String schema = "dbo";
    //String tableName = "t_data_corp_detail_download";
    //String tableDesc = "工信部版下载明细的excel模式表";

    //String schema = "dbo";
    //String tableName = "t_data_corp_detail";
    //String tableDesc = "企业指数明细表";

    // 读取 excel 表格的路径
    String readPath = "src/test/java/com/tomgs/data/common/utils/create_table.xls";
    List<Map<Integer, String>> objects = EasyExcel.read(new FileInputStream(readPath)).sheet("Sheet1").doReadSync();
    // 注释
    StringBuilder comments = new StringBuilder();
    // 表结构
    StringBuilder sql = new StringBuilder();
    // EXECUTE sp_addextendedproperty N'MS_Description', '实时出行量统计数据表(real-time-travel_statistical)', N'user', N'dbo', N'table', N'rt_travel_statistical', NULL, NULL
    comments.append("\nEXEC sp_addextendedproperty\n")
        .append("'MS_Description', N'").append(tableDesc).append("',\n")
        .append("'SCHEMA', N'").append(schema).append("',\n")
        .append("'TABLE', N'").append(tableName).append("'\n")
        .append("GO\n");

    sql.append("CREATE TABLE ").append(schema).append(".").append(tableName).append("(").append("\n");
        //.append("    id int  IDENTITY(1,1) NOT NULL,").append("\n");
    for (int i = 0; i < objects.size(); i++) {
      Map<Integer, String> map = objects.get(i);
      String fieldName = map.get(0);
      String type = map.get(1);
      String comment = map.get(2);
      comments.append("\nEXEC sp_addextendedproperty\n")
          .append("'MS_Description', N'").append(comment).append("',\n")
          .append("'SCHEMA', N'").append(schema).append("',\n")
          .append("'TABLE', N'").append(tableName).append("',\n")
          .append("'COLUMN', N'").append(fieldName).append("'\n").append("GO\n");
      sql.append("    ").append(fieldName).append(" ").append(dataType.get(type)).append(" ").append("DEFAULT NULL");
      if (i < objects.size() - 1) {
        sql.append(",\n");
      } else {
        sql.append("\n) \nGO\n");
      }
    }
    String yyyyMMdd = DateUtils.format(new Date(), "yyyyMMdd");
    FileOutputStream outputStream = new FileOutputStream("sql/sqlserver/" + schema + "." + tableName + "@" + yyyyMMdd + ".sql");
    outputStream.write(sql.toString().getBytes(StandardCharsets.UTF_8));
    outputStream.write(comments.toString().getBytes(StandardCharsets.UTF_8));
    outputStream.close();
  }

  /**
   * 将excel内容转为mysql建表sql文件
   */
  @Test
  public void testReadExcel2MySqlScriptFile() throws IOException {

    Map<String, String> dataType = Maps.newHashMap();
    dataType.put("string", "varchar(200)");
    dataType.put("double", "decimal(18,2)");
    dataType.put("bigint", "int(32)");

    List<String> tableNameList = Lists.newArrayList();
    tableNameList.add("t_data_exponent");
    tableNameList.add("t_data_corp_detail_download");
    tableNameList.add("t_data_corp_detail");

    List<String> tableDescList = Lists.newArrayList();
    tableDescList.add("企业指数汇总表");
    tableDescList.add("工信部版下载明细的excel模式表");
    tableDescList.add("企业指数明细表");

    // -- 这里进行修改
    String schema = "kd_exponent";
    for (int sheetNo = 0; sheetNo < 3; sheetNo++) {
      String tableName = tableNameList.get(sheetNo);
      String tableDesc = tableDescList.get(sheetNo);

      // 读取 excel 表格的路径
      String readPath = "src/test/java/com/tomgs/data/common/utils/create_table.xls";
      List<Map<Integer, String>> objects = EasyExcel.read(new FileInputStream(readPath)).sheet(sheetNo).doReadSync();
      // 表结构
      StringBuilder sql = new StringBuilder();

      sql.append("CREATE TABLE ").append(schema).append(".").append(tableName).append("(").append("\n");
      //.append("    id int  IDENTITY(1,1) NOT NULL,").append("\n");
      for (int i = 0; i < objects.size(); i++) {
        Map<Integer, String> map = objects.get(i);
        String fieldName = map.get(0);
        String type = map.get(1);
        String comment = map.get(2);
        sql.append("\t").append(fieldName).append(" ").append(dataType.get(type)).append(" ").append("COMMENT '").append(comment).append("'");
        if (i < objects.size() - 1) {
          sql.append(",\n");
        } else {
          sql.append("\n");
          sql.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='").append(tableDesc).append("';");
        }
      }
      String yyyyMMdd = DateUtils.format(new Date(), "yyyyMMdd");
      FileOutputStream outputStream = new FileOutputStream("sql/mysql/" + schema + "." + tableName + "@" + yyyyMMdd + ".sql");
      outputStream.write(sql.toString().getBytes(StandardCharsets.UTF_8));
      outputStream.close();
      System.out.println("------success------");
    }

  }

  @Test
  public void testReadExcelToJson() {
    InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("tmp.xlsx");
    int sheetNo = 0;
    List<Map<String, Object>> result = Lists.newArrayList();
    List<Map<Integer, String>> objects = EasyExcel.read(resourceAsStream).sheet(sheetNo).doReadSync();
    for (Map<Integer, String> map : objects) {
      String cloudId = map.get(0);
      String cloudName = map.get(1);
      String appId = map.get(2);
      String appCode = map.get(3);
      String appName = map.get(4);

      for (Map<String, Object> singleObjectMap : result) {
        String id = (String)singleObjectMap.get("cloudId");
        if (id.equals(cloudId)) {
          List<Object> appList = singleObjectMap.get("appList") == null
              ? Lists.newArrayList() : (List<Object>) singleObjectMap.get("appList");
          Map<String, Object> appMap = Maps.newHashMap();
          appMap.put("appId", appId);
          appMap.put("appCode", appCode);
          appMap.put("appName", appName);
          appList.add(appMap);
        }

      }

      Map<String, Object> single = Maps.newHashMap();
      single.putIfAbsent("cloudId", cloudId);
      single.putIfAbsent("cloudName", cloudName);

      List<Object> appList = single.get("appList") == null
          ? Lists.newArrayList() : (List<Object>) single.get("appList");

      Map<String, Object> appMap = Maps.newHashMap();
      appMap.put("appId", appId);
      appMap.put("appCode", appCode);
      appMap.put("appName", appName);
      appList.add(appMap);
      single.put("appList", appList);

      result.add(single);
    }

    String jsonStr = JSONUtils.beanToJson(result);
    System.out.println(jsonStr);

  }

}
