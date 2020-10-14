package com.mrfsong.cache.rocks;

import com.google.common.collect.Lists;
import com.mrfsong.cache.rocks.serialize.JavaSerializer;
import com.mrfsong.cache.rocks.vo.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.rocksdb.*;
import org.rocksdb.util.SizeUnit;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * <p>
 * rocksdb测试
 * </P>
 *
 * @Author songfei20
 * @Date 2020/4/13
 */
@Slf4j(topic = "RockdbLogger")
public class RocksTest {

    static {
        RocksDB.loadLibrary();
    }


    private static final String ROCKS_DB_PATH = "/tmp/rocksdb2";
    private static final String ROCKS_DB_COLUMN_FAMILY = "TEST";

    private String getCurrDateTimeFormat() {
        LocalDateTime now = LocalDateTime.now();
        return now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }

    @Before
    public void init() throws Exception {
        log.warn("==================== Begin to reset rocksdb ==========");

        //遍历所有CF
        try(final Options options = new Options().setCreateIfMissing(true)){
            final List<byte[]> columnFamilyNames = RocksDB.listColumnFamilies(options,ROCKS_DB_PATH);
            for(byte[] cfByte : columnFamilyNames){
                log.warn("list column family -> {}" , new String(cfByte));
            }

        }catch (RocksDBException e) {
            throw e;
        }

        //删除数据库全部数据
        try (final Options options = new Options()
                .setCreateIfMissing(true)) {
            RocksDB.destroyDB(ROCKS_DB_PATH,options);
        }

        log.warn("==================== Finish to reset rocksdb ==========");
    }

    @After
    public void destroy(){}

    @Test
    public void basicAPI() {

        try (final Options options = new Options()) {

            /**============================== RocksDB Configuration Begin ==============================*/
            options.setCreateIfMissing(true);
            options.setCreateMissingColumnFamilies(true);

            //列族设置
            ColumnFamilyOptions columnFamilyOptions = new ColumnFamilyOptions();
            columnFamilyOptions.setNumLevels(3);
            columnFamilyOptions.setCompressionType(CompressionType.LZ4_COMPRESSION);
            columnFamilyOptions.setBloomLocality(10);
            columnFamilyOptions.setArenaBlockSize(1024 * 1024 * 100);//todo 100M?

            ColumnFamilyDescriptor testCfDescriptor = new ColumnFamilyDescriptor(ROCKS_DB_COLUMN_FAMILY.getBytes(),columnFamilyOptions);


            //SstFile配置
            try(SstFileManager sstFileManager = new SstFileManager(Env.getDefault())){
                //设置sst文件可使用最大磁盘空间大小
                sstFileManager.setMaxAllowedSpaceUsage(1024 * 1024 * 10);//unit:byte
                options.setSstFileManager(sstFileManager);
            }

            //WAL文件清理策略
            options.setWalTtlSeconds(60 * 1L);
            options.setWalSizeLimitMB(100L);


            //设置memtable大小
            options.setWriteBufferSize(1024 * 1024 * 128);

            WriteBufferManager writeBufferManager = new WriteBufferManager(1024 * 1024 * 128 , new LRUCache(100));//配置mmtable使用最大内存
            options.setWriteBufferManager(writeBufferManager);

            /**============================== RocksDB Configuration End ==============================*/

            // a factory method that returns a RocksDB instance
            String path = String.format(ROCKS_DB_PATH,"test");
            try (final RocksDB db = RocksDB.open(options, path)) {
                ColumnFamilyHandle columnFamily = db.createColumnFamily(testCfDescriptor);
                byte[] bytes = db.get(columnFamily,"hello".getBytes());
                if(bytes == null){
                    db.put(columnFamily,"hello".getBytes(),"rocksdb".getBytes());
                }
                db.delete(columnFamily,"hello".getBytes());

                TimeUnit.SECONDS.sleep(10);
            }

        } catch (Exception e) {
            // do some error handling
            fail(e.getMessage());
        }




    }

