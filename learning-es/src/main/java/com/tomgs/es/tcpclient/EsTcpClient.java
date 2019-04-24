package com.tomgs.es.tcpclient;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * @author tangzhongyuan
 * @since 2019-04-04 18:23
 **/
@Component
public class EsTcpClient {

    @Autowired
    private TransportClient client;

    public IndexResponse prepareIndex(String index, String type, String id, Map<String, String> sourceMap) {
        return client.prepareIndex(index, type, id)
                .setSource(sourceMap, XContentType.JSON).get();
    }

    public GetResponse prepareGet(String index, String type, String id) {
        return client.prepareGet(index, type, id).get();
    }

    public DeleteResponse prepareDelete(String index, String type, String id) {
        return client.prepareDelete(index, type, id).get();
    }

    public long deleteByQuery(String index, String key, String value) {
        BulkByScrollResponse bulkByScrollResponse = DeleteByQueryAction.INSTANCE.newRequestBuilder(client)
                .filter(QueryBuilders.matchQuery(key, value)).source(index).get();
        return bulkByScrollResponse.getDeleted();
    }

    public UpdateResponse prepareUpdate(String index, String type, String id, String updateKey, String updateValue) throws IOException {
        UpdateRequestBuilder updateRequestBuilder = client.prepareUpdate(index, type, id).setDoc(jsonBuilder()
                .startObject().field(updateKey, updateValue).endObject());
        return updateRequestBuilder.get();
    }
}
