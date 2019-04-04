package com.tomgs.es.demo.dao;

import com.tomgs.es.demo.entity.Commodity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author tangzhongyuan
 * @create 2019-04-02 18:19
 **/
@Repository
public interface CommodityRepository extends ElasticsearchRepository<Commodity, String> {

}