    @Test
    public void doCheckpoint() {
        //checkpoint操作
        try (final Options options = new Options()) {

            /**============================== RocksDB Configuration Begin ==============================*/
            options.setCreateIfMissing(true);
            options.setCreateMissingColumnFamilies(true);

            //列族设置
            /*ColumnFamilyOptions columnFamilyOptions = new ColumnFamilyOptions();
            columnFamilyOptions.setNumLevels(3);
            columnFamilyOptions.setCompressionType(CompressionType.LZ4_COMPRESSION);
            columnFamilyOptions.setBloomLocality(10);
            columnFamilyOptions.setArenaBlockSize(1024 * 1024 * 100);//todo 100M?

            ColumnFamilyDescriptor testCfDescriptor = new ColumnFamilyDescriptor(ROCKS_DB_COLUMN_FAMILY.getBytes(), columnFamilyOptions);*/


            //SstFile配置
            try (SstFileManager sstFileManager = new SstFileManager(Env.getDefault())) {
                //设置sst文件可使用最大磁盘空间大小
                sstFileManager.setMaxAllowedSpaceUsage(1024 * 1024 * 10);//unit:byte
                options.setSstFileManager(sstFileManager);
            }

            //WAL文件清理策略
            options.setWalTtlSeconds(60 * 1L);
            options.setWalSizeLimitMB(100L);
            String ckpPath = String.format(ROCKS_DB_PATH,"ckp");
            try (final RocksDB db = RocksDB.open(options, ckpPath)) {
                String timeSuffix = getCurrDateTimeFormat();

                //【WARN】checkpoint目录必须预先创建
                File snapDir = new File(ckpPath + File.separator + timeSuffix);
                if (!snapDir.exists()) {
                    snapDir.mkdirs();
                }

                try (final Checkpoint checkpoint = Checkpoint.create(db)) {
                    checkpoint.createCheckpoint(ckpPath + File.separator + timeSuffix + File.separator + "snapshot1");
                    db.put("key1".getBytes(), "value1".getBytes());
                    checkpoint.createCheckpoint(ckpPath + File.separator + timeSuffix + File.separator + "snapshot2");
                }
            }
        }catch (RocksDBException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void doSnapshot(){
        try (final Options options = new Options()) {

            /**============================== RocksDB Configuration Begin ==============================*/
            options.setCreateIfMissing(true);
            options.setCreateMissingColumnFamilies(true);

            //列族设置
            /*ColumnFamilyOptions columnFamilyOptions = new ColumnFamilyOptions();
            columnFamilyOptions.setNumLevels(3);
            columnFamilyOptions.setCompressionType(CompressionType.LZ4_COMPRESSION);
            columnFamilyOptions.setBloomLocality(10);
            columnFamilyOptions.setArenaBlockSize(1024 * 1024 * 100);//todo 100M?

            ColumnFamilyDescriptor testCfDescriptor = new ColumnFamilyDescriptor(ROCKS_DB_COLUMN_FAMILY.getBytes(), columnFamilyOptions);*/


            //SstFile配置
            try (SstFileManager sstFileManager = new SstFileManager(Env.getDefault())) {
                //设置sst文件可使用最大磁盘空间大小
                sstFileManager.setMaxAllowedSpaceUsage(1024 * 1024 * 10);//unit:byte
                options.setSstFileManager(sstFileManager);
            }

            //WAL文件清理策略
            options.setWalTtlSeconds(60 * 1L);
            options.setWalSizeLimitMB(100L);

            //snapshot操作
            try (final RocksDB db = RocksDB.open(options, String.format(ROCKS_DB_PATH,"snap"))) {
                db.put("key".getBytes(), "value".getBytes());
                try (final RocksIterator iterator = db.newIterator()) {

                    //遍历CF数据
                    for (iterator.seekToFirst(); iterator.isValid(); iterator.next()) {
                        log.info(new String(iterator.key()) + ":" + new String(iterator.value()));
                    }
                    iterator.status();

                    /*iterator.seekToFirst();
                    assertTrue(iterator.isValid());
                    assertEquals(iterator.key(),"key1".getBytes());
                    log.info(new String(iterator.key()) + ":" + new String(iterator.value()));
                    iterator.next();
                    assertTrue(iterator.isValid());
                    assertEquals(iterator.key(),"key2".getBytes());
                    log.info(iterator.key() + ":" + iterator.value());
                    iterator.next();

                    iterator.seekToLast();
                    iterator.seekToFirst();;
                    assertFalse(iterator.isValid());*/
                }

                db.put("key2".getBytes(), "value2".getBytes());
                final Snapshot snapshot = db.getSnapshot();
                final ReadOptions readOptions = new ReadOptions().setSnapshot(snapshot);
                try (final RocksIterator snapshotIterator = db.newIterator(db.getDefaultColumnFamily(), readOptions)) {
                    /*snapshotIterator.seekToFirst();
                    assertTrue(snapshotIterator.isValid());
                    assertEquals(snapshotIterator.key(),"key1".getBytes());
                    snapshotIterator.next();
                    assertFalse(snapshotIterator.isValid());*/

                    for (snapshotIterator.seekToFirst(); snapshotIterator.isValid(); snapshotIterator.next()) {
                        log.info(new String(snapshotIterator.key()) + ":" + new String(snapshotIterator.value()));
                    }



                }finally {
                    // release Snapshot
                    db.releaseSnapshot(snapshot);
                }

            }
        }catch (RocksDBException e) {
            fail(e.getMessage());
        }

    }

    @Test
    public void testTtlDB(){

        try (final Options options = new Options()){
            options.setCreateIfMissing(true).setMaxCompactionBytes(0);
            /*options.setTtl(1_000L * 60 * 1);
            options.setWalTtlSeconds(60L * 5);*/
            String ttlDbPath = String.format(ROCKS_DB_PATH,"ttl");

            //ttl单位：second
            try(final TtlDB ttlDB = TtlDB.open(options,ttlDbPath,1,false);){
                ttlDB.put("ttlKey".getBytes(),"value".getBytes());
                TimeUnit.SECONDS.sleep(2);
                //手工触发compact
                ttlDB.compactRange();
                byte[] bytes = ttlDB.get("ttlKey".getBytes());
                assertNull(bytes);
            }


            try(final TtlDB ttlDB = TtlDB.open(options,ttlDbPath,60,false);){
                ttlDB.put("ttlKey2".getBytes(),"value".getBytes());
            }

            try(final RocksDB db = RocksDB.open(options, ttlDbPath)){
                byte[] bytes = db.get("ttlKey2".getBytes());
                assertNotNull(bytes);
                log.info(new String(bytes));
            }

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testColumnFamily() throws Exception {


        Statistics stats = new Statistics();
        Filter bloomFilter = new BloomFilter(10);

        Options options = new Options();

        //TODO rocks中总共有 WAL、Meta、Sstable等多种文件类型，如何规划全局的磁盘使用量？？？

        /* wal文件配置 */

        options.setMaxLogFileSize(100 * SizeUnit.MB);//配置单个WAL文件大小， 如果文件大于`max_log_file_size`, 会新建一个log文件

        // 1. 如果两者都设置为0，则将尽快删除日志，并且不会进入存档。
        // 2. 如果WAL_ttl_seconds为0且WAL_size_limit_MB不为0，则每10分钟检查一次WAL文件，如果总大小大
        //    于WAL_size_limit_MB，则从最早开始删除它们直到满足size_limit。 所有空文件都将被删除。
        // 3. 如果WAL_ttl_seconds不为0且WAL_size_limit_MB为0，则每个WAL_ttl_seconds / 2将检查WAL
        //    文件，并且将删除早于WAL_ttl_seconds的WAL文件。
        // 4. 如果两者都不为0，则每隔10分钟检查一次WAL文件，并且在ttl为第一个的情况下执行两个检查。
        options.setWalTtlSeconds(60 * 10);
        options.setWalSizeLimitMB(1 * SizeUnit.GB);

        options.setMaxTotalWalSize(1 * SizeUnit.GB);//一旦预写日志超过此大小，我们将开始强制刷新memtables由最早的实时WAL文件（即导致所有空间放大的文件）.如果设置为0，我们将会动态的选择WAL文件的大小通过

        /* ManiFest文件配置 */
        options.setMaxManifestFileSize(100 * SizeUnit.MB);//manifest文件在达到此限制时翻转。旧的清单文件将被删除。 默认值为1GB，
        options.setManifestPreallocationSize(100 * SizeUnit.MB);//要预分配（通过fallocate）清单文件的字节数。 默认值为4mb

        /* sstable配置 */
        options.setCompactionStyle(CompactionStyle.FIFO);//设置sstable文件合并方式
        options.setCompressionType(CompressionType.LZ4_COMPRESSION);//设置数据压缩算法

        options.setLevel0FileNumCompactionTrigger(6);// L0 触发 Compaction 操作的文件个数阈值
        options.setMaxBackgroundCompactions(10);//最大的后台并行 Compaction 作业数
        options.setNumLevels(7);//总层级数

        //设置sstable最大磁盘使用量
        SstFileManager sstFileManager = new SstFileManager(Env.getDefault());
        sstFileManager.setMaxAllowedSpaceUsage(100 * SizeUnit.MB);;
        options.setSstFileManager(sstFileManager);

        BlockBasedTableConfig basedTableConfig = new BlockBasedTableConfig();
        basedTableConfig
                .setBlockCacheSize(64 * SizeUnit.KB)
                .setBlockSize(32 * SizeUnit.KB)
                .setFilter(bloomFilter)
                .setCacheNumShardBits(6)
                .setBlockSizeDeviation(5)
                .setBlockRestartInterval(10)
                .setCacheIndexAndFilterBlocks(true)
                .setHashIndexAllowCollision(false)
                .setBlockCacheCompressedSize(64 * SizeUnit.KB)
                .setBlockCacheCompressedNumShardBits(10)
        ;
        options.setTableFormatConfig(basedTableConfig);

        /* memTable设置 */
        options.setWriteBufferSize(64 * SizeUnit.MB);//单个memTable大小
        options.setDbWriteBufferSize(0L);//所有列族的memtable的大小总和
        options.setMaxWriteBufferNumber(5);//内存中可以拥有刷盘到SST文件前的最大memtable数
        options.setMinWriteBufferNumberToMerge(2);
        options.setMaxBackgroundFlushes(1);//后台最多同时进行的 Flush 任务数

        options.setMaxCompactionBytes(64 * SizeUnit.MB);//所有压缩后的文件的最大大小。如果需要压缩的文件总大小大于这个值，我们在压缩的时候会避免展开更低级别的文件。

        /*WriteBufferManager writeBufferManager = new WriteBufferManager();
        options.setWriteBufferManager(WriteBufferManager);*/



        /* 其它配置 */
        options.setStatistics(stats);
//        options.setRateLimiter(); //磁盘写入限速器
        options.setCreateIfMissing(true).setCreateMissingColumnFamilies(true);

        /* writeOption 配置*/
        WriteOptions writeOption = new WriteOptions();
        writeOption.disableWAL();//关闭wal日志

        // 如果为true，RocksDB支持刷新多个列族并以原子方式将其结果提交给MANIFEST。请注意，如果始终启用WAL，
        // 则无需将atomic_flush设置为true，因为WAL允许数据库恢复到WAL中的最后一个持久状态。
        // 当存在不受WAL保护的写入列族时，此选项很有用。
        // 对于手动刷新，应用程序必须指定要列的族列并用DB :: Flush中以原子方式刷新。
        // 对于自动触发的刷新，RocksDB以原子方式刷新所有列族。
        // 目前可以启用WAL-enabled写入在进行原子flush之后可以replay
        // 如果进程崩溃并尝试恢复，则独立进行。
        options.setAtomicFlush(true);




        //列族配置
        ColumnFamilyOptions columnFamilyOptions = new ColumnFamilyOptions();
        columnFamilyOptions.setCompressionType(CompressionType.BZLIB2_COMPRESSION);//设置压缩算法
        columnFamilyOptions.setTableFormatConfig(new BlockBasedTableConfig().setBlockSize(32 * SizeUnit.KB));



        final List<ColumnFamilyDescriptor> cfNames = Arrays.asList(
                new ColumnFamilyDescriptor(RocksDB.DEFAULT_COLUMN_FAMILY),
                new ColumnFamilyDescriptor("T_CF_1".getBytes(),columnFamilyOptions),
                new ColumnFamilyDescriptor("T_CF_2".getBytes(),columnFamilyOptions)
        );

        final List<ColumnFamilyHandle> columnFamilyHandleList = new ArrayList<>();



        try (final DBOptions dbOptions = new DBOptions(options)){
            try(final RocksDB rocksDB = RocksDB.open(dbOptions,ROCKS_DB_PATH,cfNames,columnFamilyHandleList)){

                //未指定cf时更新
                rocksDB.put("test_key".getBytes(), "d_value".getBytes());//不指定则使用默认CF
                rocksDB.put(columnFamilyHandleList.get(1), "test_key".getBytes(),"value_1".getBytes());
                rocksDB.put(columnFamilyHandleList.get(2), "test_key".getBytes(),"value_2".getBytes());

                byte[] cf0Bytes = rocksDB.get("test_key".getBytes());
                byte[] cf1Bytes = rocksDB.get(columnFamilyHandleList.get(1), "test_key".getBytes());
                byte[] cf2Bytes = rocksDB.get(columnFamilyHandleList.get(2), "test_key".getBytes());

                log.info("value from cf0 : {}" , (cf0Bytes == null) ? "" : new String(cf0Bytes));
                log.info("value from cf1 : {}" , (cf1Bytes == null) ? "" : new String(cf1Bytes));
                log.info("value from cf2 : {}" , (cf2Bytes == null) ? "" : new String(cf2Bytes));

                //batch write
                try(WriteBatch writeBatch = new WriteBatch()){
                    writeBatch.put(columnFamilyHandleList.get(1), "test_key_1".getBytes(),"test_key_1".getBytes());
                    writeBatch.put(columnFamilyHandleList.get(2),"test_key_1".getBytes(),"test_key_2".getBytes());
                    rocksDB.write(writeOption,writeBatch);
                }

                //multi get
                List<byte[]> multiGetAsList = rocksDB.multiGetAsList(columnFamilyHandleList.subList(1,3), Lists.newArrayList("test_key_1".getBytes(), "test_key_1".getBytes()));
                multiGetAsList.forEach(bytes -> {
                    if(bytes != null){
                        log.info("multiGet ==========> {}" , new String(bytes));
                    }
                });

                //遍历Column Family 全部数据
                List<RocksIterator> cfIterators = rocksDB.newIterators(columnFamilyHandleList);
                for(RocksIterator iter : cfIterators){
                    iter.seekToFirst();
                    while (iter.isValid()) {
                        log.info("CF Iter: " + new String(iter.key()) + "|" + new String(iter.value()));
                        iter.next();
                    }
                }


            }finally {
                for (final ColumnFamilyHandle columnFamilyHandle : columnFamilyHandleList) {
                    columnFamilyHandle.close();
                }
            }
        }


        try(final Options opt = new Options()) {
            opt.setCreateIfMissing(true);
            opt.setCreateMissingColumnFamilies(true);
            List<ColumnFamilyHandle> columnFamilyHandles = Lists.newArrayList();
            try(final RocksDB rocksDB = RocksDB.open(new DBOptions(opt),ROCKS_DB_PATH,cfNames,columnFamilyHandles)) {
                byte[] cf1Bytes = rocksDB.get(columnFamilyHandles.get(1), "test_key".getBytes());
                log.info(new String(cf1Bytes));

            }finally {
                for (final ColumnFamilyHandle columnFamilyHandle : columnFamilyHandles) {
                    columnFamilyHandle.close();
                }
            }

        }



    }

    @Test
    public void testMerge() {


       /* final List<ColumnFamilyDescriptor> cfNames = Arrays.asList(
                new ColumnFamilyDescriptor(RocksDB.DEFAULT_COLUMN_FAMILY),
                new ColumnFamilyDescriptor("T_CF_1".getBytes()),
                new ColumnFamilyDescriptor("T_CF_2".getBytes())
        );

        final List<ColumnFamilyHandle> columnFamilyHandleList = new ArrayList<>();*/

        try (final Options dbOptions = new Options()) {
            dbOptions.setCreateIfMissing(true);
            dbOptions.setMaxLogFileSize(1024 * 1024 * 10);
            dbOptions.setMergeOperator(new StringAppendOperator());
            try(final RocksDB rocksDB = RocksDB.open(dbOptions,ROCKS_DB_PATH)){
                rocksDB.put("TEST_MERGE_KEY".getBytes() , "1".getBytes());

                rocksDB.merge("TEST_MERGE_KEY".getBytes(),"2".getBytes());

                byte[] valBytes = rocksDB.get("TEST_MERGE_KEY".getBytes());

                log.info(new String(valBytes));
            }
        }catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testDeleteByRange() {
        try (final Options options = new Options()) {
            options.setCreateIfMissing(true);
            options.setCreateMissingColumnFamilies(true);
            try (final RocksDB db = RocksDB.open(options, ROCKS_DB_PATH)) {
                Slice start = new Slice("hello");
                Slice end = new Slice("bye");

                ColumnFamilyOptions columnFamilyOptions = new ColumnFamilyOptions();
                ColumnFamilyDescriptor testCfDescriptor = new ColumnFamilyDescriptor(ROCKS_DB_COLUMN_FAMILY.getBytes(),columnFamilyOptions);
                ColumnFamilyHandle columnFamily = db.createColumnFamily(testCfDescriptor);
                db.deleteFilesInRanges(columnFamily, Arrays.asList(start.data(),end.data()),true);
            }
        }catch (RocksDBException e) {
            e.printStackTrace();
        }

    }


    @Test
    public void testEntity() throws Exception{

        //初始化rocksdb数据目录
        File f = new File(ROCKS_DB_PATH);
        if(!f.exists()){
            Files.createDirectories(Paths.get(ROCKS_DB_PATH));
        }else {
            f.delete();
        }

        User user = new User("zsan",30);
        Serializer<User> serializer = new JavaSerializer<>();
        try(final Options options = new Options()){
            options.setCreateIfMissing(true);

            try (final RocksDB db = RocksDB.open(options, ROCKS_DB_PATH)) {
                byte[] keyBytes = serializer.serialize(user);
                byte[] valBytes = serializer.serialize(user);

                db.put(keyBytes,valBytes);
                valBytes = db.get(keyBytes);
                if(valBytes != null && valBytes.length > 0){
                    User user1 = serializer.deSerialize(valBytes);
                    log.info(user1.toString());
                }





            }
        }catch (Exception e) {
            fail(e.getMessage());
        }




    }
}
