package com.mrfsong.storage.rocks;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;

/**
 * <p>
 * rocksdb测试
 * </P>
 *
 * @Author songfei20
 * @Date 2020/4/13
 */
@Slf4j
public class RocksTest {

    static {
        RocksDB.loadLibrary();
    }


    @Test
    public void testConn() {

        try (final Options options = new Options().setCreateIfMissing(true)) {

            // a factory method that returns a RocksDB instance
            try (final RocksDB db = RocksDB.open(options, "rocks/db")) {
                byte[] bytes = db.get("hello".getBytes());
                if(bytes == null){
                    db.put("hello".getBytes(),"rocksdb".getBytes());
                }
//                db.delete("hello".getBytes());
                // do something
            }
        } catch (RocksDBException e) {
            // do some error handling
            e.printStackTrace();
        }

    }

    @Test
    public void testTtl(){}

    @Test
    public void testColumnF(){}
}
