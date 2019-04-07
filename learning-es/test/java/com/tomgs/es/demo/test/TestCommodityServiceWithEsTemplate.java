package com.tomgs.es.demo.test;

import com.tomgs.es.demo.entity.Commodity;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author tangzhongyuan
 * @create 2019-04-04 18:16
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestCommodityServiceWithEsTemplate {

    /**
     * 这个有springboot自动配置
     */
    @Autowired
    public ElasticsearchTemplate elasticsearchTemplate;

    @Test
    public void testInsert() {
        Commodity commodity = new Commodity();
        commodity.setSkuId("1501009005");
        commodity.setName("葡萄吐司面包（10片装）");
        commodity.setCategory("101");
        commodity.setPrice(160);
        commodity.setBrand("良品铺子");

        IndexQuery indexQuery = new IndexQueryBuilder().withObject(commodity).build();
        elasticsearchTemplate.index(indexQuery);
    }

    @Test
    public void testQuery() {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery("name", "吐司"))
                .build();
        List<Commodity> list = elasticsearchTemplate.queryForList(searchQuery, Commodity.class);
        System.out.println(list);
    }

}
