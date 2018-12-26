package com.pancm.test;

import com.pancm.dao.SchoolDao;
import com.pancm.pojo.School;
import com.pancm.util.ElasticsearchUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.util.StringUtils;

import java.util.List;


public class ESTest {
    @Autowired
    private SchoolDao schoolDao;

    public static void main(String[] args) {

    }

    @Test
    public void testElasticSearch(){
        // 客户名称  合同名称
        //String filter = "";
        String filter = "压测-现金贷产品-自动化-Automation4-20181213103634349";
        // 报表生成开始日期
        //Date beginPlanDate = DateUtil.parseDate("1948-11-12", DateUtil.FORMAT_PATTERN_DAY);
        String beginPlanDate = "";
        // 报表生成结束日期
        //Date endPlanDate = DateUtil.parseDate("1997-10-12", DateUtil.FORMAT_PATTERN_DAY);
        String endPlanDate = "";

        // 分页参数 pageSize为每一页的数量；pageNumber为页码
        int pageSize = 2;
        int pageNumber = 2;

        //创建对象，设置集群名称和IP地址
        ElasticsearchUtils es = new ElasticsearchUtils("elasticsearch",
                "192.168.199.239");
        String indexName = "university";//索引名称
        String typeName = "school";//类型名称
        String id = "1";
        String jsonData = "{" + "\"name\":\"压测-现金贷产品-自动化-Automation4-20181213103634349\","
                + "\"birth\":\"1990-10-12\"," + "\"email\":\"981162250@qq.com\""
                + "}";//json数据
        String id2 = "2";
        String jsonData2 = "{" + "\"name\":\"zhangsan_张冲新\","
                + "\"birth\":\"1960-10-12\"," + "\"email\":\"压测-现金贷产品-自动化-Automation4-20181213103634349\""
                + "}";//json数据
        String id3 = "3";
        String jsonData3 = "{" + "\"name\":\"wangyifan\","
                + "\"birth\":\"1950-10-12\"," + "\"email\":\"981162250@qq.com\""
                + "}";//json数据
        String id4 = "4";
        String jsonData4 = "{" + "\"name\":\"压测-现金贷产品-自动化-Automation4-20181213103634250\","
                + "\"birth\":\"1953-11-24\"," + "\"email\":\"704296853@qq.com\""
                + "}";//json数据
        String id5 = "5";
        String jsonData5 = "{" + "\"name\":\"压测-现金贷产品-自动化-Automation4-20181213103634349\","
                + "\"birth\":\"1958-11-23\"," + "\"email\":\"666696853@qq.com\""
                + "}";//json数据
        String id6 = "6";
        String jsonData6 = "{" + "\"name\":\"压测-现金贷产品-自动化-Automation4-20181213103634888\","
                + "\"birth\":\"1973-11-25\"," + "\"email\":\"7545296853@qq.com\""
                + "}";//json数据
        //1.创建索引(ID可自定义也可以自动创建，此处使用自定义ID)
        es.createIndex(indexName, typeName, id, jsonData);
        es.createIndex(indexName, typeName, id2, jsonData2);
        es.createIndex(indexName, typeName, id3, jsonData3);
        es.createIndex(indexName, typeName, id4, jsonData4);
        es.createIndex(indexName, typeName, id5, jsonData5);
        es.createIndex(indexName, typeName, id6, jsonData6);

        // 分页设置
        Pageable pageable = new PageRequest(pageNumber, pageSize);
        //2.执行查询
        //(1)创建查询条件
        BoolQueryBuilder mustQuery = QueryBuilders.boolQuery();
        if (!StringUtils.isEmpty(filter)){
            mustQuery.must(QueryBuilders.multiMatchQuery(filter , "name", "email").type(MultiMatchQueryBuilder.Type.PHRASE)); //  添加第1条must的条件 此处为匹配所有文档
        }
        if (!StringUtils.isEmpty(beginPlanDate) && !StringUtils.isEmpty(endPlanDate)){
            mustQuery.must(QueryBuilders.rangeQuery("birth").from(beginPlanDate).to(endPlanDate));
        }
        if (StringUtils.isEmpty(filter) && StringUtils.isEmpty(beginPlanDate) && StringUtils.isEmpty(beginPlanDate)){
            mustQuery.must(QueryBuilders.matchAllQuery());
        }
        //NativeSearchQueryBuilder
        //mustQuery.withPageable


        //SearchQuery searchQuery = new NativeSearchQueryBuilder().withPageable(pageable).withQuery(mustQuery).build();


        System.out.println("查询的语句:" + mustQuery.toString());
//        Page<School> searchPageResults = schoolDao.search(mustQuery, pageable);
//        List<School> schoolList = searchPageResults.getContent();
//        for (School school :schoolList){
//            String name = school.getName();
//            String birth = school.getBirth();
//            String email = school.getEmail();
//            System.out.println(name);
//            System.out.println(birth);
//            System.out.println(email);
//        }

        SearchResponse searchResponse = es.searcher(indexName, typeName,
                mustQuery, pageSize, pageNumber);
        //(3)解析结果
        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit searchHit : searchHits) {
            String name = (String) searchHit.getSource().get("name");
            String birth = (String) searchHit.getSource().get("birth");
            String email = (String) searchHit.getSource().get("email");
            System.out.println(name);
            System.out.println(birth);
            System.out.println(email);
        }

