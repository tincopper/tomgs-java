package com.tomgs.learning.apaas.core;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.db.Page;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLLimit;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.expr.SQLNumberExpr;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGSelectQueryBlock;
import com.tomgs.learning.apaas.model.Argument;
import com.tomgs.learning.apaas.model.DynamicObject;
import com.tomgs.learning.apaas.model.FieldObject;

import java.util.List;

/**
 * Data Relation Mapping
 *
 * @author tomgs
 * @since 1.0
 */
public class DataRelationMapping {

    public String getDataRelationMapping(DynamicObject dynObj, Page page, List<Argument> arguments) {
        switch (dynObj.getDataBaseType()) {
            case PGSQL:
                final SQLSelectQueryBlock pgQueryBlock = new PGSelectQueryBlock();
                return getSelectSql(dynObj, pgQueryBlock, page, arguments);
            case MYSQL:
                final SQLSelectQueryBlock mysqlQueryBlock = new MySqlSelectQueryBlock();
                return getSelectSql(dynObj, mysqlQueryBlock, page, arguments);
            case REDIS:
            default:
                throw new UnsupportedOperationException("not supported redis operation.");
        }
    }

    public void deleteDataRelationMapping(DynamicObject dynObj) {

    }

    public void putDataRelationMapping(DynamicObject dynObj) {

    }

    public void updateDataRelationMapping(DynamicObject dynObj) {

    }

    private static String getSelectSql(DynamicObject dynObj, SQLSelectQueryBlock queryBlock, Page page, List<Argument> arguments) {
        SQLSelect sqlSelect = new SQLSelect();
        //列名
        final String tableName = dynObj.getName();
        for (FieldObject field : dynObj.getFields()) {
            final String fieldName = field.getName();
            queryBlock.addSelectItem(new SQLIdentifierExpr(fieldName));
        }
        queryBlock.setFrom(new SQLExprTableSource(new SQLIdentifierExpr(tableName)));
        if (CollectionUtil.isNotEmpty(arguments)) {
            for (Argument argument : arguments) {
                final SQLExpr sqlExpr = SQLUtils.toSQLExpr(argument.toSqlString());
                queryBlock.addWhere(sqlExpr);
            }
        }
        //待分页
        if (page != null) {
            queryBlock.setLimit(new SQLLimit(new SQLNumberExpr(page.getPageNumber()), new SQLNumberExpr(page.getPageSize())));
        }
        sqlSelect.setQuery(queryBlock);

        return SQLUtils.toSQLString(sqlSelect);
    }

}
