package com.share.dao;

import com.share.model.share_project_info;

public interface share_project_infoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(share_project_info record);

    int insertSelective(share_project_info record);

    share_project_info selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(share_project_info record);

    int updateByPrimaryKey(share_project_info record);
}