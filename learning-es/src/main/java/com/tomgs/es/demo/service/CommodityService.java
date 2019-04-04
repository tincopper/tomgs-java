package com.tomgs.es.demo.service;

import com.tomgs.es.demo.entity.Commodity;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author tangzhongyuan
 * @create 2019-04-02 18:22
 **/
public interface CommodityService {

    long count();

    Commodity save(Commodity commodity);

    void delete(Commodity commodity);

    Iterable<Commodity> getAll();

    List<Commodity> getByName(String name);

    Page<Commodity> pageQuery(Integer pageNo, Integer pageSize, String kw);
}
