package com.tomgs.storage.etcd;

import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.Client;
import io.etcd.jetcd.KV;
import io.etcd.jetcd.kv.GetResponse;
import io.etcd.jetcd.test.EtcdClusterExtension;
import org.junit.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * EtcdDemo
 *
 * https://github.com/etcd-io/jetcd
 *
 * @author tomgs
 * @since 2022/3/30
 */
public class EtcdDemo {

    @RegisterExtension
    public static final EtcdClusterExtension cluster = EtcdClusterExtension.builder()
            .withNodes(1)
            .build();
    Client client = Client.builder().endpoints(cluster.clientEndpoints()).build();

    @Test
    public void testConnect() throws ExecutionException, InterruptedException {
        // create client using endpoints
        //Client client = Client.builder().endpoints("http://etcd0:2379", "http://etcd1:2379", "http://etcd2:2379").build();
        // create client using target which enable using any name resolution mechanism provided
        // by grpc-java (i.e. dns:///foo.bar.com:2379)
        // Client client = Client.builder().target("ip:///etcd0:2379,etcd1:2379,etcd2:2379").build();

        KV kvClient = client.getKVClient();
        ByteSequence key = ByteSequence.from("test_key".getBytes());
        ByteSequence value = ByteSequence.from("test_value".getBytes());

        // put the key-value
        kvClient.put(key, value).get();

        // get the CompletableFuture
        CompletableFuture<GetResponse> getFuture = kvClient.get(key);

        // get the value from CompletableFuture
        GetResponse response = getFuture.get();

        // delete the key
        kvClient.delete(key).get();
    }
    
}
