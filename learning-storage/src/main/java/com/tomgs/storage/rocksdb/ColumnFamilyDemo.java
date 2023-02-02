package com.tomgs.storage.rocksdb;

import org.junit.Test;
import org.rocksdb.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ColumnFamilyDemo
 *
 * @author tomgs
 * @since 2022/3/22
 */
public class ColumnFamilyDemo extends BaseDemo {

    /**
     * // 使用特定的列族打开数据库，可以把列族理解为关系型数据库中的表(table)
     */
    @Test
    public void testCustomColumnFamily() {
        String cfdbPath = "./target/data-cf/";
        System.out.println("测试自定义的列簇...");
        try (final ColumnFamilyOptions cfOpts = new ColumnFamilyOptions()
                .optimizeLevelStyleCompaction()) {
            String cfName = "cf";
            // list of column family descriptors, first entry must always be default column family
            final List<ColumnFamilyDescriptor> cfDescriptors = Arrays.asList(
                    new ColumnFamilyDescriptor(RocksDB.DEFAULT_COLUMN_FAMILY, cfOpts),
                    new ColumnFamilyDescriptor(cfName.getBytes(), cfOpts)
            );

            List<ColumnFamilyHandle> cfHandles = new ArrayList<>();
            try (final DBOptions dbOptions = new DBOptions()
                    //.setIncreaseParallelism()
                    .setCreateIfMissing(true)
                    .setCreateMissingColumnFamilies(true);
                 final RocksDB db = RocksDB.open(dbOptions, cfdbPath, cfDescriptors, cfHandles)) {

                ColumnFamilyHandle cfHandle = cfHandles.stream()
                        .filter(x -> {
                            try {
                                return (new String(x.getName())).equals(cfName);
                            } catch (RocksDBException e) {
                                return false;
                            }
                        })
                        .collect(Collectors.toList())
                        .get(0);

                try {
                    // put and get from non-default column family
                    db.put(cfHandles.get(1), new WriteOptions(), "key".getBytes(), "value".getBytes());

                    // atomic write
                    try (final WriteBatch wb = new WriteBatch()) {
                        wb.put(cfHandles.get(0), "key2".getBytes(),
                                "value2".getBytes());
                        wb.put(cfHandles.get(1), "key3".getBytes(),
                                "value3".getBytes());
//                        wb.delete(cfHandles.get(1), "key".getBytes());
                        db.write(new WriteOptions(), wb);
                    }

                    System.out.println("newIterator方法获取");
                    //如果不传columnFamilyHandle，则获取默认的列簇，如果传了columnFamilyHandle，则获取指定列簇的
                    RocksIterator iter = db.newIterator(cfHandles.get(1));
                    for (iter.seekToFirst(); iter.isValid(); iter.next()) {
                        System.out.println(String.format("key:%s,value:%s",
                                new String(iter.key()), new String(iter.value())));
                    }

                    // drop column family
                    db.dropColumnFamily(cfHandles.get(1));

                } finally {
                    for (final ColumnFamilyHandle handle : cfHandles) {
                        handle.close();
                    }
                }
            }
        } catch (RocksDBException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testColumFamilyDemo2() {
        String cfdbPath = "./target/data-cf/";
        System.out.println("测试自定义的列簇...");
        try (final ColumnFamilyOptions cfOpts = new ColumnFamilyOptions().optimizeLevelStyleCompaction()) {
            String cfName = "cf";
            // list of column family descriptors, first entry must always be default column family
            final List<ColumnFamilyDescriptor> cfDescriptors = Arrays.asList(
                    new ColumnFamilyDescriptor(RocksDB.DEFAULT_COLUMN_FAMILY, cfOpts),
                    new ColumnFamilyDescriptor(cfName.getBytes(), cfOpts)
            );

            List<ColumnFamilyHandle> cfHandles = new ArrayList<>();
            try (final DBOptions dbOptions = new DBOptions()
                    //.setIncreaseParallelism()
                    .setCreateIfMissing(true)
                    .setCreateMissingColumnFamilies(true);
                 final RocksDB db = RocksDB.open(dbOptions, cfdbPath, cfDescriptors, cfHandles)) {
                try {
                    // put and get from non-default column family
                    db.put(cfHandles.get(1), new WriteOptions(), "key".getBytes(), "value".getBytes());

                    // create table
                    final ColumnFamilyHandle columnFamilyHandle = db.createColumnFamily(new ColumnFamilyDescriptor("table1".getBytes(), cfOpts));
                    db.put(columnFamilyHandle, new WriteOptions(), "key".getBytes(), "value1".getBytes());

                    final byte[] defaultValue = db.get(cfHandles.get(1), "key".getBytes());
                    final byte[] customValue = db.get(columnFamilyHandle, "key".getBytes());

                    System.out.println("defaultValue: " + new String(defaultValue));
                    System.out.println("customValue: " + new String(customValue));

                    // drop column family
                    db.dropColumnFamily(cfHandles.get(1));

                    final byte[] deleteValue = db.get(cfHandles.get(1), "key".getBytes());
                    System.out.println("deleteValue: " + new String(deleteValue));
                } finally {
                    for (final ColumnFamilyHandle handle : cfHandles) {
                        handle.close();
                    }
                }
            }
        } catch (RocksDBException e) {
            e.printStackTrace();
        }
    }

}
