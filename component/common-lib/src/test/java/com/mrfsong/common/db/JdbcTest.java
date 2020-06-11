package com.mrfsong.common.db;

import com.github.jsonzou.jmockdata.MockConfig;
import com.google.common.base.Stopwatch;
import com.mrfsong.common.util.Printer;
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
    private static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    Properties dbProperties;

    MockConfig mockConfig;

    AtomicLong counter = new AtomicLong(20165728L);

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
                .dateRange("2020-06-10 00:00:00","2020-06-10 23:59:59")
                .intRange(20,100)
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

    /**
     * TODO 如何处理Mock数据重复问题
     */
    @Test
    public void createData(){

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
//            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dcomb?characterEncoding=UTF8&useUnicode=true&rewriteBatchedStatements=true&useCompression=true&cachePrepStmts=true&useServerPrepStmts=true","root","root");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dcomb?characterEncoding=UTF8&useUnicode=true&rewriteBatchedStatements=true&useCompression=true","root","root");
            preparedStatement = conn.prepareStatement("insert into `dcomb`.`source`(id,`name`,age,create_time) values (? , ? , ? , ?)");
            long begin = System.currentTimeMillis();
            for(int i=0;i< 5000000;i++){
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
            log.warn("prepareStatementBatch 消耗时间:" + (System.currentTimeMillis() - begin));
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

//                LocalDateTime bigDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(entry.getKey().getTime()), ZoneId.systemDefault());
//                LocalDate localDate = bigDateTime.toLocalDate();


                double bigFactor = entry.getValue() / partitionScaleSize * 1.0D;
                //拆分单天
                long hourSplitTotal = Math.round(24 / bigFactor);
                LocalDateTime[] arr = getOneDayRange(entry.getKey());
                Map<LocalDateTime, LocalDateTime> localDateTimeLocalDateTimeMap = partitionByDateInteval(arr[0], arr[1], hourSplitTotal, ChronoUnit.HOURS);
            }

            //合并小分片（切记要剔除大分片的时间段）
            for(Map.Entry<Integer,Integer> entry : indexMap.entrySet()){
                List<Map.Entry<Timestamp, Long>> subList = arrayList.subList(entry.getKey(),entry.getValue());
                doMergeParition(subList,partitionScaleSize);
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



    @Test(timeout=30000)
    public void testDatePartitionNew(){


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
                LocalDateTime[] arr = getOneMaxDayRange(gDateList.get(idx+1));//max值
                Map<LocalDateTime, LocalDateTime> splitDateMap = partitionByDateInteval(arr[0], arr[1], hourSplitTotal, ChronoUnit.HOURS);
                splitDateMap.entrySet().stream().forEach(entry -> log.debug("[Bigger] start: {} , end: {}" , entry.getKey().format(DATE_FORMATTER),entry.getValue().format(DATE_FORMATTER)));

            }

            //合并小分片（切记要剔除大分片的时间段）
            for(Map.Entry<Integer,Integer> entry : dateIndexMap.entrySet()){
                List<Long> subList = gRowCountList.subList(entry.getKey(),entry.getValue());
                doMergeParition(subList,gDateList,partitionScaleSize);
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

    private String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-","").toLowerCase();
    }


    private Map<LocalDateTime,LocalDateTime> partitionByDateInteval(LocalDateTime minDateTime, LocalDateTime maxDateTime, long step , ChronoUnit dateInteval ) {

        Map<LocalDateTime,LocalDateTime> splitResult = new HashMap<>();
        long differ = dateInteval.between(minDateTime, maxDateTime);
        long offset = differ / step == 0 ? differ / step : (differ / step) + 1;
        LocalDateTime tmpDateTime = minDateTime;

        for(long idx = 1; idx <= offset;idx++){
            LocalDateTime plusDateTime = tmpDateTime.plus(step,dateInteval);
            if(idx == offset){
                plusDateTime = maxDateTime;
            }
            splitResult.put(tmpDateTime, plusDateTime);
            log.warn("startTime:{} , endTime:{}",tmpDateTime.format(DATE_FORMATTER),plusDateTime.format(DATE_FORMATTER));
            tmpDateTime = plusDateTime;
        }

        return splitResult;

    }

    private Map<LocalDateTime,LocalDateTime> partitionByDateInteval(Timestamp minTs, Timestamp maxTs, long step, ChronoUnit dateInteval) {

        Map<LocalDateTime,LocalDateTime> splitResult = new HashMap<>();

        LocalDateTime minDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(minTs.getTime()), ZoneId.systemDefault());
        LocalDateTime maxDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(minTs.getTime()), ZoneId.systemDefault());
        long differ = dateInteval.between(minDateTime, maxDateTime);
        long offset = differ / step == 0 ? differ / step : (differ / step) + 1;
        LocalDateTime tmpDateTime = minDateTime;

        for(long postition = 0; postition <= offset;postition++){
            LocalDateTime plusDateTime = tmpDateTime.plus(step,dateInteval);
            if(postition == offset){
                plusDateTime = maxDateTime;
            }
            log.warn("startTime:{} , endTime:{}",tmpDateTime.format(DATE_FORMATTER),plusDateTime.format(DATE_FORMATTER));
            splitResult.put(tmpDateTime, plusDateTime);
            tmpDateTime = plusDateTime;
        }
        return splitResult;
    }


    private LocalDateTime[] getOneDayRange(Timestamp ts){
        LocalDateTime[] array = new LocalDateTime[2];
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(ts.getTime()), ZoneId.systemDefault());
        array[0] = LocalDateTime.of(dateTime.toLocalDate(), LocalTime.MIN);
        array[1] = LocalDateTime.of(dateTime.toLocalDate(), LocalTime.MAX);//group by 返回的时间是粗略值，不一定为group by 字段的最大值

        return array;
    }

    private LocalDateTime[] getOneMaxDayRange(Timestamp ts){
        LocalDateTime[] array = new LocalDateTime[2];
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(ts.getTime()), ZoneId.systemDefault());
        array[0] = LocalDateTime.of(dateTime.toLocalDate(), LocalTime.MIN);
        array[1] = dateTime;//group by 返回的时间是粗略值，不一定为group by 字段的最大值

        return array;
    }

    private void doMergeParition(List<Map.Entry<Timestamp, Long>> dataList , Long threshold) {

        long counter = 0L;
        Timestamp startTs = dataList.get(0).getKey();
        LocalDateTime startDateTime = null ;
        LocalDateTime endDateTime = null ;

        //TODO 此处逻辑有问题，GroupBy结果的日期显示的只是单天日期中的一天，而统计值是当天的全部数据
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

    private void doMergeParition(List<Long> countList ,List<Timestamp> timeList , Long threshold) {

        long counter = 0L; int position = 0;
        Timestamp startTs = timeList.get(0);
        LocalDateTime startDateTime = null ;
        LocalDateTime endDateTime = null ;

        for(Long cnt : countList){
            counter += cnt;
            if(counter >= threshold){
                startDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(startTs.getTime()), ZoneId.systemDefault());
                endDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timeList.get(position * 2 + 1).getTime()), ZoneId.systemDefault());//取max
                log.warn("startTime:{} , endTime:{},counter:{}",startDateTime.format(DATE_FORMATTER),endDateTime.format(DATE_FORMATTER),counter);

                startTs = timeList.get((position + 1) * 2);
                counter = 0L;
            }
            position++;
        }

        if(counter > 0L){
            startDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(startTs.getTime()), ZoneId.systemDefault());
            endDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timeList.get((position-1) * 2 + 1).getTime()), ZoneId.systemDefault());//position累加器，最后值为数组元素数量，故此处需要做减1操作
            log.warn("startTime:{} , endTime:{} , counter:{}",startDateTime.format(DATE_FORMATTER),endDateTime.format(DATE_FORMATTER),counter);
        }

    }

    /**
     * 日期查询条件拆分
     * @param begin
     * @param end
     * @param conn
     * @return
     */
    private List<Range<Timestamp>> subDateResult(String begin , String end , Connection conn) {
        LocalDateTime dateTimeStart = LocalDateTime.parse(begin, DATE_FORMATTER);
        LocalDateTime dateTimeEnd = LocalDateTime.parse(end, DATE_FORMATTER);

        List<Range<Timestamp>> gDateList = new ArrayList<>();
        Map<LocalDateTime, LocalDateTime> dateSplitResult = partitionByDateInteval(dateTimeStart, dateTimeEnd, 1, ChronoUnit.DAYS);
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String sql = "select min(create_time),max(create_time), count(*) from source where create_time >= ? and create_time < ? GROUP BY year(create_time),MONTH(create_time),DAY(create_time)";
        try{
            for(Map.Entry<LocalDateTime, LocalDateTime> entry : dateSplitResult.entrySet()){
                statement = conn.prepareStatement(sql);
                statement.setString(1,entry.getKey().format(DATE_FORMATTER));
                statement.setString(2,entry.getValue().format(DATE_FORMATTER));
                resultSet = statement.executeQuery();
                if(resultSet.next()){
                    gDateList.add(new Range<>(resultSet.getTimestamp(1),resultSet.getTimestamp(2),resultSet.getLong(3)));
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



    class Range<T> {

        T min;

        T max;

        Long count;

        public Range(T min, T max, Long count) {
            this.min = min;
            this.max = max;
            this.count = count;
        }

        public T getMin() {
            return min;
        }

        public void setMin(T min) {
            this.min = min;
        }

        public T getMax() {
            return max;
        }

        public void setMax(T max) {
            this.max = max;
        }

        public Long getCount() {
            return count;
        }

        public void setCount(Long count) {
            this.count = count;
        }

        @Override
        public String toString() {
            return "Range{" +
                    "min=" + min +
                    ", max=" + max +
                    ", count=" + count +
                    '}';
        }
    }


}