package com.tomgs.learning.apaas.core;

import cn.hutool.db.Page;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLLimit;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.expr.SQLNumberExpr;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGSelectQueryBlock;
import com.tomgs.learning.apaas.model.DynamicObject;
import com.tomgs.learning.apaas.model.FieldObject;

/**
 * Data Relation Mapping
 *
 * @author tomgs
 * @since 1.0
 */
public class DataRelationMapping {

    public String getDataRelationMapping(DynamicObject dynObj, Page page) {
        switch (dynObj.getDataBaseType()) {
            case PGSQL:
                final SQLSelectQueryBlock pgQueryBlock = new PGSelectQueryBlock();
                return getSelectSql(dynObj, pgQueryBlock, page);
            case MYSQL:
                final SQLSelectQueryBlock mysqlQueryBlock = new MySqlSelectQueryBlock();
                return getSelectSql(dynObj, mysqlQueryBlock, page);
            case REDIS:
            default:
                throw new UnsupportedOperationException("not supported redis operation.");
        }
    }

    private static String getSelectSql(DynamicObject dynObj, SQLSelectQueryBlock queryBlock, Page page) {
        SQLSelect sqlSelect = new SQLSelect();
        //列名
        final String tableName = dynObj.getName();
        for (FieldObject field : dynObj.getFields()) {
            final String fieldName = field.getName();
            queryBlock.addSelectItem(new SQLIdentifierExpr(fieldName));
        }
        queryBlock.setFrom(new SQLExprTableSource(new SQLIdentifierExpr(tableName)));
        //queryBlock.setWhere();
        //待分页
        if (page != null) {
            queryBlock.setLimit(new SQLLimit(new SQLNumberExpr(page.getPageNumber()), new SQLNumberExpr(page.getPageSize())));
        }
        sqlSelect.setQuery(queryBlock);

        return SQLUtils.toSQLString(sqlSelect);
    }

    public void deleteDataRelationMapping(DynamicObject dynObj) {

    }

    public void putDataRelationMapping(DynamicObject dynObj) {

    }

    public void updateDataRelationMapping(DynamicObject dynObj) {

    }

}