        //3.更新数据
        jsonData = "{" + "\"name\":\"zhengzhou\"," + "\"birth\":\"1978-01-30\","
                + "\"email\":\"zhengzhou@163.com\"" + "}";//json数据
        es.updateIndex(indexName, typeName, id, jsonData);

        //4.删除数据
        es.deleteIndex(indexName, typeName, id);
        es.deleteIndex(indexName, typeName, id2);
        es.deleteIndex(indexName, typeName, id3);
        es.deleteIndex(indexName, typeName, id4);
        es.deleteIndex(indexName, typeName, id5);
        es.deleteIndex(indexName, typeName, id6);
    }
    public void lastTestES(){
//        // 客户名称  合同名称
//        //String filter = "";
//        String filter = "压测-现金贷产品-自动化-Automation4-20181213103634349";
//        // 报表生成开始日期
//        //Date beginPlanDate = DateUtil.parseDate("1948-11-12", DateUtil.FORMAT_PATTERN_DAY);
//        String beginPlanDate = "";
//        // 报表生成结束日期
//        //Date endPlanDate = DateUtil.parseDate("1997-10-12", DateUtil.FORMAT_PATTERN_DAY);
//        String endPlanDate = "";
//
//        //创建对象，设置集群名称和IP地址
//        ElasticsearchUtils es = new ElasticsearchUtils("elasticsearch",
//                "192.168.199.239");
//        String indexName = "university";//索引名称
//        String typeName = "school";//类型名称
//        String id = "1";
//        String jsonData = "{" + "\"name\":\"压测-现金贷产品-自动化-Automation4-20181213103634349\","
//                + "\"birth\":\"1990-10-12\"," + "\"email\":\"981162250@qq.com\""
//                + "}";//json数据
//        String id2 = "2";
//        String jsonData2 = "{" + "\"name\":\"zhangsan_张冲新\","
//                + "\"birth\":\"1960-10-12\"," + "\"email\":\"压测-现金贷产品-自动化-Automation4-20181213103634349\""
//                + "}";//json数据
//        String id3 = "3";
//        String jsonData3 = "{" + "\"name\":\"wangyifan\","
//                + "\"birth\":\"1950-10-12\"," + "\"email\":\"981162250@qq.com\""
//                + "}";//json数据
//        String id4 = "4";
//        String jsonData4 = "{" + "\"name\":\"压测-现金贷产品-自动化-Automation4-20181213103634250\","
//                + "\"birth\":\"1953-11-24\"," + "\"email\":\"704296853@qq.com\""
//                + "}";//json数据
//        //1.创建索引(ID可自定义也可以自动创建，此处使用自定义ID)
//        es.createIndex(indexName, typeName, id, jsonData);
//        es.createIndex(indexName, typeName, id2, jsonData2);
//        es.createIndex(indexName, typeName, id3, jsonData3);
//        es.createIndex(indexName, typeName, id4, jsonData4);
//
//        //2.执行查询
//        //(1)创建查询条件
//        BoolQueryBuilder mustQuery = QueryBuilders.boolQuery();
//        if (!StringUtils.isEmpty(filter)){
//            mustQuery.must(QueryBuilders.multiMatchQuery(filter , "name", "email").type(MultiMatchQueryBuilder.Type.PHRASE)); //  添加第1条must的条件 此处为匹配所有文档
//        }
//        if (!StringUtils.isEmpty(beginPlanDate) && !StringUtils.isEmpty(endPlanDate)){
//            mustQuery.must(QueryBuilders.rangeQuery("birth").from(beginPlanDate).to(endPlanDate));
//        }
//        if (StringUtils.isEmpty(filter) && StringUtils.isEmpty(beginPlanDate) && StringUtils.isEmpty(beginPlanDate)){
//            mustQuery.must(QueryBuilders.matchAllQuery());
//        }
//
//
//
//
//
//        //QueryBuilder queryBuilder = QueryBuilders.rangeQuery("birth").from("1958-11-12").to("1997-10-12");
//        //QueryBuilder queryBuilder = QueryBuilders.multiMatchQuery("压测-消费贷产品-自动化-Automation4-20181213103634349", "name", "email").type(MultiMatchQueryBuilder.Type.PHRASE);
//        //QueryBuilder queryBuilder = QueryBuilders.matchQuery("name", "压word中文");//搜索name为kimchy的数据
//        //(2)执行查询
//        SearchResponse searchResponse = es.searcher(indexName, typeName,
//                mustQuery);
//        //(3)解析结果
//        SearchHits hits = searchResponse.getHits();
//        SearchHit[] searchHits = hits.getHits();
//        for (SearchHit searchHit : searchHits) {
//            String name = (String) searchHit.getSource().get("name");
//            String birth = (String) searchHit.getSource().get("birth");
//            String email = (String) searchHit.getSource().get("email");
//            System.out.println(name);
//            System.out.println(birth);
//            System.out.println(email);
//        }
//
//        //3.更新数据
//        jsonData = "{" + "\"name\":\"zhengzhou\"," + "\"birth\":\"1978-01-30\","
//                + "\"email\":\"zhengzhou@163.com\"" + "}";//json数据
//        es.updateIndex(indexName, typeName, id, jsonData);
//
//        //4.删除数据
//        es.deleteIndex(indexName, typeName, id);
//        es.deleteIndex(indexName, typeName, id2);
//        es.deleteIndex(indexName, typeName, id3);
//        es.deleteIndex(indexName, typeName, id4);
    }

    public void queryBuilderXXX(){
//        //创建对象，设置集群名称和IP地址
//        ElasticsearchUtils es = new ElasticsearchUtils("elasticsearch",
//                "192.168.83.150");
//        String indexName = "university";//索引名称
//        String typeName = "school";//类型名称
//        String id = "1";
//        String jsonData = "{" + "\"name\":\"压测-现金贷产品-自动化-Automation4-20181213103634349\","
//                + "\"birth\":\"1990-10-12\"," + "\"email\":\"981162250@qq.com\""
//                + "}";//json数据
//        String id2 = "2";
//        String jsonData2 = "{" + "\"name\":\"zhangsan_张冲新\","
//                + "\"birth\":\"1960-10-12\"," + "\"email\":\"压测-现金贷产品-自动化-Automation4-20181213103634349\""
//                + "}";//json数据
//        String id3 = "3";
//        String jsonData3 = "{" + "\"name\":\"wangyifan\","
//                + "\"birth\":\"1950-10-12\"," + "\"email\":\"981162250@qq.com\""
//                + "}";//json数据
//        //1.创建索引(ID可自定义也可以自动创建，此处使用自定义ID)
//        es.createIndex(indexName, typeName, id, jsonData);
//        es.createIndex(indexName, typeName, id2, jsonData2);
//        es.createIndex(indexName, typeName, id3, jsonData3);
//
//        //2.执行查询
//        //(1)创建查询条件
//        QueryBuilder queryBuilder = QueryBuilders.rangeQuery("birth").from("1958-11-12").to("1997-10-12");
//        //QueryBuilder queryBuilder = QueryBuilders.multiMatchQuery("压测-消费贷产品-自动化-Automation4-20181213103634349", "name", "email").type(MultiMatchQueryBuilder.Type.PHRASE);
//        //QueryBuilder queryBuilder = QueryBuilders.matchQuery("name", "压word中文");//搜索name为kimchy的数据
//        //(2)执行查询
//        SearchResponse searchResponse = es.searcher(indexName, typeName,
//                queryBuilder);
//        //(3)解析结果
//        SearchHits hits = searchResponse.getHits();
//        SearchHit[] searchHits = hits.getHits();
//        for (SearchHit searchHit : searchHits) {
//            String name = (String) searchHit.getSource().get("name");
//            String birth = (String) searchHit.getSource().get("birth");
//            String email = (String) searchHit.getSource().get("email");
//            System.out.println(name);
//            System.out.println(birth);
//            System.out.println(email);
//        }
//
//        //3.更新数据
//        jsonData = "{" + "\"name\":\"zhengzhou\"," + "\"birth\":\"1978-01-30\","
//                + "\"email\":\"zhengzhou@163.com\"" + "}";//json数据
//        es.updateIndex(indexName, typeName, id, jsonData);
//
//        //4.删除数据
//        es.deleteIndex(indexName, typeName, id);
//        es.deleteIndex(indexName, typeName, id2);
//        es.deleteIndex(indexName, typeName, id3);
    }


    public void queryBuliderBymultiMatchAndRang(){
//        //创建对象，设置集群名称和IP地址
//        ElasticsearchUtils es = new ElasticsearchUtils("elasticsearch",
//                "192.168.83.150");
//        String indexName = "university";//索引名称
//        String typeName = "school";//类型名称
//        String id = "1";
//        String jsonData = "{" + "\"name\":\"压测-现金贷产品-自动化-Automation4-20181213103634349\","
//                + "\"birth\":\"1990-10-12\"," + "\"email\":\"981162250@qq.com\""
//                + "}";//json数据
//        String id2 = "2";
//        String jsonData2 = "{" + "\"name\":\"zhangsan_张冲新\","
//                + "\"birth\":\"1960-10-12\"," + "\"email\":\"压测-现金贷产品-自动化-Automation4-20181213103634349\""
//                + "}";//json数据
//        String id3 = "3";
//        String jsonData3 = "{" + "\"name\":\"wangyifan\","
//                + "\"birth\":\"1950-10-12\"," + "\"email\":\"981162250@qq.com\""
//                + "}";//json数据
//        //1.创建索引(ID可自定义也可以自动创建，此处使用自定义ID)
//        es.createIndex(indexName, typeName, id, jsonData);
//        es.createIndex(indexName, typeName, id2, jsonData2);
//        es.createIndex(indexName, typeName, id3, jsonData3);
//
//        //2.执行查询
//        //(1)创建查询条件
//        BoolQueryBuilder mustQuery = QueryBuilders.boolQuery();
//        mustQuery.must(QueryBuilders.multiMatchQuery("压测-现金贷产品-自动化-Automation4-20181213103634349", "name", "email").type(MultiMatchQueryBuilder.Type.PHRASE)); //  添加第1条must的条件 此处为匹配所有文档
//        mustQuery.must(QueryBuilders.rangeQuery("birth").from("1958-11-12").to("1997-10-12"));
//
//
//
//
//        //QueryBuilder queryBuilder = QueryBuilders.rangeQuery("birth").from("1958-11-12").to("1997-10-12");
//        //QueryBuilder queryBuilder = QueryBuilders.multiMatchQuery("压测-消费贷产品-自动化-Automation4-20181213103634349", "name", "email").type(MultiMatchQueryBuilder.Type.PHRASE);
//        //QueryBuilder queryBuilder = QueryBuilders.matchQuery("name", "压word中文");//搜索name为kimchy的数据
//        //(2)执行查询
//        SearchResponse searchResponse = es.searcher(indexName, typeName,
//                mustQuery);
//        //(3)解析结果
//        SearchHits hits = searchResponse.getHits();
//        SearchHit[] searchHits = hits.getHits();
//        for (SearchHit searchHit : searchHits) {
//            String name = (String) searchHit.getSource().get("name");
//            String birth = (String) searchHit.getSource().get("birth");
//            String email = (String) searchHit.getSource().get("email");
//            System.out.println(name);
//            System.out.println(birth);
//            System.out.println(email);
//        }
//
//        //3.更新数据
//        jsonData = "{" + "\"name\":\"zhengzhou\"," + "\"birth\":\"1978-01-30\","
//                + "\"email\":\"zhengzhou@163.com\"" + "}";//json数据
//        es.updateIndex(indexName, typeName, id, jsonData);
//
//        //4.删除数据
//        es.deleteIndex(indexName, typeName, id);
//        es.deleteIndex(indexName, typeName, id2);
//        es.deleteIndex(indexName, typeName, id3);
    }
}
