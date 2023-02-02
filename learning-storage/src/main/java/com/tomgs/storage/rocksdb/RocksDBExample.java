package com.tomgs.storage.rocksdb;

import org.rocksdb.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * RocksDBExample
 *
 * @author tomgs
 * @since 1.0
 */
public class RocksDBExample {

    private static final String dbPath   = "./target/rocksdb-data/";
    private static final String cfdbPath = "./target/rocksdb-data-cf/";

    static {
        RocksDB.loadLibrary();
    }

    //  RocksDB.DEFAULT_COLUMN_FAMILY
    public void testDefaultColumnFamily() {
        System.out.println("testDefaultColumnFamily begin...");
        // 文件不存在，则先创建文件
        try (final Options options = new Options().setCreateIfMissing(true)) {
            try (final RocksDB rocksDB = RocksDB.open(options, dbPath)) {
                // 简单key-value
                byte[] key = "Hello".getBytes();
                rocksDB.put(key, "World".getBytes());

                System.out.println(new String(rocksDB.get(key)));

                rocksDB.put("SecondKey".getBytes(), "SecondValue".getBytes());

                // 通过List做主键查询
                List<byte[]> keys = Arrays.asList(key, "SecondKey".getBytes(), "missKey".getBytes());
                List<byte[]> values = rocksDB.multiGetAsList(keys);
                for (int i = 0; i < keys.size(); i++) {
                    System.out.println("multiGet " + new String(keys.get(i)) + ":" + (values.get(i) != null ? new String(values.get(i)) : null));
                }

                // 打印全部[key - value]
                RocksIterator iter = rocksDB.newIterator();
                for (iter.seekToFirst(); iter.isValid(); iter.next()) {
                    System.out.println("iterator key:" + new String(iter.key()) + ", iter value:" + new String(iter.value()));
                }

                // 删除一个key
                rocksDB.delete(key);
                System.out.println("after remove key:" + new String(key));

                iter = rocksDB.newIterator();
                for (iter.seekToFirst(); iter.isValid(); iter.next()) {
                    System.out.println("iterator key:" + new String(iter.key()) + ", iter value:" + new String(iter.value()));
                }
            }
        } catch (RocksDBException e) {
            e.printStackTrace();
        }
    }

    // 使用特定的列族打开数据库，可以把列族理解为关系型数据库中的表(table)
    public void testCertainColumnFamily() {
        System.out.println("\ntestCertainColumnFamily begin...");
        try (final ColumnFamilyOptions cfOpts = new ColumnFamilyOptions().optimizeLevelStyleCompaction()) {
            String cfName = "my-first-columnfamily";
            // list of column family descriptors, first entry must always be default column family
            final List<ColumnFamilyDescriptor> cfDescriptors = Arrays.asList(
                    new ColumnFamilyDescriptor(RocksDB.DEFAULT_COLUMN_FAMILY, cfOpts),
                    new ColumnFamilyDescriptor(cfName.getBytes(), cfOpts)
            );

            List<ColumnFamilyHandle> cfHandles = new ArrayList<>();
            try (final DBOptions dbOptions = new DBOptions().setCreateIfMissing(true).setCreateMissingColumnFamilies(true);
                 final RocksDB rocksDB = RocksDB.open(dbOptions, cfdbPath, cfDescriptors, cfHandles)) {
                ColumnFamilyHandle cfHandle = cfHandles.stream().filter(x -> {
                    try {
                        return (new String(x.getName())).equals(cfName);
                    } catch (RocksDBException e) {
                        return false;
                    }
                }).collect(Collectors.toList()).get(0);

                // 写入key/value
                String key = "FirstKey";
                rocksDB.put(cfHandle, key.getBytes(), "FirstValue".getBytes());
                // 查询单key
                byte[] getValue = rocksDB.get(cfHandle, key.getBytes());
                System.out.println("get Value : " + new String(getValue));
                // 写入第2个key/value
                rocksDB.put(cfHandle, "SecondKey".getBytes(), "SecondValue".getBytes());

                List<byte[]> keys = Arrays.asList(key.getBytes(), "SecondKey".getBytes());
                List<ColumnFamilyHandle> cfHandleList = Arrays.asList(cfHandle, cfHandle);
                // 查询多个key
                List<byte[]> values = rocksDB.multiGetAsList(cfHandleList, keys);
                for (int i = 0; i < keys.size(); i++) {
                    System.out.println("multiGet:" + new String(keys.get(i)) + "--" + (values.get(i) == null ? null : new String(values.get(i))));
                }

                // 删除单key
                rocksDB.delete(cfHandle, key.getBytes());

                RocksIterator iter = rocksDB.newIterator(cfHandle);
                for (iter.seekToFirst(); iter.isValid(); iter.next()) {
                    System.out.println("iterator:" + new String(iter.key()) + ":" + new String(iter.value()));
                }
            } finally {
                // NOTE frees the column family handles before freeing the db
                for (final ColumnFamilyHandle cfHandle : cfHandles) {
                    cfHandle.close();
                }
            }
        } catch (RocksDBException e) {
            e.printStackTrace();
        } // frees the column family options
    }

    public static void main(String[] args) throws Exception {
        RocksDBExample test = new RocksDBExample();
        test.testDefaultColumnFamily();
        test.testCertainColumnFamily();
    }

}
