package com.study;

import com.alibaba.fastjson.JSON;
import com.study.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class EsApiTest {
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Test
    public void createIndex() {
        CreateIndexRequest createIndexRequest = new CreateIndexRequest("ceshi");
        CreateIndexResponse createIndexResponse = null;
        try {
            createIndexResponse = restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(createIndexResponse.toString());
    }

    @Test
    public void addIndex(){
        IndexRequest request = new IndexRequest("ceshi").id("1");
        request.timeout("1s");
        User user = User.builder().id(1L).name("haha").address("北京市").build();
        request.source(JSON.toJSONString(user), XContentType.JSON);
        try {
            IndexResponse res = restHighLevelClient.index(request, RequestOptions.DEFAULT);
            log.info("返回信息{}", JSON.toJSONString(res));
        } catch (IOException e) {
            log.error("向索引中添加数据失败", e);
        }
        log.info("添加成功{}", user);
    }

    @Test
    public void getIndex(){
        GetRequest request = new GetRequest("ceshi", "1");
        GetResponse documentFields = null;
        try {
            documentFields = restHighLevelClient.get(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("获取信息失败", e);
        }
        log.info("查询到的数据：【{}】",documentFields.getSourceAsString());
    }

    @Test
    public void search() {
        SearchRequest request = new SearchRequest("ceshi");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("address", "北京市");
        searchSourceBuilder.query(matchQueryBuilder);
        request.source(searchSourceBuilder);
        SearchResponse response = null;
        try {
            response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("查询失败", e);
        }
        log.info("查询结果【{}】", JSON.toJSONString(response));
    }
}
