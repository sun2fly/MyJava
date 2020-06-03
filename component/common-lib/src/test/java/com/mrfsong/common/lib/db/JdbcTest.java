package com.mrfsong.common.lib.db;

import com.mrfsong.common.lib.util.Printer;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

/**
 * <p>
 *      jdbc单元测试
 * </p>
 *
 * @Author: Felix
 * @Created: 2020/05/27 15:39
 */
@Slf4j
public class JdbcTest {

    private static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    Properties dbProperties;

    @Before
    public void prepare() {
        try(InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties");){
            dbProperties = new Properties();
            dbProperties.load(resourceAsStream);
        }catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @After
    public void destory() {}

    @Test
    public void query() {

        Connection conn = null;
        ResultSet resultSet = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dcomb","root","root");
            //获取表索引（非主键）
            PreparedStatement preparedStatement = conn.prepareStatement("select * from index_test");
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                Timestamp createTime = resultSet.getTimestamp("create_time");
                Timestamp updateTime = resultSet.getTimestamp("update_time");

                String create_time_ymd = resultSet.getString("create_time");
                String update_time_ymd = resultSet.getString("update_time");

                boolean status = resultSet.getBoolean("status");
                byte sex = resultSet.getByte("sex");
                log.info("ResultSet : {}" , resultSet.toString());
            }

            LocalDateTime localDateTime = LocalDateTime.now();
            log.info("Now: {}",localDateTime.format(DATE_FORMATTER));
        }catch (Exception e) {
            Assert.fail(Printer.getException(e));
        } finally {
            if(resultSet != null) {
                try {
                    resultSet.close();
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Test
    public void showMagic() {

        String sql = "select table_rows from information_schema.tables where table_name='index_test' and table_schema='dcomb'";
        Connection conn = null;
        ResultSet resultSet = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dcomb","repl","mysql57");
            //获取表索引（非主键）
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                long aLong = resultSet.getLong(1);
                log.info("Table count : {}" , aLong);
            }

        }catch (Exception e) {
            Assert.fail(Printer.getException(e));
        } finally {
            if(resultSet != null) {
                try {
                    resultSet.close();
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Test
    public void example() {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dcomb","root","root");

            DatabaseMetaData meta =  conn.getMetaData();
            String dbVendorName = meta.getDatabaseProductName();
            String dbVersion = meta.getDatabaseProductVersion();
            log.info("dbVendorName:" + dbVendorName + ", dbVersion:" +dbVersion);

            log.warn("==================== Show Table Schemas ====================");
            ResultSet rsSchema = meta.getSchemas();
            String dbSchema = null;
            while(rsSchema.next()) {
                //TABLE_SCHEM
                String tableSchema = rsSchema.getString(1);
                dbSchema = tableSchema;
                //TABLE_CATALOG
                String tableCatalog = rsSchema.getString(2);
                log.info("tableSchema:" + tableSchema + ", tableCatalog:" + tableCatalog );
            }

            //ResultSet rs = meta.getTables(null, "", null, null);
            log.warn("==================== Show Table Catalog ====================");
            ResultSet rs = meta.getTables(null, dbSchema, "source", null);
            while(rs.next()) {
                //TABLE_CAT
                String tableCatalog = rs.getString(1);
                //TABLE_SCHEM
                String tableSchema = rs.getString(2);
                //table name
                String tableName = rs.getString(3);
                //table type
                String tableType = rs.getString(4);

                //TYPE_CAT
                String typeCatalog = rs.getString(5);

                //TYPE_NAME
                String typeName = rs.getString(5);

                log.info("tableCatalog:" + tableCatalog + ", tableSchema:" + tableSchema +", tableName:" + tableName
                        + ", tableType:" +tableType
                        + ", typeCatalog:" + typeCatalog
                        + ", typeName:" + typeName
                        + ", tableType:" +tableType);


                log.warn("==================== Show Table Primary Key Columns ====================");
                ResultSet rsPrimaryKeys = meta.getPrimaryKeys(null, "", tableName);
                while(rsPrimaryKeys.next()) {
                    // COLUMN_NAME
                    String keyColName = rsPrimaryKeys.getString(4);
                    //PK_NAME String
                    String pkName = rsPrimaryKeys.getString(6);
                    log.info("PrimaryKeyColumn:" + keyColName + ", primaryKeyName:" + pkName );
                }

                log.warn("==================== Show Table Columns ====================");
                ResultSet rsColumns = meta.getColumns(null, "", tableName, null);
                while(rsColumns.next()) {
                    // COLUMN_NAME
                    String columnName = rsColumns.getString(4);
                    // DATA_TYPE
                    int colType = rsColumns.getInt(5);
                    //TYPE_NAME String
                    String colTypeName = rsColumns.getString(6);
                    // COLUMN_SIZE int => column size.
                    int colSize = rsColumns.getInt(7);
                    // IS_NULLABLE
                    String isNullAble = rsColumns.getString(18);
                    // COLUMN_DEF
                    String columnDef = rsColumns.getString(13);

                    // IS_AUTOINCREMENT
                    String isAutoIncrement = rsColumns.getString(22);

                    log.info("columnName:" + columnName + ", colTypeName:" + colTypeName +", colSize:" + colSize
                            + ", isNullAble:" +isNullAble
                            + ", columnDef:" + columnDef
                            + ", isAutoIncrement:" + isAutoIncrement);
                }


                log.warn("==================== Show Table Imported Keys Columns ====================");
                ResultSet rsImportedKeys = meta.getImportedKeys(null, dbSchema, tableName);

                log.info("ImportedKeys for table '" + tableName + "'");
                while(rsImportedKeys.next()){
                    // COLUMN_NAME
                    String pkTableName = rsImportedKeys.getString("PKTABLE_NAME");
                    String pkColumnName = rsImportedKeys.getString("PKCOLUMN_NAME");
                    String fkTableName = rsImportedKeys.getString("FKTABLE_NAME");
                    String fkColumnName = rsImportedKeys.getString("FKCOLUMN_NAME");
                    String fkName = rsImportedKeys.getString("FK_NAME");
                    log.info("    pkTableName:" + pkTableName + ", pkColumnName:" + pkColumnName +", fkTableName:" + fkTableName
                            + ", fkColumnName:" +fkColumnName
                            + ", fkName:" + fkName);
                    log.info("");
                }



                log.warn("==================== Show Table Exported Keys Columns ====================");
                ResultSet rsExportedKeys = meta.getExportedKeys(null, dbSchema, tableName);
                while(rsExportedKeys.next()) {
                    // COLUMN_NAME
                    String pkTableName = rsExportedKeys.getString("PKTABLE_NAME");
                    String pkColumnName = rsExportedKeys.getString("PKCOLUMN_NAME");
                    String fkTableName = rsExportedKeys.getString("FKTABLE_NAME");
                    String fkColumnName = rsExportedKeys.getString("FKCOLUMN_NAME");
                    String fkName = rsExportedKeys.getString("FK_NAME");
                    log.info("pkTableName:" + pkTableName + ", pkColumnName:" + pkColumnName +", fkTableName:" + fkTableName
                            + ", fkColumnName:" +fkColumnName
                            + ", fkName:" + fkName);
                }
            }



        }catch(Exception e){
            Assert.fail(Printer.getException(e));
        }
    }

    @Test
    public void showDatabaseMetaData() {
        Connection conn = null;
        ResultSet resultSet = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dcomb","root","root");
            DatabaseMetaData meta =  conn.getMetaData();
            //获取表索引（非主键）
            resultSet = meta.getIndexInfo("dcomb", "dcomb", "index_test", false, false);
            while(resultSet.next()) {

                String tableCat = resultSet.getString("TABLE_CAT");
                String tableSchem = resultSet.getString("TABLE_SCHEM");
                String tableName = resultSet.getString("TABLE_NAME");

                //是否非唯一索引：Fasle 表示该字段为唯一索引
                boolean isNotUnique = resultSet.getBoolean("NON_UNIQUE");
                String indexQualifier = resultSet.getString("INDEX_QUALIFIER");
                String indexName = resultSet.getString("INDEX_NAME");

                /**
                 * 索引类型
                 * 0：tableIndexStatistic - this identifies table statistics that are returned in conjuction with a table's index descriptions
                 * 1：tableIndexClustered - this is a clustered index        ===> 聚集索引
                 * 2：tableIndexHashed - this is a hashed index              ===> Hash索引
                 * 3：tableIndexOther - this is some other style of index    ===> B+树索引
                 */
                short indexType = resultSet.getShort("TYPE");

                //该索引字段在索引中的位置（组合索引情况下有用，可以判断前缀、后缀索引）
                int ordinalPosition = resultSet.getInt("ORDINAL_POSITION");
                String columnName = resultSet.getString("COLUMN_NAME");

                // column sort sequence, "A" => ascending, "D" => descending, may be null if sort sequence is not supported; null when TYPE is tableIndexStatistic
                String ascOrDesc = resultSet.getString("ASC_OR_DESC");

                //When TYPE is tableIndexStatistic, then this is the number of rows in the table; otherwise, it is the number of unique values in the index.
                String cardinality = resultSet.getString("CARDINALITY");

                //When TYPE is tableIndexStatisic then this is the number of pages used for the table, otherwise it is the number of pages used for the current index.
                String pages = resultSet.getString("PAGES");

                //When TYPE is tableIndexStatisic then this is the number of pages used for the table, otherwise it is the number of pages used for the current index.
                String filterCondition = resultSet.getString("FILTER_CONDITION");

                log.info("index:{} , column:{} , type:{} , Unique:{} , ordinalPosition:{} " , indexName , columnName , indexType , !isNotUnique , ordinalPosition);
            }
        }catch (Exception e) {
            Assert.fail(Printer.getException(e));
        } finally {
            if(resultSet != null) {
                try {
                    resultSet.close();
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}