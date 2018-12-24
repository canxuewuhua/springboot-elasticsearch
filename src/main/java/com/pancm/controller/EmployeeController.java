package com.pancm.controller;

import com.google.gson.Gson;
import com.pancm.dao.EmployeeRepository;
import com.pancm.pojo.Employee;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author linzhiqiang
 * TermQueryBuilder 对词条进行完全匹配   计算机=计算机
 * WildcardQueryBuilder  对词条进行模糊匹配  "计算机" =  *算*
 * QueryStringQueryBuilder 对查询的词进行分词 再进行匹配
 */
@RestController
@RequestMapping("es")
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    /**
     * 添加
     * @return
     */
    @RequestMapping("add")
    public String add() {
        Employee employee = new Employee();
        employee.setId("3");
        employee.setFirstName("wenshan");
        employee.setLastName("fang");
        employee.setAge(30);
        employee.setAbout("i am in taiwan");
        employeeRepository.save(employee);
        System.err.println("add a wenshan");
        return "success";
    }

    /**
     * 删除
     * @return
     */
    @RequestMapping("delete")
    public String delete() {
        Employee employee = employeeRepository.queryEmployeeById("1");
        employeeRepository.delete(employee);
        return "success";
    }

    /**
     * 局部更新
     * @return
     */
    @RequestMapping("update")
    public String update() {
        Employee employee = employeeRepository.queryEmployeeById("2");
        employee.setFirstName("meimei");
        employeeRepository.save(employee);
        System.err.println("update a obj");
        return "success";
    }
    /**
     * 查询
     * @return
     */
    @RequestMapping("query")
    public Employee query() {
        Employee accountInfo = employeeRepository.queryEmployeeById("2");
        System.err.println(new Gson().toJson(accountInfo));
        return accountInfo;
    }

    /**
     * 查询
     * @return
     */
    @RequestMapping("queryByContent")
    public List<Employee> queryByContent(String searchContent) {
        log.info("searchContent:{}", searchContent);
        QueryStringQueryBuilder builder = new QueryStringQueryBuilder(searchContent);
        log.info("查询的语句:{}", builder);
        Iterable<Employee> searchResult = employeeRepository.search(builder);
        Iterator<Employee> iterator = searchResult.iterator();
        List<Employee> list=new ArrayList<Employee>();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        log.info("list的内容：{}", list);
        return list;
    }

    /**
     * 查询API
     * 基本查询、多词条查询、matchAll查询、常用词查询、multiMatch查询、queryString查询、 simpleQueryString查询、前缀查询、模糊查询、通配符查询、 Range查询
     */
    @RequestMapping("queryBuilder")
    public String queryBuilder() {
        log.info("查询API...");

        // 基本查询
        QueryBuilder query = QueryBuilders.matchPhraseQuery("firstName", "meimei");
        // 多词条查询
        QueryBuilder query1 = QueryBuilders.termsQuery("addr", "Beijing", "Shanghai");
        // matchAll查询
        QueryBuilder query2 = QueryBuilders.matchAllQuery();
        // 常用词查询
        QueryBuilder query3 = QueryBuilders.commonTermsQuery("addr", "Beijing");
        // multiMatch查询
        QueryBuilder query4 = QueryBuilders.multiMatchQuery("Beijing", "addr", "name");
        // queryString查询
        QueryBuilder query5 = QueryBuilders.queryStringQuery("Beijing");
        // simpleQueryString查询
        QueryBuilder query6 = QueryBuilders.simpleQueryStringQuery("Beijing");
        // 前缀查询
        QueryBuilder query7 = QueryBuilders.prefixQuery("addr", "Bei");
        // 模糊查询
        QueryBuilder query8 = QueryBuilders.fuzzyQuery("addr", "BeiJing");
        // 通配符查询
        QueryBuilder query9 = QueryBuilders.wildcardQuery("addr", "Bei*");
        // 闭区间
        QueryBuilder query10 = QueryBuilders.rangeQuery("age").from(10).to(20);
        // 开区间
        QueryBuilder query11 = QueryBuilders.rangeQuery("age").gt(10).lt(20);

        return query.getName();
    }
}