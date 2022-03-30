package com.tomgs.storage.rocksdb;

import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.rocksdb.*;
import org.rocksdb.util.SizeUnit;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * BaseDemo
 *
 * @author tomgs
 * @since 2022/3/22
 */
public class BaseDemo {

    @Before
    public void before() {
        RocksDB.loadLibrary();
    }

    @Test
    public void testBaseOp() throws RocksDBException {
        String dbPath = "./target/test_db";
        final Options options = new Options();
        options.setCreateIfMissing(true);
        final RocksDB rocksDB = RocksDB.open(options, dbPath);

        System.out.println("put: [key, value]");
        rocksDB.put("key".getBytes(), "value".getBytes());
        System.out.println("===================================");

        byte[] bytes = rocksDB.get("key".getBytes());
        System.out.println("get: key -> " + new String(bytes));
        System.out.println("===================================");

        RocksIterator iter = rocksDB.newIterator();
        System.out.println("all key and value:");

        for (iter.seekToFirst(); iter.isValid(); iter.next()) {
            System.out.println("iter key: " + new String(iter.key()) + ",iter value: " +
                    new String(iter.value()));
        }

        rocksDB.close();
        options.close();
    }

    @Test
    public void testMultiOp() {
        String dbPath = "./target/test_db1";
        System.out.println("开始测试rocksdb的基本操作...");
        final Options options = new Options();
        final Filter bloomFilter = new BloomFilter(10);
        final ReadOptions readOptions = new ReadOptions().setFillCache(false);
        final Statistics stats = new Statistics();
        final RateLimiter rateLimiter = new RateLimiter(10000000, 10000, 10);

        options.setCreateIfMissing(true)
                .setStatistics(stats)
                .setWriteBufferSize(8 * SizeUnit.KB)
                .setMaxWriteBufferNumber(3)
                .setMaxBackgroundJobs(10)
                .setCompressionType(CompressionType.SNAPPY_COMPRESSION)
                .setCompactionStyle(CompactionStyle.UNIVERSAL);

        final BlockBasedTableConfig table_options = new BlockBasedTableConfig();
        Cache cache = new LRUCache(64 * 1024, 6);
        table_options.setBlockCache(cache)
                .setFilterPolicy(bloomFilter)
                .setBlockSizeDeviation(5)
                .setBlockRestartInterval(10)
                .setCacheIndexAndFilterBlocks(true)
                .setBlockCacheCompressed(new LRUCache(64 * 1000, 10));
        options.setTableFormatConfig(table_options);
        options.setRateLimiter(rateLimiter);

        try (final RocksDB db = RocksDB.open(options, dbPath)) {
            List<byte[]> keys = Lists.newArrayList();
            keys.add("hello".getBytes());

            db.put("hello".getBytes(), "world".getBytes());
            byte[] value = db.get("hello".getBytes());
            System.out.format("Get('hello') = %s\n", new String(value));

            // write batch test
            try (final WriteOptions writeOpt = new WriteOptions()) {
                for (int i = 1; i <= 9; ++i) {
                    try (final WriteBatch batch = new WriteBatch()) {
                        for (int j = 1; j <= 9; ++j) {
                            batch.put(String.format("%dx%d", i, j).getBytes(),
                                    String.format("%d", i * j).getBytes());
                            keys.add(String.format("%dx%d", i, j).getBytes());
                        }
                        db.write(writeOpt, batch);
                    }
                }
            }

            System.out.println("multiGetAsList方法获取");
            List<byte[]> values = db.multiGetAsList(keys);
            for (int i = 0; i < keys.size(); i++) {
                System.out.println(String.format("key:%s,value:%s",
                        new String(keys.get(i)),
                        (values.get(i) != null ? new String(values.get(i)) : null)));
            }

            System.out.println("newIterator方法获取");
            RocksIterator iter = db.newIterator();
            for (iter.seekToFirst(); iter.isValid(); iter.next()) {
                System.out.println(String.format("key:%s,value:%s",
                        new String(iter.key()), new String(iter.value())));
            }

        } catch (RocksDBException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testPrefixQuery() throws RocksDBException {
        String dbPath = "./target/test_db2";
        final Options options = new Options();
        options.setCreateIfMissing(true);
        final RocksDB rocksDB = RocksDB.open(options, dbPath);
        // key会进行排序，所以为了获取/test前缀的，应该先seek到/test前缀，然后判断如果不为/test前缀的那么就是最后一个了，
        // 不然会全部输出。
        // rocksdb支持向前和向后遍历的方式，通过seek + next和 seek + prev实现向前还是向后
        rocksDB.put("/tast/key1".getBytes(StandardCharsets.UTF_8), "value2".getBytes(StandardCharsets.UTF_8));
        rocksDB.put("/tbst/key1".getBytes(StandardCharsets.UTF_8), "value2".getBytes(StandardCharsets.UTF_8));
        rocksDB.put("/test/key1".getBytes(StandardCharsets.UTF_8), "value1".getBytes(StandardCharsets.UTF_8));
        rocksDB.put("/test/key2".getBytes(StandardCharsets.UTF_8), "value2".getBytes(StandardCharsets.UTF_8));
        rocksDB.put("/tfst/key1".getBytes(StandardCharsets.UTF_8), "value2".getBytes(StandardCharsets.UTF_8));
        rocksDB.put("/tgst/key1".getBytes(StandardCharsets.UTF_8), "value2".getBytes(StandardCharsets.UTF_8));

        final RocksIterator iter = rocksDB.newIterator();
        // 从头开始遍历
        // seek + next
        System.out.println("==================从头开始遍历===========================");
        for (iter.seekToFirst(); iter.isValid(); iter.next()) {
            System.out.println("iter key: " + new String(iter.key()) + ",iter value: " +
                    new String(iter.value()));
        }

        // 从/test前缀开始遍历
        System.out.println("==================从/test前缀开始遍历===========================");
        for (iter.seek("/test".getBytes(StandardCharsets.UTF_8)); iter.isValid(); iter.next()) {
            System.out.println("iter key: " + new String(iter.key()) + ",iter1 value: " +
                    new String(iter.value()));
        }

        // 从/test前缀前一个数据开始遍历
        System.out.println("==================从/test前缀前一个的数据开始遍历===========================");
        for (iter.seekForPrev("/test".getBytes(StandardCharsets.UTF_8)); iter.isValid(); iter.next()) {
            System.out.println("iter key: " + new String(iter.key()) + ",iter value: " +
                    new String(iter.value()));
        }

        // 从最后一个开始向前遍历（反向遍历）
        // seek + prev实现
        System.out.println("==================从最后一个开始向前遍历（反向遍历）===========================");
        for (iter.seekToLast(); iter.isValid(); iter.prev()) {
            System.out.println("iter key: " + new String(iter.key()) + ",iter value: " +
                    new String(iter.value()));
        }

        iter.close();
    }
}
