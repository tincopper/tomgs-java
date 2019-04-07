package com.tomgs.es.explain;

import org.apache.lucene.search.Explanation;
import org.elasticsearch.action.explain.ExplainRequestBuilder;
import org.elasticsearch.action.explain.ExplainResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author tangzhongyuan
 * @create 2019-04-04 18:36
 **/
@Service
public class ExplainServiceImpl implements ExplainService {

    @Autowired
    private TransportClient client;

    public Explanation queryExplain(String indexName, String type, String id, String fieldName, String fieldValue) {
        ExplainRequestBuilder explainRequestBuilder = client.prepareExplain(indexName, type, id);
        ExplainResponse explainResponse = explainRequestBuilder.setQuery(QueryBuilders.termQuery(fieldName, fieldValue))
                .execute().actionGet();
        Explanation explanation = explainResponse.getExplanation();
        return explanation;
    }
}
