package com.tomgs.ratis.kv.storage;

import lombok.Builder;
import lombok.Data;

import java.io.File;

/**
 * StoreOptions
 *
 * @author tomgs
 * @since 2022/3/24
 */
@Data
@Builder
public class StorageOptions {

    private String clusterName;

    private StorageType storageType;

    private File storagePath;

}
