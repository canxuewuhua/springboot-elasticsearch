package com.pancm.dao;

import com.pancm.pojo.LoanDetailReport;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends ElasticsearchRepository<LoanDetailReport,String> {

    /**
     * 查询报表信息
     * @param id
     * @return
     */
    LoanDetailReport queryLoanDetailReportById(String id);
}
