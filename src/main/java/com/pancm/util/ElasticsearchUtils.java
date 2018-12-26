package com.pancm.util;

import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ElasticsearchUtils {

    private TransportClient  client;

    public ElasticsearchUtils(String clusterName, String ipAddress) {

        try {
            Settings settings = Settings
                    .settingsBuilder()
                    //设置集群名称
                    .put("cluster.name", clusterName)
                    .put("client.transport.ignore_cluster_name", false)
                    .put("node.client", true).put("client.transport.sniff", true)
                    .build();
            //此处端口号为9300
            client =TransportClient.builder().settings(settings).build()
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(ipAddress),
                            9300));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建索引
     * @param indexName 索引名称，相当于数据库名称
     * @param typeName 索引类型，相当于数据库中的表名
     * @param id id名称，相当于每个表中某一行记录的标识
     * @param jsonData json数据
     */
    public void createIndex(String indexName, String typeName, String id,
                            String jsonData) {
        IndexRequestBuilder requestBuilder = client.prepareIndex(indexName,
                typeName, id).setRefresh(true);//设置索引名称，索引类型，id
        requestBuilder.setSource(jsonData).execute().actionGet();//创建索引
    }

    /**
     * 执行搜索
     * @param indexname 索引名称
     * @param type 索引类型
     * @param queryBuilder 查询条件
     * @return
     */
    public SearchResponse   searcher(String indexName, String typeName,
                                   QueryBuilder queryBuilder, int pageSize, int pageNumber) {
        SearchResponse searchResponse = client.prepareSearch(indexName)
                .setTypes(typeName).setFrom(pageSize*(pageNumber-1)).setSize(pageSize).setQuery(queryBuilder).execute()
                .actionGet();//执行查询
        return searchResponse;
    }

    /**
     * 更新索引
     * @param indexName 索引名称
     * @param typeName 索引类型
     * @param id id名称
     * @param jsonData json数据
     */
    public void updateIndex(String indexName, String typeName, String id,
                            String jsonData) {
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.index(indexName);//设置索引名称
        updateRequest.id(id);//设置id
        updateRequest.type(typeName);//设置索引类型
        updateRequest.doc(jsonData);//更新数据
        client.update(updateRequest).actionGet();//执行更新
    }

    /**
     * 删除索引
     * @param indexName
     * @param typeName
     * @param id
     */
    public void deleteIndex(String indexName, String typeName, String id) {
        client.prepareDelete(indexName, typeName, id).get();
    }
}
