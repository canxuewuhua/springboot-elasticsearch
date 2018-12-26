package com.pancm.dao;

import com.pancm.pojo.School;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author yongqiang.zhu
 * @date 2018/12/26 15:33
 */
@Repository
public interface SchoolDao extends ElasticsearchRepository<School,String> {
}
