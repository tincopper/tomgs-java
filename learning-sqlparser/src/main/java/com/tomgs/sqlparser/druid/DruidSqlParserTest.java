package com.tomgs.sqlparser.druid;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLLimit;
import com.alibaba.druid.sql.ast.SQLOrderBy;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.*;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.util.JdbcConstants;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * DruidSqlParserTest
 *
 * https://github.com/alibaba/druid/wiki
 * https://github.com/alibaba/druid/wiki/SQL-Parser
 * https://blog.csdn.net/qq_25104587/article/details/90577646
 *
 * @author tomgs
 * @since 2022/1/7
 */
public class DruidSqlParserTest {

    @Test
    public void testAddSelectColumn() {
        String sql = "select name from public.user where id = 1";
        List<SQLStatement> statements = SQLUtils.parseStatements(sql, JdbcConstants.MYSQL);
        SQLSelectStatement selectStatement = (SQLSelectStatement) statements.get(0);
        SQLSelect select = selectStatement.getSelect();
        SQLSelectQueryBlock selectQuery = (SQLSelectQueryBlock) select.getQuery();
        // 获取查询字段信息
        List<SQLSelectItem> selectList = selectQuery.getSelectList();
        // 新增字段
        SQLSelectItem sqlSelectItem = new SQLSelectItem();
        sqlSelectItem.setExpr(new SQLIdentifierExpr("mail"));
        selectList.add(sqlSelectItem);

        // 获取表名信息
        SQLTableSource from = selectQuery.getFrom();
        System.out.println(from);

        System.out.println(selectStatement.toString());

        Assert.assertEquals(selectStatement.toUnformattedString(), "SELECT name, mail FROM public.user WHERE id = 1");
    }

    @Test
    public void testAddInsertColumn() {
        String sql = "INSERT INTO public.userlist (id, name) VALUES ('1', 'user01');";
        List<SQLStatement> statements = SQLUtils.parseStatements(sql, JdbcConstants.MYSQL);
        SQLInsertStatement insertStatement = (SQLInsertStatement) statements.get(0);

        // 新增列名和值
        List<SQLExpr> columns = insertStatement.getColumns();
        columns.add(new SQLIdentifierExpr("mail"));

        SQLInsertStatement.ValuesClause values = insertStatement.getValues();
        // 批量insert
        //List<SQLInsertStatement.ValuesClause> valuesList = insertStatement.getValuesList();
        //values.addValue("123@qq.com");
        // int -> SQLIntegerExpr, varchar -> SQLCharExpr
        values.addValue(new SQLCharExpr("123@qq.com"));

        System.out.println(insertStatement.toUnformattedString());
    }

    public void enhanceSql(String sql) {
        // 解析
        List<SQLStatement> statements = SQLUtils.parseStatements(sql, JdbcConstants.MYSQL);
        // 只考虑一条语句
        SQLStatement statement = statements.get(0);
        // 只考虑查询语句
        SQLSelectStatement sqlSelectStatement = (SQLSelectStatement) statement;
        SQLSelectQuery sqlSelectQuery     = sqlSelectStatement.getSelect().getQuery();
        // 非union的查询语句
        if (sqlSelectQuery instanceof SQLSelectQueryBlock) {
            SQLSelectQueryBlock sqlSelectQueryBlock = (SQLSelectQueryBlock) sqlSelectQuery;
            // 获取字段列表
            List<SQLSelectItem> selectItems         = sqlSelectQueryBlock.getSelectList();
            selectItems.forEach(x -> {
                // 处理---------------------
            });
            // 获取表
            SQLTableSource table = sqlSelectQueryBlock.getFrom();
            // 普通单表
            if (table instanceof SQLExprTableSource) {
                // 处理---------------------
                // join多表
            } else if (table instanceof SQLJoinTableSource) {
                // 处理---------------------
                // 子查询作为表
            } else if (table instanceof SQLSubqueryTableSource) {
                // 处理---------------------
            }
            // 获取where条件
            SQLExpr where = sqlSelectQueryBlock.getWhere();
            // 如果是二元表达式
            if (where instanceof SQLBinaryOpExpr) {
                SQLBinaryOpExpr   sqlBinaryOpExpr = (SQLBinaryOpExpr) where;
                SQLExpr           left            = sqlBinaryOpExpr.getLeft();
                SQLBinaryOperator operator        = sqlBinaryOpExpr.getOperator();
                SQLExpr           right           = sqlBinaryOpExpr.getRight();
                // 处理---------------------
                // 如果是子查询
            } else if (where instanceof SQLInSubQueryExpr) {
                SQLInSubQueryExpr sqlInSubQueryExpr = (SQLInSubQueryExpr) where;
                // 处理---------------------
            }
            // 获取分组
            SQLSelectGroupByClause groupBy = sqlSelectQueryBlock.getGroupBy();
            // 处理---------------------
            // 获取排序
            SQLOrderBy orderBy = sqlSelectQueryBlock.getOrderBy();
            // 处理---------------------
            // 获取分页
            SQLLimit limit = sqlSelectQueryBlock.getLimit();
            // 处理---------------------
            // union的查询语句
        } else if (sqlSelectQuery instanceof SQLUnionQuery) {
            // 处理---------------------
        }
    }
}
