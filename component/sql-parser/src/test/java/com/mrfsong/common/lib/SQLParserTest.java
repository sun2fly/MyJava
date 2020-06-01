package com.mrfsong.common.lib;

import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitorAdapter;
import net.sf.jsqlparser.expression.operators.relational.ComparisonOperator;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @Author: Felix
 * @Created: 2020/05/28 18:21
 */
@Slf4j
public class SQLParserTest {

    private static final String SQL = "select wrl.id from waybill_route_link wrl left join waybill_info wi on wrl.waybill_code = wi.waybill_code where wrl.update_time >= '2020-05-01 00:00:00' and wrl.create_time >= '2020-04-05 00:00:00' and wrl.yn = 1 and wrl.waybill_code like 'JD%'group by wrl.waybill_code order by wrl.update_time desc";


    @Test
    public void test() {
        try {

            //TODO SQL语法校验
            Statement statement = CCJSqlParserUtil.parse(SQL);
            Select select = (Select)statement;

            PlainSelect plainSelect = (PlainSelect) select.getSelectBody();

            /* 解析Select Item */
            List<SelectItem> selectItems = plainSelect.getSelectItems();
            selectItems.stream().forEach(selectItem -> log.info("select item : {}",selectItem.toString()));


            /* 解析表名 */
            /*TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
            List<String> tabNameList = tablesNamesFinder.getTableList(select);
            tabNameList.stream().forEach(tabName -> log.info("Table:{}" , tabName));*/

            FromItem fromItem = plainSelect.getFromItem();
            Table table = (Table) fromItem;
            log.info("From Table : {} , Alias: {}",table.getName() , table.getAlias());

            /* 解析WHERE条件 */
            Expression where = plainSelect.getWhere();
            where.accept(new ExpressionVisitorAdapter() {

                @Override
                protected void visitBinaryExpression(BinaryExpression expr) {
                    if (expr instanceof ComparisonOperator) {
                        log.info("Cond column:[{}] , operator:[{}] , value:[{}]" , expr.getLeftExpression() , expr.getStringExpression() , expr.getRightExpression());
                    }

                    super.visitBinaryExpression(expr);
                }
            });


            /* 解析JOIN */
            List<Join> joins = plainSelect.getJoins();
            for (Join join : joins) {
                fromItem = join.getRightItem();
                table = (Table) fromItem;
                log.info("Join Table : {} , Alias: {}",table.getName() , table.getAlias());
            }


            /* 解析ORDER BY */
            List<OrderByElement> orderByElements = plainSelect.getOrderByElements();
            orderByElements.stream().forEach(orderByElement -> {
                orderByElement.accept(new OrderByVisitor() {
                    @Override
                    public void visit(OrderByElement orderBy) {
                        log.info("OrderBy : {}" , orderBy.toString());
                    }
                });
            });

            /* 解析Group BY */
            GroupByElement groupBy = plainSelect.getGroupBy();
            groupBy.accept(new GroupByVisitor() {
                @Override
                public void visit(GroupByElement groupBy) {
                    log.info("GroupBy : {}" , groupBy.toString());
                }
            });


        } catch (JSQLParserException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw,true));
            Assert.fail(sw.toString());
        }


    }



}
