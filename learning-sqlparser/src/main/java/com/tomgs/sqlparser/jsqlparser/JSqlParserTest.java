package com.tomgs.sqlparser.jsqlparser;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.*;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.util.SelectUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * JSqlParserTest
 *
 * https://github.com/JSQLParser/JSqlParser
 * https://ixyzero.com/blog/archives/4806.html
 *
 * @author tomgs
 * @since 2022/1/7
 */
public class JSqlParserTest {

    /**
     * https://github.com/JSQLParser/JSqlParser/wiki/Examples-of-SQL-building
     * @throws JSQLParserException  parser exception
     */
    @Test
    public void testAddSelectColumn() throws JSQLParserException {
        Select select = (Select) CCJSqlParserUtil.parse("select name from user where id = 1");
        SelectUtils.addExpression(select, new Column("mail"));
        Assert.assertEquals(select.toString(), "SELECT name, mail FROM user WHERE id = 1");
    }

    /**
     * 测试查询语句增加where条件
     */
    @Test
    public void testAddWhereCondition() throws Exception {
        Select select = (Select) CCJSqlParserUtil.parse("select name from user");
        PlainSelect plainSelect = (PlainSelect) select.getSelectBody();
        if (plainSelect.getWhere() == null) {
            EqualsTo equalsTo = new EqualsTo();
            equalsTo.setLeftExpression(new Column("id"));
            equalsTo.setRightExpression(new LongValue(1000L));
            plainSelect.setWhere(equalsTo);
        }
        Assert.assertEquals(select.toString(), "SELECT name FROM user WHERE id = 1000");
    }

    /**
     * 测试增加where查询条件
     */
    @Test
    public void testAddCondition() throws Exception {
        Select select = (Select) CCJSqlParserUtil.parse("select name from user where id = 1000");
        PlainSelect plainSelect = (PlainSelect) select.getSelectBody();

        // 原where表达式
        Expression where = plainSelect.getWhere();
        // 新增的查询条件表达式
        EqualsTo equalsTo = new EqualsTo();
        equalsTo.setLeftExpression(new Column("name"));
        equalsTo.setRightExpression(new StringValue("'张三'"));
        // 用and链接条件
        AndExpression and = new AndExpression(where, equalsTo);
        // 设置新的where条件
        plainSelect.setWhere(and);

        Assert.assertEquals(select.toString(), "SELECT name FROM user WHERE id = 1000 AND name = '张三'");
    }

    /**
     * 测试null条件
     */
    @Test
    public void testNullCondition() throws Exception {
        Select select = (Select) CCJSqlParserUtil.parse("select name from user where id = 1000");
        PlainSelect plainSelect = (PlainSelect) select.getSelectBody();

        // 原where表达式
        Expression where = plainSelect.getWhere();
        // 新增的null判断条件
        IsNullExpression isNullExpression = new IsNullExpression();
        isNullExpression.setLeftExpression(new Column("name"));
        isNullExpression.setNot(true);
        // 用and链接条件
        AndExpression and = new AndExpression(where, isNullExpression);
        // 设置新的where条件
        plainSelect.setWhere(and);

        Assert.assertEquals(select.toString(), "SELECT name FROM user WHERE id = 1000 AND name IS NOT NULL");
    }

    /**
     * https://github.com/JSQLParser/JSqlParser/wiki/Examples-of-SQL-building#extending-a-simple-insert
     *
     * @throws JSQLParserException parser exception
     */
    @Test
    public void testAddInsertColumn() throws JSQLParserException {
        Insert insert = (Insert) CCJSqlParserUtil.parse("INSERT INTO public.userlist (id, name) VALUES ('1', 'user01');");
        System.out.println(insert.toString());

        //adding a column
        insert.getColumns().add(new Column("col2"));
        System.out.println(insert.toString());

        //adding a value using a visitor
        insert.getItemsList().accept(new ItemsListVisitor() {

            public void visit(SubSelect subSelect) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void visit(ExpressionList expressionList) {
                expressionList.getExpressions().add(new LongValue(5));
            }

            @Override
            public void visit(NamedExpressionList namedExpressionList) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void visit(MultiExpressionList multiExprList) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        System.out.println(insert.toString());

        //adding another column
        insert.getColumns().add(new Column("col3"));
        //adding another value (the easy way)
        ((ExpressionList)insert.getItemsList()).getExpressions().add(new LongValue(10));
        System.out.println(insert.toString());
    }

}
