package com.pancm.pojo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Document(indexName = "report", type = "loandetailreport", shards = 1,replicas = 0, refreshInterval = "-1")
public class LoanDetailReport implements Serializable{
    @Id
    private String id;
    @Field
    private String contract_ledger_id;

    private Date generate_date;

    private String contract_code;

    private String contract_name;

    private String customer_name;

    private String customer_code;

    private String product_name;

    private BigDecimal receivable_interest;

    private BigDecimal loan_amount;

    private Date loan_date;

    private String monthly_number;

    private String repayment_type;

    private BigDecimal strike_rate;

    private BigDecimal fine_rate;

    private BigDecimal interest_rate_rate;

    private BigDecimal receivable_interest_total;

    private Date end_date;

    private String investor_name;

    private String customer_name_md5;

    private String channel;

    private String creator_id;

    private Date creation_ts;

    private String updator_id;

    private Date updation_ts;
}
