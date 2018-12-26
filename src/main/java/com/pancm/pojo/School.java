package com.pancm.pojo;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;

/**
 * @author yongqiang.zhu
 * @date 2018/12/26 15:31
 */
@Data
@Document(indexName = "university",type = "school", shards = 1,replicas = 0, refreshInterval = "-1")
public class School implements Serializable {
	private String name;
	private String birth;
	private String email;
}
