package com.tomgs.es.demo.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;

/**
 * @author tangzhongyuan
 * @create 2019-04-02 18:13
 **/
@Data
@Document(indexName = "commodity")
public class Commodity implements Serializable {

    @Id
    private String skuId;

    private String name;

    private String category;

    private Integer price;

    private String brand;

    private Integer stock;

}
