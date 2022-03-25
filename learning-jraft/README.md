OzoneManager写操作流程：
对于一个写操作先是写到缓存当中（使用了一个双缓存的方式实现->OzoneManagerDoubleBuffer），
然后OzoneManagerDoubleBuffer当中有一个定时线程定时刷盘将缓存当中readyBuffer的数据写入到rocksdb当中去并且更新当前的事务index和term。

刷盘过程：
先将currentBuffer切换为readyBuffer（交换两个值），然后更新数据到rocksdb，更新数据成功之后再更新当前的事务index和term。然后清空readyBuffer。
再更新快照的事务index
```java
// update the last updated index in OzoneManagerStateMachine.
ozoneManagerRatisSnapShot.updateLastAppliedIndex(flushedEpochs);
```

重启/异常恢复流程：
从快照OzoneManagerRatisSnapshot当中恢复到OzoneManagerDoubleBuffer当中。
从快照恢复：stateMachine: reinitialize -> loadSnapshotInfoFromDB
生成快照：stateMachine: takeSnapshot ->
Table<String, TransactionInfo> txnInfoTable = ozoneManager.getMetadataManager().getTransactionInfoTable();
txnInfoTable.put(TRANSACTION_INFO_KEY, build);
ozoneManager.getMetadataManager().getStore().flushDB();