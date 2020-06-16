package com.mrfsong.common.db;

import com.github.jsonzou.jmockdata.MockConfig;
import com.google.common.base.Stopwatch;
import com.mrfsong.common.util.DateUtil;
import com.mrfsong.common.util.Printer;
import com.mrfsong.common.util.Tuple3;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.sql.*;
import java.time.LocalDateTime;
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

    /**
     * TODO 日期统计结果偏大
     */
    @Test(timeout = 1000 * 60 * 10)
    public void testDatePartition() {
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

        List<Tuple3<String,String,Long>> lastSplitResult = new ArrayList<>();//最终日期分片结果

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
            Map<LocalDateTime, LocalDateTime> dayPartitionMap = splitDateToDay(DateUtil.parseTs2DateTime(minDate), DateUtil.parseTs2DateTime(maxDate), ChronoUnit.DAYS);
            List<Tuple3<Timestamp,Timestamp,Long>> splitDayStatResult = getPartitionStat(dayPartitionMap,conn);
            /* 分片检测， 大分片识别、大分片拆分、小分片合并 */
            doDatePartition(lastSplitResult,splitDayStatResult,partitionOverflowSize,mergedStopSize,conn);

            lastSplitResult.forEach(tp -> log.warn("最终分片结果：{}",tp.toString()));
            log.info("处理完成，耗时 [{}]", stopwatch);

            doCountTest(conn,lastSplitResult);

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

    private void doCountTest(Connection conn , List<Tuple3<String,String,Long>> tuple3List) {
        String sql = "select count(*) from source where create_time >= ? and create_time < ?";

        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try{
            long dbCount = 0L;
            long pCount = 0L;
            for(Tuple3<String,String,Long> tuple3 : tuple3List) {
                statement = conn.prepareStatement(sql);
                statement.setString(1,tuple3._1());
                statement.setString(2,tuple3._2());
                resultSet = statement.executeQuery();
                if(resultSet.next()){
                    long aLong = resultSet.getLong(1);
                    log.warn("startTime : {} , endTime : {} , dbCount: {} , pCount:{}" , tuple3._1(),tuple3._2(),aLong , tuple3._3());
                    dbCount += aLong;
                    pCount += tuple3._3();
                }
            }

            log.warn("dbTotal:{} ,  pTotal : {}" , dbCount,pCount);

        }catch(Exception e){

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

    }


    @Test
    public void testMethod() {
        LocalDateTime startTime = LocalDateTime.parse("2020-06-05 23:10:35", DATE_FORMATTER);
        LocalDateTime endTime = LocalDateTime.parse("2020-06-16 01:00:00", DATE_FORMATTER);
        Map<LocalDateTime, LocalDateTime> dateTimeMap = splitDateToDay(startTime, endTime, ChronoUnit.DAYS);
        dateTimeMap.entrySet().forEach(entry -> {
            log.info("start : {} , end: {}" , entry.getKey().format(DATE_FORMATTER) ,entry.getValue().format(DATE_FORMATTER));
        });
    }

    private String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-","").toLowerCase();
    }


    private Map<LocalDateTime,LocalDateTime> getPagingDate(LocalDateTime minDateTime, LocalDateTime maxDateTime, long splitNum , ChronoUnit dateInteval ) {
        Map<LocalDateTime,LocalDateTime> splitResult = new LinkedHashMap<>();
        long differ = dateInteval.between(minDateTime, maxDateTime);//日期差值
        long step = (long)Math.ceil(differ  * 1.0d / splitNum);//步长
        LocalDateTime tmpDateTime = minDateTime;

        for(long idx = 1; idx <= splitNum;idx++){
            LocalDateTime plusDateTime = tmpDateTime.plus(step,dateInteval);
            if(idx == splitNum){
                plusDateTime = maxDateTime;//此处添加1s是为了兼容日期 date >= 'yyyy-MM-dd hh:MM:ss' and date < 'yyyy-MM-dd hh:MM:ss'查询逻辑
            }
            splitResult.put(tmpDateTime, plusDateTime);
            tmpDateTime = plusDateTime;
        }
        return splitResult;

    }

    /**
     * 日期分段切分
     * @param minDateTime
     * @param maxDateTime
     * @param dateInteval
     * @return
     */
    private Map<LocalDateTime,LocalDateTime> splitDateToDay(LocalDateTime minDateTime, LocalDateTime maxDateTime, ChronoUnit dateInteval) {
        Map<LocalDateTime,LocalDateTime> splitResult = new LinkedHashMap<>();
        long hours = ChronoUnit.HOURS.between(minDateTime, maxDateTime);
        long differ = (long)Math.ceil(hours / 24.0d);
        if(differ == 0){//日期间隔未超过一个chronoUnit
            splitResult.put(minDateTime, maxDateTime.plusSeconds(1L));
        }else {
            LocalDateTime startTime = minDateTime;
            LocalDateTime endTime;
            for(int idx=1;idx<differ+1;idx++){
                if(idx == differ) {
                    splitResult.put(startTime,maxDateTime.plusSeconds(1L));
                } else {
                    endTime = minDateTime.plus(idx,dateInteval);
                    splitResult.put(startTime,endTime);
                    startTime = endTime;
                }
            }
        }

        return splitResult;
    }

    /**
     * 小分片合并
     * @param countList
     * @param timeList
     * @param threshold
     * @return
     */
    private List<Tuple3<Timestamp,Timestamp,Long>> mergePartition(List<Long> countList , List<Timestamp> timeList , Long threshold) {

        List<Tuple3<Timestamp,Timestamp,Long>> results = new ArrayList<>();
        long counter = 0L;
        Timestamp startTs = timeList.get(0);

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
     * 获取分片统计信息
     * @param dateTimeMap   分片日期
     * @param conn          dbConnection
     * @return              <最小日期,最大日期,记录数>
     */
    private List<Tuple3<Timestamp,Timestamp,Long>> getPartitionStat(Map<LocalDateTime , LocalDateTime> dateTimeMap , Connection conn) {

        List<Tuple3<Timestamp,Timestamp,Long>> gDateList = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String sql = "select count(*) from source where create_time >= ? and create_time < ?";
        try{
            for(Map.Entry<LocalDateTime,LocalDateTime> entry : dateTimeMap.entrySet()){
                String queryDateFrom = entry.getKey().format(DATE_FORMATTER);
                String queryDateTo = entry.getValue().format(DATE_FORMATTER);
                statement = conn.prepareStatement(sql);
                statement.setString(1,queryDateFrom);
                statement.setString(2,queryDateTo);
                resultSet = statement.executeQuery();
                Long count;
                if(resultSet.next()){
                    count = resultSet.getLong(1);
                    gDateList.add(new Tuple3(DateUtil.parse2Ts(queryDateFrom,DATE_FORMATTER),DateUtil.parse2Ts(queryDateTo,DATE_FORMATTER),count));
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
     * 日期分片算法
     * @param resultMap                 分片结果
     * @param datePartitionStat         分片统计信息
     * @param partitionOverflowSize     分片记录溢出阈值
     * @param mergedStopSize            分片合并截断阈值
     * @param conn                      DbConnection
     */
    private void doDatePartition(List<Tuple3<String,String,Long>> resultMap , List<Tuple3<Timestamp,Timestamp,Long>> datePartitionStat , Long partitionOverflowSize, Long mergedStopSize, Connection conn) {

        //分片日期min/max查询结果 （数据结构： 2N: minDate 2N+1: maxDate）
        List<Timestamp> queryDateList = new ArrayList<>();
        //分片日期记录count条数 （数据结构： N: count）
        List<Long> rowCountList = new ArrayList<>();

        datePartitionStat.forEach(tp -> {
            queryDateList.add(tp._1());
            queryDateList.add(tp._2());
            rowCountList.add(tp._3());
        });

        /* ================== 分片检测 ==================*/
        //检测大分片
        int position = 0 ,index = 0;
        List<Integer> bigPartDateIndex = new ArrayList<>();
        for(Long cnt : rowCountList){
            if(cnt > partitionOverflowSize){
                bigPartDateIndex.add(index * 2);
            }
            index++;
        }


        //小分片数据分段
        Map<Integer,Integer> dateIndexMap = new LinkedHashMap<>();
        for(int bIndex : bigPartDateIndex){
            if(bIndex / 2 > position){
                dateIndexMap.put(position, bIndex / 2);
            }
            position = bIndex / 2 + 1;
        }

        if(position < rowCountList.size()){
            dateIndexMap.put(position,rowCountList.size());
        }

        /* ================== 小分片合并 ==================*/
        List<Tuple3<Timestamp,Timestamp,Long>> mergedDateList ;
        for(Map.Entry<Integer,Integer> entry : dateIndexMap.entrySet()){
            try{
                List<Long> subList = rowCountList.subList(entry.getKey(),entry.getValue());
                if(subList != null && subList.size() > 0){
                    List<Timestamp> subTimeList = queryDateList.subList(entry.getKey() * 2, entry.getValue() * 2);
                    mergedDateList = mergePartition(subList,subTimeList,mergedStopSize);
                    mergedDateList.forEach(tuple -> {
                        resultMap.add(new Tuple3<>(DateUtil.formatTs(tuple._1(),DATE_FORMATTER),DateUtil.formatTs(tuple._2(),DATE_FORMATTER),tuple._3()));
                    });
                }
            }catch (Exception e) {
                log.error("小分片合并异常",e);
                e.printStackTrace();
            }
        }


        /* ================== 大分片拆分 ==================*/
        if(bigPartDateIndex != null && bigPartDateIndex.size() > 0){
            //拆分大分片
            Map<LocalDateTime, LocalDateTime> splitDateMap = new LinkedHashMap<>();
            for(Integer idx : bigPartDateIndex){
                Long rowCount = rowCountList.get(idx / 2);
                LocalDateTime minDateTime = DateUtil.parseTs2DateTime(queryDateList.get(idx));
                LocalDateTime maxDateTime = DateUtil.parseTs2DateTime(queryDateList.get(idx + 1));

                log.warn("Find BIG Partition , Date [{} / {}] ,rowCount:{} " , minDateTime.format(DATE_FORMATTER),maxDateTime.format(DATE_FORMATTER),rowCount);

                //日期分片，最小拆分单位到秒，此处统一采用秒（最小时间单位）进行拆分、可以规避不同维度日期（DAYS/HOURS/MINTUS/HOURS）切换时无法动态进行单位转换的问题
                long splitNum = (long)Math.ceil(rowCount / (partitionOverflowSize * 1.0D));
                Map<LocalDateTime, LocalDateTime> subPartition = getPagingDate(minDateTime, maxDateTime, splitNum, ChronoUnit.SECONDS);
                subPartition.entrySet().forEach(entry -> {
                    log.debug("Sub BIG Partition result : Date [{} / {}]" , entry.getKey().format(DATE_FORMATTER),entry.getValue().format(DATE_FORMATTER));
                });
                splitDateMap.putAll(subPartition);
            }

            //查询分片统计信息
            List<Tuple3<Timestamp, Timestamp, Long>> splitPartitionStat = getPartitionStat(splitDateMap, conn);
            doDatePartition(resultMap , splitPartitionStat , partitionOverflowSize , mergedStopSize , conn);
        }


    }

}