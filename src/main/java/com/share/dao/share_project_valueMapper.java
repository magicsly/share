package com.share.dao;

import com.share.model.share_project_value;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface share_project_valueMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(share_project_value record);

    int insertSelective(share_project_value record);

    share_project_value selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(share_project_value record);

    int updateByPrimaryKey(share_project_value record);

    public List<share_project_value> selectByPid(Integer pid);
}