package com.tomgs.ratis.kv.storage;

import com.tomgs.ratis.kv.exception.StorageDBException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;

import java.io.File;
import java.io.IOException;

/**
 * RocksDBStore
 *
 * @author tomgs
 * @since 2022/3/24
 */
@Slf4j
public class RocksDBStore implements DBStore {

    static {
        RocksDB.loadLibrary();
    }

    private final StorageOptions storageOptions;

    private RocksDB rocksDB;

    public RocksDBStore(final StorageOptions storageOptions) {
        this.storageOptions = storageOptions;
    }

    @Override
    public void init() {
        final File dbPath = storageOptions.getStoragePath();
        final Options options = new Options();
        options.setCreateIfMissing(true);
        try {
            final File dbDir = new File(dbPath, storageOptions.getClusterName());
            if (!dbDir.exists()) {
                try {
                    FileUtils.forceMkdir(dbDir);
                } catch (IOException e) {
                    throw new StorageDBException(e.getMessage(), e);
                }
            }
            rocksDB = RocksDB.open(options, dbDir.getPath());
        } catch (RocksDBException e) {
            throw new StorageDBException("Init DB exception: " + e.getMessage(), e);
        }
    }

    @Override
    public byte[] get(byte[] key) {
        try {
            return rocksDB.get(key);
        } catch (RocksDBException e) {
            throw new StorageDBException("GET exception: " + e.getMessage(), e);
        }
    }

    @Override
    public void put(byte[] key, byte[] value) {
        try {
            rocksDB.put(key, value);
        } catch (RocksDBException e) {
            throw new StorageDBException("PUT exception: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(byte[] key) {
        try {
            rocksDB.delete(key);
        } catch (RocksDBException e) {
            throw new StorageDBException("PUT exception: " + e.getMessage(), e);
        }
    }

    @Override
    public void close() {
        rocksDB.close();
    }

}
