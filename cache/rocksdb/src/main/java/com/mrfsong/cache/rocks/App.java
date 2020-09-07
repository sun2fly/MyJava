package com.mrfsong.cache.rocks;

import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;

/**
 * Hello world!
 *
 */
public class App 
{
    static {
        RocksDB.loadLibrary();
        System.out.println("============ Hey , I'm static block  ============");

    }

    public static void main( String[] args )
    {
        try (final Options options = new Options().setCreateIfMissing(true)) {

            try (final RocksDB db = RocksDB.open(options, "../db/test")) {

            }
        } catch (RocksDBException e) {
            // do some error handling
        }

    }
}
