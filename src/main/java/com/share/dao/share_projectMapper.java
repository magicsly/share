package com.share.dao;

import com.share.model.share_project;

public interface share_projectMapper {
    int deleteByPrimaryKey(Integer pid);

    int insert(share_project record);

    int insertSelective(share_project record);

    share_project selectByPrimaryKey(Integer pid);

    int updateByPrimaryKeySelective(share_project record);

    int updateByPrimaryKey(share_project record);
}