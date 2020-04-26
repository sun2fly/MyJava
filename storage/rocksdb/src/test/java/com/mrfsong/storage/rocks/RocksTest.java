package com.mrfsong.storage.rocks;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.rocksdb.*;

import java.io.File;
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


    private static final String ROCKS_DB_PATH = "D:\\data\\rocksdb\\%s";
    private static final String ROCKS_DB_COLUMN_FAMILY = "TEST_C_F";


    public String getCurrDateTimeFormat() {
        LocalDateTime now = LocalDateTime.now();
        return now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }

//    @Before
    public void resetDB() {
        log.warn("==================== Begin to reset rocksdb ==========");

        // todo 此处需要open数据库所有的column family ??? 如果是那就太恶心了！！！
        final List<ColumnFamilyDescriptor> cfDescriptors = Arrays.asList(
//                new ColumnFamilyDescriptor(RocksDB.DEFAULT_COLUMN_FAMILY),
                new ColumnFamilyDescriptor(ROCKS_DB_COLUMN_FAMILY.getBytes())
        );
        final List<ColumnFamilyHandle> columnFamilyHandleList = new ArrayList<>();

        try (final DBOptions options = new DBOptions()
                .setCreateIfMissing(true)
                .setCreateMissingColumnFamilies(true);
             final RocksDB db = RocksDB.open(options,
                     ROCKS_DB_PATH, cfDescriptors,
                     columnFamilyHandleList)) {

            ColumnFamilyHandle columnFamilyHandle = null;
            try {

                /*for(ColumnFamilyHandle columnFamilyHandle : columnFamilyHandleList){
                    if(!Arrays.equals(columnFamilyHandle.getDescriptor().getName(),RocksDB.DEFAULT_COLUMN_FAMILY)){
                        db.dropColumnFamily(columnFamilyHandle);
                    }


                }*/

                columnFamilyHandle = db.createColumnFamily(
                        new ColumnFamilyDescriptor(ROCKS_DB_COLUMN_FAMILY.getBytes(),
                                new ColumnFamilyOptions()));
                db.dropColumnFamily(columnFamilyHandle);
            } finally {

                if (columnFamilyHandle != null) {
                    columnFamilyHandle.close();
                }

                for (ColumnFamilyHandle cfHandle : columnFamilyHandleList) {
                    cfHandle.close();
                }
            }

        }catch (RocksDBException e) {
            e.printStackTrace();
        }

        //清除数据
        /*try (final Options options = new Options()) {
            options.setCreateIfMissing(true);
            options.setCreateMissingColumnFamilies(true);
            try (final RocksDB db = RocksDB.open(options, ROCKS_DB_PATH)) {
                ColumnFamilyOptions columnFamilyOptions = new ColumnFamilyOptions();
                ColumnFamilyDescriptor testCfDescriptor = new ColumnFamilyDescriptor(ROCKS_DB_COLUMN_FAMILY.getBytes(),columnFamilyOptions);
                ColumnFamilyHandle columnFamily = db.createColumnFamily(testCfDescriptor);
                db.dropColumnFamily(columnFamily);
            }
        }catch (RocksDBException e) {
            e.printStackTrace();
        }*/

        log.warn("==================== Finish to reset rocksdb ==========");
    }

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
    public void testColumnF(){

        //遍历所有CF
        try(final Options options = new Options().setCreateIfMissing(true)){
            final List<byte[]> columnFamilyNames = RocksDB.listColumnFamilies(options,ROCKS_DB_PATH);
            for(byte[] cfByte : columnFamilyNames){
                log.info("CF -> {}" , new String(cfByte));
            }

        }catch (RocksDBException e) {

        }


        final List<ColumnFamilyDescriptor> cfNames = Arrays.asList(
                new ColumnFamilyDescriptor(RocksDB.DEFAULT_COLUMN_FAMILY),
                new ColumnFamilyDescriptor(ROCKS_DB_COLUMN_FAMILY.getBytes())
        );

        final List<ColumnFamilyHandle> columnFamilyHandleList = new ArrayList<>();


        try (final DBOptions dbOptions = new DBOptions()){
            dbOptions.setCreateIfMissing(true).setCreateMissingColumnFamilies(true);
            dbOptions.setMaxLogFileSize(1024 * 1024 * 10);
            //do other ...... //




            try(final RocksDB cfDb = RocksDB.open(dbOptions,ROCKS_DB_PATH,cfNames,columnFamilyHandleList)){

                //未指定cf时更新
                cfDb.put("dfkey1".getBytes(), "dfvalue".getBytes());//不指定则使用默认CF
                cfDb.put(columnFamilyHandleList.get(0), "dfkey2".getBytes(),"dfvalue".getBytes());
                cfDb.put(columnFamilyHandleList.get(1), "newcfkey1".getBytes(),"newcfvalue".getBytes());

                //iterator
                List<RocksIterator> cfIterators = cfDb.newIterators(columnFamilyHandleList);
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
        }catch (Exception e) {
            e.printStackTrace();
        }



    }

    @Test
    public void testDelRange() {
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

    /**
     * 获取所有Column Family
     */
    private void getAllColumnFamily() {}
}
