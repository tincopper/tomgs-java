package com.tomgs.es.explain;

import org.apache.lucene.search.Explanation;

/**
 * @author tangzhongyuan
 * @since 2019-04-04 18:35
 **/
public interface ExplainService {

    Explanation queryExplain(String indexName, String type, String id, String fieldName, String fieldValue);
}
