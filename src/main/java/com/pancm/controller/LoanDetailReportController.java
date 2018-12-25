package com.pancm.controller;

import com.google.gson.Gson;
import com.pancm.dao.ReportRepository;
import com.pancm.pojo.LoanDetailReport;
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

@RestController
@RequestMapping("report")
@Slf4j
public class LoanDetailReportController {

    @Autowired
    private ReportRepository reportRepository;

    /**
     * 查询
     * @return
     */
    @RequestMapping("queryByContent")
    public List<LoanDetailReport> queryByContent(String searchContent) {
        log.info("searchContent:{}", searchContent);
        QueryStringQueryBuilder builder = new QueryStringQueryBuilder(searchContent);
        log.info("查询的语句:{}", builder);
        Iterable<LoanDetailReport> searchResult = reportRepository.search(builder);
        Iterator<LoanDetailReport> iterator = searchResult.iterator();
        List<LoanDetailReport> list=new ArrayList<LoanDetailReport>();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        log.info("list的内容：{}", list);
        return list;
    }

    /**
     * 查询
     * @return
     */
    @RequestMapping("query")
    public List<LoanDetailReport> query() {
        QueryBuilder builder = QueryBuilders.matchAllQuery();
        log.info("查询的语句:{}", builder);
        Iterable<LoanDetailReport> searchResult = reportRepository.search(builder);
        Iterator<LoanDetailReport> iterator = searchResult.iterator();
        List<LoanDetailReport> list=new ArrayList<LoanDetailReport>();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        log.info("list的内容：{}", list);
        return list;
    }
    /**
     * 精确查询
     * @return
     */
    @RequestMapping("queryByAccurateContent")
    public List<LoanDetailReport> queryByAccurateContent(String searchContent) {
        log.info("searchContent:{}", searchContent);

        QueryBuilder builder = QueryBuilders.matchPhraseQuery("contract_name", "");


        //QueryBuilder builder = QueryBuilders.termQuery("contract_name", "压测-现金贷产品-自动化-Automation4-20181213100955222");
        //QueryBuilder builder = QueryBuilders.termQuery("contract_name", "压测-现金贷产品-自动化-Automation4-20181213100955222");
        log.info("查询的语句:{}", builder);
        Iterable<LoanDetailReport> searchResult = reportRepository.search(builder);
        Iterator<LoanDetailReport> iterator = searchResult.iterator();
        List<LoanDetailReport> list=new ArrayList<LoanDetailReport>();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        log.info("list的内容：{}", list);
        return list;
    }
}
