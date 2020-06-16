package com.mrfsong.common.db;

import com.github.jsonzou.jmockdata.MockConfig;
import com.google.common.base.Stopwatch;
import com.mrfsong.common.util.DateUtil;
import com.mrfsong.common.util.Printer;
import com.mrfsong.common.util.Tuple2;
import com.mrfsong.common.util.Tuple3;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import static com.github.jsonzou.jmockdata.JMockData.mock;

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

    private static Integer BULK_SIZE = 50000;
    private static Integer QUERY_BATCH_SIZE = 1000000;
    private static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    Properties dbProperties;

    MockConfig mockConfig;

    AtomicLong counter = new AtomicLong(29665728L);

    @Before
    public void prepare() {
        try(InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties");){
            dbProperties = new Properties();
            dbProperties.load(resourceAsStream);

        }catch (Exception e) {
            Assert.fail(e.getMessage());
        }

        mockConfig = new MockConfig()
                .doubleRange(1.0d,9999.99999d)
                .floatRange(1.11111f,9999.99999f)
                .decimalScale(3) // 设置小数位数为3，默认是2
                .dateRange("2020-06-16 00:00:00","2020-06-16 00:01:00")
                .intRange(30,100)
//                .setEnabledCircle(false)
//                .stringRegex("mock-")
                .globalConfig();
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
    public void information_schema() {
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

    /**
     * TODO 如何处理Mock数据重复问题
     */
    @Test
    public void mockData(){

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
//            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dcomb?characterEncoding=UTF8&useUnicode=true&rewriteBatchedStatements=true&useCompression=true&cachePrepStmts=true&useServerPrepStmts=true","root","root");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dcomb?characterEncoding=UTF8&useUnicode=true&rewriteBatchedStatements=true&useCompression=true","root","root");
            preparedStatement = conn.prepareStatement("insert into `dcomb`.`source`(id,`name`,age,create_time) values (? , ? , ? , ?)");
            Stopwatch stopwatch = Stopwatch.createStarted();
            for(int i=0;i< 3000000;i++){
                preparedStatement.setLong(1,counter.incrementAndGet());
                preparedStatement.setString(2,getUUID());
                preparedStatement.setInt(3,mock(Integer.class,mockConfig));
                preparedStatement.setTimestamp(4,mock(Timestamp.class,mockConfig));
                preparedStatement.addBatch();

                if(i % BULK_SIZE == 0){
                    preparedStatement.executeBatch();
                    preparedStatement.clearBatch();
                }
            }

            preparedStatement.executeBatch();
            preparedStatement.clearBatch();
            log.warn("prepareStatementBatch 消耗时间:" + stopwatch);
        }catch (Exception e) {
            Assert.fail(Printer.getException(e));
        } finally {
            if(preparedStatement != null) {
                try {
                    preparedStatement.close();
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Test
    public void testResultSet() {
        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dcomb?characterEncoding=UTF8&useUnicode=true&rewriteBatchedStatements=true&useCompression=true","root","root");
            statement = conn.createStatement();
            resultSet = statement.executeQuery("select min(id),max(id) from target2_bak limit 10");
            if(resultSet.next()){
                log.warn("========= enter one =========");

                if(resultSet.next()){
                    log.warn("========= enter two =========");
                }
            }
        }catch (Exception e) {
            Assert.fail(Printer.getException(e));
        } finally {
            if(statement != null) {
                try {
                    resultSet.close();
                    statement.close();
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Deprecated
    @Test(timeout=600000)
    public void testDatePartition(){
        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;

        Long batchSize = 1000000L;
        //目前分片值保留了日期查询字段，实际情况可能会包括其他非日期索引查询字段，所以可以做以下假设：
        //1: 单日期分片数据 * rand(0 ~ 1] = batchSize
        //2: 当只存在分片日期查询条件时，该值为1；当存在其他条件时，该值 大于 1，具体应用中需要通过识别Where条件来决定取值
        double partitionScaleRatio = 1.0d;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dcomb?characterEncoding=UTF8&useUnicode=true&rewriteBatchedStatements=true&useCompression=true","root","root");
            statement = conn.createStatement();
            resultSet = statement.executeQuery("select create_time, count(*) from source where create_time >= '2020-01-01 00:00:00' and create_time < '2020-06-11 00:00:00' GROUP BY year(create_time),MONTH(create_time),DAY(create_time)");

            Map<Timestamp,Long> gCountMap = new HashMap<>();
            while(resultSet.next()){
              gCountMap.put(resultSet.getTimestamp(1),resultSet.getLong(2));

            }

            //日期排序
            ArrayList<Map.Entry<Timestamp, Long>> arrayList = new ArrayList<>(gCountMap.entrySet());
            Collections.sort(arrayList, Comparator.comparing(Map.Entry::getKey));
            arrayList.stream().forEach(timestampLongEntry -> log.info("key:{},value:{}" , timestampLongEntry.getKey() , timestampLongEntry.getValue()));

            //检测大分片
            int start = 0 ,index = 0;
            long partitionScaleSize = Math.round(batchSize * partitionScaleRatio);//分片记录放大值
            Map<Integer,Integer> indexMap = new HashMap<>();
            List<Map.Entry<Timestamp, Long>> bigPartition = new ArrayList<>();
            for(Map.Entry<Timestamp, Long> entry : arrayList){
                if(entry.getValue() > partitionScaleSize){
                    //截断 subList:[)
                    indexMap.put(start,index);
                    start = index;
                    bigPartition.add(entry);
                }
                index++;
            }


            //拆分大分片
            for(Map.Entry<Timestamp, Long> entry : bigPartition){
                //此值表示当前分片的放大系数。根据此系数可以动态的对大分片数据进行二次拆分
                //如果当前分片是按照day切分的，那么此值表示待拆分的hours"份数"
                double bigFactor = entry.getValue() / partitionScaleSize * 1.0D;
                //拆分单天
                long hourSplitTotal = Math.round(24 / bigFactor);
                LocalDateTime[] arr = getOneDayRange(entry.getKey(),false);
                Map<LocalDateTime, LocalDateTime> localDateTimeLocalDateTimeMap = partitionByDateInteval(arr[0], arr[1], hourSplitTotal, ChronoUnit.HOURS);
            }

            //合并小分片（切记要剔除大分片的时间段）
            for(Map.Entry<Integer,Integer> entry : indexMap.entrySet()){
                List<Map.Entry<Timestamp, Long>> subList = arrayList.subList(entry.getKey(),entry.getValue());
                doMergePartition(subList,partitionScaleSize);
            }

        }catch (Exception e) {
            Assert.fail(Printer.getException(e));
        } finally {
            if(statement != null) {
                try {
                    resultSet.close();
                    statement.close();
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Deprecated
    @Test(timeout=30000)
    public void testDatePartitionTwo(){

        //计时器
        Stopwatch stopwatch = Stopwatch.createStarted();

        Long batchSize = 1000000L;
        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;

        //目前分片值保留了日期查询字段，实际情况可能会包括其他非日期索引查询字段，所以可以做以下假设：
        //1: 单日期分片数据 * rand(0 ~ 1] = batchSize
        //2: 当只存在分片日期查询条件时，该值为1；当存在其他条件时，该值 大于 1，具体应用中需要通过识别Where条件来决定取值
        double partitionScaleRatio = 1.0d;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dcomb?characterEncoding=UTF8&useUnicode=true&rewriteBatchedStatements=true&useCompression=true","root","root");
            statement = conn.createStatement();

            //TODO 此处如果存在查询效率瓶颈，可以先获取min(date),max(date) ，然后拆分为天，分别统计每天的min/max/count量 （假定：单天数据量不能过大 ！！！）
            resultSet = statement.executeQuery("select min(create_time),max(create_time), count(*) from source where create_time >= '2020-01-01 00:00:00' and create_time < '2020-06-11 00:00:00' GROUP BY year(create_time),MONTH(create_time),DAY(create_time) order by create_time asc");


            //数据结构： 2N: minDate 2N+1: maxDate
            List<Timestamp> gDateList = new ArrayList<>();//分组min/max日期列表，步长为2
            //数据结构： N: count
            List<Long> gRowCountList = new ArrayList<>();
            while(resultSet.next()){
                gDateList.add(resultSet.getTimestamp(1));
                gDateList.add(resultSet.getTimestamp(2));
                gRowCountList.add(resultSet.getLong(3));
            }

            //检测大分片
            int start = 0 ,index = 0;
            long partitionScaleSize = Math.round(batchSize * partitionScaleRatio);//分片记录放大值
            Map<Integer,Integer> dateIndexMap = new HashMap<>();//"小"分片日期数据区间

            List<Integer> bigPartDateIndex = new ArrayList<>();
            for(Long cnt : gRowCountList){
                if(cnt > partitionScaleSize){
                    dateIndexMap.put(start, index);
                    start = index ;
                    bigPartDateIndex.add(index * 2);
                }
                index++;
            }

            //拆分大分片
            for(Integer idx : bigPartDateIndex){
                double bigFactor = gRowCountList.get(idx / 2) / partitionScaleSize * 1.0D;
                //拆分单天
                long hourSplitTotal = Math.round(24 / bigFactor);
                LocalDateTime[] arr = getOneDayRange(gDateList.get(idx+1),true);//max值
                Map<LocalDateTime, LocalDateTime> splitDateMap = partitionByDateInteval(arr[0], arr[1], hourSplitTotal, ChronoUnit.HOURS);
                splitDateMap.entrySet().stream().forEach(entry -> log.debug("[Bigger] start: {} , end: {}" , entry.getKey().format(DATE_FORMATTER),entry.getValue().format(DATE_FORMATTER)));

            }

            //合并小分片（切记要剔除大分片的时间段）
            for(Map.Entry<Integer,Integer> entry : dateIndexMap.entrySet()){
                List<Long> subList = gRowCountList.subList(entry.getKey(),entry.getValue());
                doMergePartition(subList,gDateList,partitionScaleSize);
            }

            log.info("处理完成，耗时 [{}]", stopwatch);

        }catch (Exception e) {
            Assert.fail(Printer.getException(e));
        } finally {
            if(statement != null) {
                try {
                    resultSet.close();
                    statement.close();
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Test(timeout=300000)
    public void testDatePartitionThree(){

        //计时器
        Stopwatch stopwatch = Stopwatch.createStarted();

        Long batchSize = 1000000L;
        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;

        //目前分片值保留了日期查询字段，实际情况可能会包括其他非日期索引查询字段，所以可以做以下假设：
        //1: 单日期分片数据 * rand(0 ~ 1] = batchSize
        //2: 当只存在分片日期查询条件时，该值为1；当存在其他查询条件时，该值大于1；
        //3: 具体应用中需要通过识别Where条件来决定取值
        double mergedStopRatio = 0.95d;         //合并小分片"分裂"系数，超过这个值会结束当前合并，开启新的小分片合并
        double partitionOverflowRatio = 1.1d;   //大分片"溢出"系数，这个值会决定是否会被识别为"大分片"
        long partitionOverflowSize = (long)Math.ceil(batchSize * partitionOverflowRatio);//分片记录放大值

        List<Tuple2<String,String>> lastSplitResult = new ArrayList<>();//最终日期分片结果

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dcomb?characterEncoding=UTF8&useUnicode=true&rewriteBatchedStatements=true&useCompression=true","root","root");
            statement = conn.createStatement();

            resultSet = statement.executeQuery("select min(create_time),max(create_time) from source where create_time >= '2020-01-01 00:00:00'");

            Timestamp minDate = null , maxDate = null;
            while(resultSet.next()){
                minDate = resultSet.getTimestamp(1);
                maxDate = resultSet.getTimestamp(2);
            }
            resultSet.close();

            //日期分片统计结果
            Map<LocalDateTime, LocalDateTime> dayPartitionMap = datePartition(DateUtil.parseTs2DateTime(minDate), DateUtil.parseTs2DateTime(maxDate), ChronoUnit.DAYS);


            /* 分片检测， 大分片识别、大分片拆分、小分片合并 */


            List<Tuple3<Timestamp,Timestamp,Long>> splitDayStatResult = partitionDetection(dayPartitionMap,conn);
//            List<Tuple3<Timestamp,Timestamp,Long>> splitDayStatResult = subDateResult(DateUtil.formatTs(minDate,DATE_FORMATTER), DateUtil.formatTs(maxDate,DATE_FORMATTER),ChronoUnit.DAYS, conn);

            //分片日期min/max查询结果 （数据结构： 2N: minDate 2N+1: maxDate）
            List<Timestamp> queryDateList = new ArrayList<>();
            //分片日期记录count条数 （数据结构： N: count）
            List<Long> rowCountList = new ArrayList<>();

            splitDayStatResult.forEach(tp -> {
                queryDateList.add(tp._1());
                queryDateList.add(tp._2());
                rowCountList.add(tp._3());
            });


            //检测大分片
            int start = 0 ,index = 0;
            Map<Integer,Integer> dateIndexMap = new HashMap<>();//"小"分片日期数据区间
            List<Integer> bigPartDateIndex = new ArrayList<>();
            for(Long cnt : rowCountList){
                if(cnt > partitionOverflowSize){
                    dateIndexMap.put(start, index);
                    start = index ;
                    bigPartDateIndex.add(index * 2);
                }
                index++;
            }

            //拆分大分片
            Map<LocalDateTime, LocalDateTime> splitDateMap = new HashMap<>();
            for(Integer idx : bigPartDateIndex){
                double bigFactor = rowCountList.get(idx / 2) / (partitionOverflowSize * 1.0D);
                //大分片时间降级 （Day -> Hours）

                long splitNum = (long)Math.ceil(24 / bigFactor);
                LocalDateTime[] arr = getOneDayRange(queryDateList.get(idx+1),true);//max值
//                Map<LocalDateTime, LocalDateTime> dayPartition = partitionByDateInteval(arr[0], arr[1], splitNum, ChronoUnit.HOURS);
                Map<LocalDateTime, LocalDateTime> dayPartition = doSplitPartition(arr[0], arr[1], splitNum, ChronoUnit.HOURS);
                splitDateMap.putAll(dayPartition);
            }

            //分片结果检测
//            List<Tuple3<Timestamp, Timestamp, Long>> splitPartitionStat = partitionDetection(splitDateMap, conn);
//            List<Tuple3<Timestamp, Timestamp, Long>> overflowPartition = splitPartitionStat.stream().filter(tp -> tp._3() > partitionOverflowSize).collect(Collectors.toList());


            //尝试小分片合并,减少分片量
            long mergedStopSize = (long)Math.ceil(batchSize * mergedStopRatio);
            List<Tuple3<Timestamp,Timestamp,Long>> mergedDateList = new ArrayList<>();
            for(Map.Entry<Integer,Integer> entry : dateIndexMap.entrySet()){
                List<Long> subList = rowCountList.subList(entry.getKey(),entry.getValue());
                mergedDateList.addAll(doMergePartition(subList,queryDateList,mergedStopSize));
            }

            mergedDateList.forEach(tuple -> {
                lastSplitResult.add(new Tuple2<>(DateUtil.formatTs(tuple._1(),DATE_FORMATTER),DateUtil.formatTs(tuple._2(),DATE_FORMATTER)));
            });

            //小分片合并后可能会新产生大分片，此处需要对合并结果进行二次检测、处理
            //小分片合并时已经设置了安全阈值【stopCount】，可以保证已有小分片合并后不会"膨胀"产生大分片
        /*    List<Tuple3<Timestamp, Timestamp, Long>> mergedBigPartitions = mergedDateList.stream().filter(tp -> (tp._3() > partitionOverflowSize)).collect(Collectors.toList());
            mergedBigPartitions.forEach(bigPartition -> {
                long splitNum = (long)Math.ceil(bigPartition._3() / (partitionOverflowSize * 1.0D));
                doSplitPartition(DateUtil.parseTs2DateTime(bigPartition._1()),DateUtil.parseTs2DateTime(bigPartition._2()),splitNum,ChronoUnit.HOURS);
            });*/


            log.info("处理完成，耗时 [{}]", stopwatch);

        }catch (Exception e) {
            Assert.fail(Printer.getException(e));
        } finally {
            if(statement != null) {
                try {
                    resultSet.close();
                    statement.close();
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test(timeout = 1000 * 60 * 10)
    public void testPartitionFour() {
        //计时器
        Stopwatch stopwatch = Stopwatch.createStarted();


        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;

        //目前分片值保留了日期查询字段，实际情况可能会包括其他非日期索引查询字段，所以可以做以下假设：
        //1: 单日期分片数据 * rand(0 ~ 1] = batchSize
        //2: 当只存在分片日期查询条件时，该值为1；当存在其他查询条件时，该值大于1；
        //3: 具体应用中需要通过识别Where条件来决定取值
        long batchSize = 1000000L;
        double mergedStopRatio = 0.95d;         //合并小分片"分裂"系数，超过这个值会结束当前合并，开启新的小分片合并
        double partitionOverflowRatio = 1.1d;   //大分片"溢出"系数，这个值会决定是否会被识别为"大分片"
        long partitionOverflowSize = (long)Math.ceil(batchSize * partitionOverflowRatio);//分片记录放大值
        long mergedStopSize = (long)Math.ceil(batchSize * mergedStopRatio);

        List<Tuple2<String,String>> lastSplitResult = new ArrayList<>();//最终日期分片结果

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dcomb?characterEncoding=UTF8&useUnicode=true&rewriteBatchedStatements=true&useCompression=true","root","root");
            statement = conn.createStatement();

            resultSet = statement.executeQuery("select min(create_time),max(create_time) from source where create_time >= '2020-01-01 00:00:00'");

            Timestamp minDate = null , maxDate = null;
            while(resultSet.next()){
                minDate = resultSet.getTimestamp(1);
                maxDate = resultSet.getTimestamp(2);
            }
            resultSet.close();

            //日期分片统计结果
            Map<LocalDateTime, LocalDateTime> dayPartitionMap = datePartition(DateUtil.parseTs2DateTime(minDate), DateUtil.parseTs2DateTime(maxDate), ChronoUnit.DAYS);
            List<Tuple3<Timestamp,Timestamp,Long>> splitDayStatResult = partitionDetection(dayPartitionMap,conn);
            /* 分片检测， 大分片识别、大分片拆分、小分片合并 */
            doMain(lastSplitResult,splitDayStatResult,partitionOverflowSize,mergedStopSize,conn);

            lastSplitResult.forEach(tp -> log.warn("最终分片结果：{}",tp.toString()));

            log.info("处理完成，耗时 [{}]", stopwatch);

        }catch (Exception e) {
            Assert.fail(Printer.getException(e));
        } finally {
            if(statement != null) {
                try {
                    resultSet.close();
                    statement.close();
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Test
    public void testMethod() {
        LocalDateTime startTime = LocalDateTime.parse("2020-06-05 23:10:35", DATE_FORMATTER);
        LocalDateTime endTime = LocalDateTime.parse("2020-06-16 01:00:00", DATE_FORMATTER);
        Map<LocalDateTime, LocalDateTime> dateTimeMap = datePartition(startTime, endTime, ChronoUnit.DAYS);
        dateTimeMap.entrySet().forEach(entry -> {
            log.info("start : {} , end: {}" , entry.getKey().format(DATE_FORMATTER) ,entry.getValue().format(DATE_FORMATTER));
        });
    }

    private String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-","").toLowerCase();
    }


    private Map<LocalDateTime,LocalDateTime> partitionByDateInteval(LocalDateTime minDateTime, LocalDateTime maxDateTime, long splitNum , ChronoUnit dateInteval ) {

        Map<LocalDateTime,LocalDateTime> splitResult = new LinkedHashMap<>();
        long differ = dateInteval.between(minDateTime, maxDateTime);//日期差值
        long step = (long)Math.ceil(differ  * 1.0d / splitNum);//步长
        LocalDateTime tmpDateTime = minDateTime;

        for(long idx = 1; idx <= splitNum;idx++){
            LocalDateTime plusDateTime = tmpDateTime.plus(step,dateInteval);
            if(idx == splitNum){
                plusDateTime = maxDateTime.plusSeconds(1L);//此处添加1s是为了兼容日期 date >= 'yyyy-MM-dd hh:MM:ss' and date < 'yyyy-MM-dd hh:MM:ss'查询逻辑
            }
            splitResult.put(tmpDateTime, plusDateTime);
//            log.debug("startTime:{} , endTime:{}",tmpDateTime.format(DATE_FORMATTER),plusDateTime.format(DATE_FORMATTER));
            tmpDateTime = plusDateTime;
        }
        return splitResult;

    }

    private Map<LocalDateTime,LocalDateTime> datePartition(LocalDateTime minDateTime, LocalDateTime maxDateTime,ChronoUnit chronoUnit) {
        Map<LocalDateTime,LocalDateTime> splitResult = new LinkedHashMap<>();
        long differ = chronoUnit.between(minDateTime, maxDateTime);
        if(differ == 0){//日期间隔未超过一个chronoUnit
            splitResult.put(minDateTime, maxDateTime);
        }else {
            LocalDateTime startTime = minDateTime;
            LocalDateTime endTime;
            for(int idx=1;idx<differ+1;idx++){
                if(idx == differ) {
                    splitResult.put(startTime,maxDateTime);
                } else {
                    endTime = minDateTime.plus(idx,chronoUnit);
                    splitResult.put(startTime,endTime);
                    startTime = endTime;
                }
            }
        }

        return splitResult;
    }

    /**
     * 获取指定日期
     * @param ts        原始日期
     * @param within    是否在当前日期范围内
     * @return
     */
    private LocalDateTime[] getOneDayRange(Timestamp ts,Boolean within){
        LocalDateTime[] array = new LocalDateTime[2];
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(ts.getTime()), ZoneId.systemDefault());
        array[0] = LocalDateTime.of(dateTime.toLocalDate(), LocalTime.MIN);
        if(within){
            array[1] = dateTime;
        }else {
            array[1] = LocalDateTime.of(dateTime.toLocalDate(), LocalTime.MAX);
        }
        return array;
    }

    @Deprecated
    private void doMergePartition(List<Map.Entry<Timestamp, Long>> dataList , Long threshold) {

        long counter = 0L;
        Timestamp startTs = dataList.get(0).getKey();
        LocalDateTime startDateTime = null ;
        LocalDateTime endDateTime = null ;

        for(Map.Entry<Timestamp, Long> entry : dataList){
            counter += entry.getValue();
            if(counter >= threshold){
                startDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(startTs.getTime()), ZoneId.systemDefault());
                endDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(entry.getKey().getTime()), ZoneId.systemDefault());
                log.warn("startTime:{} , endTime:{},counter:{}",startDateTime.format(DATE_FORMATTER),endDateTime.format(DATE_FORMATTER),counter);

                startTs = entry.getKey();
                counter = 0L;
            }
        }

        if(counter > 0L){
            endDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(dataList.get(dataList.size() - 1).getKey().getTime()), ZoneId.systemDefault());
            log.warn("startTime:{} , endTime:{} , counter:{}",startDateTime.format(DATE_FORMATTER),endDateTime.format(DATE_FORMATTER),counter);
        }

    }

    /**
     * "大日期"分片
     * TODO 目前大分片只是按照记录量取模后等分,对于短时间数据"洪峰"场景、会出现分片后数据仍然聚集在某一小分片中，需要递归检测分片后结果，直到真正的不存在大分片
     * @param startTime
     * @param endTime
     * @param splitNum
     * @param chronoUnit
     * @return
     */
    private Map<LocalDateTime, LocalDateTime> doSplitPartition(LocalDateTime startTime, LocalDateTime endTime , Long splitNum, ChronoUnit chronoUnit) {
        log.warn("Find Bigger Partition , Date start: {} , end: {}" , startTime.format(DATE_FORMATTER),endTime.format(DATE_FORMATTER));
        Map<LocalDateTime, LocalDateTime> splitDateMap = partitionByDateInteval(startTime, endTime, splitNum, chronoUnit);

        splitDateMap.entrySet().stream().forEach(entry -> log.info("Bigger Partition Result ===> Date start: {} , end: {}" , entry.getKey().format(DATE_FORMATTER),entry.getValue().format(DATE_FORMATTER)));
        return splitDateMap;
    }


    private List<Tuple3<Timestamp,Timestamp,Long>> doMergePartition(List<Long> countList , List<Timestamp> timeList , Long threshold) {

        List<Tuple3<Timestamp,Timestamp,Long>> results = new ArrayList<>();
        long counter = 0L; int position = 0;
        Timestamp startTs = timeList.get(0);
        LocalDateTime startDateTime = null ;
        LocalDateTime endDateTime = null ;

        for(Long cnt : countList){
            counter += cnt;
            if(counter >= threshold){
                startDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(startTs.getTime()), ZoneId.systemDefault());
                endDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timeList.get(position * 2 + 1).getTime()), ZoneId.systemDefault());//取max
                results.add(new Tuple3<>(startTs,timeList.get(position * 2 + 1),counter));
                log.warn("startTime:{} , endTime:{},counter:{}",startDateTime.format(DATE_FORMATTER),endDateTime.format(DATE_FORMATTER),counter);
                startTs = timeList.get((position + 1) * 2);
                counter = 0L;
            }
            position++;
        }

        if(counter > 0L){
            startDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(startTs.getTime()), ZoneId.systemDefault());
            endDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timeList.get((position-1) * 2 + 1).getTime()), ZoneId.systemDefault());//position累加器，最后值为数组元素数量，故此处需要做减1操作
            results.add(new Tuple3<>(startTs,timeList.get((position-1) * 2 + 1),counter));
            log.warn("startTime:{} , endTime:{} , counter:{}",startDateTime.format(DATE_FORMATTER),endDateTime.format(DATE_FORMATTER),counter);
        }

        return results;

    }

    /**
     * 小分片合并
     * @param countList
     * @param timeList
     * @param threshold
     * @return
     */
    private List<Tuple3<Timestamp,Timestamp,Long>> doMergePartition2(List<Long> countList , List<Timestamp> timeList , Long threshold) {

        List<Tuple3<Timestamp,Timestamp,Long>> results = new ArrayList<>();
        long counter = 0L;
        Timestamp startTs = timeList.get(0);

        //TODO 待实现！！！
        for(int position=0;position<countList.size();position++){
            long cnt = countList.get(position);
            if(cnt >= threshold){
                //如果当前时间段rowCount已经超限（mergedStopSize），直接分片
                results.add(new Tuple3<>(timeList.get(position * 2),timeList.get(position * 2 + 1),cnt));

                //判断之前是否有已合并数据
                if(counter > 0L){
                    results.add(new Tuple3<>(startTs,timeList.get(position * 2 - 1),cnt));
                }
                int nextStartIdx = (position+1) * 2;
                if(nextStartIdx < timeList.size()){
                    startTs = timeList.get(nextStartIdx);
                    counter = 0L;
                }
            } else {
                counter += cnt;
                if(counter >= threshold){
                    results.add(new Tuple3<>(startTs,timeList.get(position * 2 - 1),(counter-cnt)));
                    int nextStartIdx = position * 2;
                    if(nextStartIdx < timeList.size()){
                        startTs = timeList.get(position * 2);
                        counter = cnt;
                    }
                }
            }
        }


        if(counter > 0L){
            timeList.get(timeList.size() - 1);
            results.add(new Tuple3<>(startTs,timeList.get(timeList.size() - 1),counter));
        }

        results.forEach(tp3 -> log.debug("startTime:{} , endTime:{} , counter:{}",DateUtil.parseTs2DateTime(tp3._1()).format(DATE_FORMATTER),DateUtil.parseTs2DateTime(tp3._2()).format(DATE_FORMATTER),tp3._3()));

        return results;

    }

    /**
     * 日期查询条件拆分
     * TODO 优化点：并发查询
     * @param beginTime
     * @param endTime
     * @param conn
     * @return
     */
    private List<Tuple3<Timestamp,Timestamp,Long>> subDateResult(String beginTime , String endTime ,ChronoUnit chronoUnit , Connection conn) {
        LocalDateTime dateTimeStart = LocalDateTime.parse(beginTime, DATE_FORMATTER);
        LocalDateTime dateTimeEnd = LocalDateTime.parse(endTime, DATE_FORMATTER);

        List<Tuple3<Timestamp,Timestamp,Long>> gDateList = new ArrayList<>();
        Map<LocalDateTime, LocalDateTime> dateSplitResult = datePartition(dateTimeStart, dateTimeEnd,chronoUnit);
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String sql = "select min(create_time),max(create_time), count(*) from source where create_time >= ? and create_time < ?";
        try{
            for(Map.Entry<LocalDateTime, LocalDateTime> entry : dateSplitResult.entrySet()){
                String queryDateFrom = entry.getKey().format(DATE_FORMATTER);
                String queryDateTo = entry.getValue().format(DATE_FORMATTER);
                statement = conn.prepareStatement(sql);
                statement.setString(1,queryDateFrom);
                statement.setString(2,queryDateTo);
                resultSet = statement.executeQuery();
                Timestamp minTs ;
                Timestamp maxTs ;
                Long count;
                if(resultSet.next()){
                    minTs = resultSet.getTimestamp(1);
                    maxTs = resultSet.getTimestamp(2);
                    count = resultSet.getLong(3);
                    if(minTs != null && maxTs != null){
                        gDateList.add(new Tuple3(minTs,maxTs,count));
                    }
                }
            }
        }catch (Exception e) {
            Assert.fail(Printer.getException(e));
        }finally {
            if(statement != null) {
                try {
                    resultSet.close();
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return gDateList;
    }


    /**
     * 分片信息检测
     * @param conn
     * @return
     */
    private List<Tuple3<Timestamp,Timestamp,Long>> partitionDetection(Map<LocalDateTime , LocalDateTime> dateTimeMap , Connection conn) {

        List<Tuple3<Timestamp,Timestamp,Long>> gDateList = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String sql = "select min(create_time),max(create_time), count(*) from source where create_time >= ? and create_time <= ?";
        try{
            for(Map.Entry<LocalDateTime,LocalDateTime> entry : dateTimeMap.entrySet()){
                String queryDateFrom = entry.getKey().format(DATE_FORMATTER);
                String queryDateTo = entry.getValue().format(DATE_FORMATTER);
                statement = conn.prepareStatement(sql);
                statement.setString(1,queryDateFrom);
                statement.setString(2,queryDateTo);
                resultSet = statement.executeQuery();
                Timestamp minTs ;
                Timestamp maxTs ;
                Long count;
                if(resultSet.next()){
                    minTs = resultSet.getTimestamp(1);
                    maxTs = resultSet.getTimestamp(2);
                    count = resultSet.getLong(3);
                    if(minTs != null && maxTs != null){
                        gDateList.add(new Tuple3(minTs,maxTs,count));
                    }
                }
            }
        }catch (Exception e) {
            Assert.fail(Printer.getException(e));
        }finally {
            if(statement != null) {
                try {
                    resultSet.close();
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return gDateList;
    }


    private void doMain(List<Tuple2<String,String>> resultMap , List<Tuple3<Timestamp,Timestamp,Long>> datePartitionInfo , Long partitionOverflowSize, Long mergedStopSize, Connection conn) {

        //分片日期min/max查询结果 （数据结构： 2N: minDate 2N+1: maxDate）
        List<Timestamp> queryDateList = new ArrayList<>();
        //分片日期记录count条数 （数据结构： N: count）
        List<Long> rowCountList = new ArrayList<>();

        datePartitionInfo.forEach(tp -> {
            queryDateList.add(tp._1());
            queryDateList.add(tp._2());
            rowCountList.add(tp._3());
        });


        //检测大分片 TODO 待优化
        int position = 0 ,index = 0;

        List<Integer> bigPartDateIndex = new ArrayList<>();
        for(Long cnt : rowCountList){
            if(cnt > partitionOverflowSize){
                bigPartDateIndex.add(index * 2);
            }
            index++;
        }


        //TODO 此处逻辑有问题！！！
        Map<Integer,Integer> dateIndexMap = new LinkedHashMap<>();//"小"分片日期数据区间
        for(int bIndex : bigPartDateIndex){
            if(bIndex / 2 > position){
                dateIndexMap.put(position, bIndex / 2);
            }
            position = bIndex / 2 + 1;
        }

        if(position < rowCountList.size()){
            dateIndexMap.put(position,rowCountList.size());
        }

        //尝试小分片合并,减少分片量
        List<Tuple3<Timestamp,Timestamp,Long>> mergedDateList = null;
        for(Map.Entry<Integer,Integer> entry : dateIndexMap.entrySet()){
            try{
                List<Long> subList = rowCountList.subList(entry.getKey(),entry.getValue());
                if(subList != null && subList.size() > 0){
                    List<Timestamp> subTimeList = queryDateList.subList(entry.getKey() * 2, entry.getValue() * 2);
                    mergedDateList = doMergePartition2(subList,subTimeList,mergedStopSize);
                    mergedDateList.forEach(tuple -> {
                        resultMap.add(new Tuple2<>(DateUtil.formatTs(tuple._1(),DATE_FORMATTER),DateUtil.formatTs(tuple._2(),DATE_FORMATTER)));
                    });
                }
            }catch (Exception e) {
                log.error(e.getMessage());
            }

        }

        //不存在大分片则退出
        if(bigPartDateIndex == null || bigPartDateIndex.size() == 0){
            return ;
        }

        //拆分大分片
        Map<LocalDateTime, LocalDateTime> splitDateMap = new LinkedHashMap<>();
        for(Integer idx : bigPartDateIndex){
            Long rowCount = rowCountList.get(idx / 2);
            LocalDateTime minDateTime = DateUtil.parseTs2DateTime(queryDateList.get(idx));
            LocalDateTime maxDateTime = DateUtil.parseTs2DateTime(queryDateList.get(idx + 1));

            log.warn("Find BIG Partition , Date [{} / {}] ,rowCount:{} " , minDateTime.format(DATE_FORMATTER),maxDateTime.format(DATE_FORMATTER),rowCount);

            //大分片时间降级 （Day -> Hours）
            //此处要做动态分片单位识别

            //日期分片，最小拆分单位到秒，此处统一采用秒（最小时间单位）进行拆分、可以规避不同维度日期分片（DAYS/HOURS/MINTUS/HOURS）时无法进行动态单位转换的问题
            long splitNum = (long)Math.ceil(rowCount / (partitionOverflowSize * 1.0D));;

            //todo 此处强制按天拆分是否会有问题！！！ 当初为何这样写？？？
//            LocalDateTime[] arr = getOneDayRange(queryDateList.get(idx+1),true);//max值
            Map<LocalDateTime, LocalDateTime> subPartition = partitionByDateInteval(minDateTime, maxDateTime, splitNum, ChronoUnit.SECONDS);
            subPartition.entrySet().forEach(entry -> {
                log.info("Sub BIG Partition result : Date [{} / {}]" , entry.getKey().format(DATE_FORMATTER),entry.getValue().format(DATE_FORMATTER));
            });
            splitDateMap.putAll(subPartition);
        }

        //分片结果检测
        List<Tuple3<Timestamp, Timestamp, Long>> splitPartitionStat = partitionDetection(splitDateMap, conn);
        doMain(resultMap , splitPartitionStat , partitionOverflowSize , mergedStopSize , conn);
    }

}