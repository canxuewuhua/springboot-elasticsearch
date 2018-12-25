package com.pancm.test;

import com.pancm.util.ElasticsearchUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

public class TestReport {
    public static void main(String[] args) {
        //创建对象，设置集群名称和IP地址
        ElasticsearchUtils es = new ElasticsearchUtils("elasticsearch",
                "192.168.83.150");
        String indexName = "report";//索引名称
        String typeName = "loandetailreport";//类型名称
        QueryBuilder queryBuilder = QueryBuilders.matchPhraseQuery("contract_name", "压测-现金贷产品-自动化-Automation4-20181213103634349");//搜索name为kimchy的数据
        //(2)执行查询
        SearchResponse searchResponse = es.searcher(indexName, typeName,
                queryBuilder);
        //(3)解析结果
        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit searchHit : searchHits) {
            String contractName = (String) searchHit.getSource().get("contract_name");
            String productName = (String) searchHit.getSource().get("product_name");
            String customerName = (String) searchHit.getSource().get("customer_name");
            System.out.println(contractName);
            System.out.println(productName);
            System.out.println(customerName);
        }
    }
}
