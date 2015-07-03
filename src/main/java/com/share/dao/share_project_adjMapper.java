package com.share.dao;

import com.share.model.share_project_adj;
import org.springframework.stereotype.Repository;

@Repository
public interface share_project_adjMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(share_project_adj record);

    int insertSelective(share_project_adj record);

    share_project_adj selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(share_project_adj record);

    int updateByPrimaryKey(share_project_adj record);
}