package com.share.dao;

import com.share.model.share_project;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

@Repository
public interface share_projectMapper {
    int deleteByPrimaryKey(Integer pid);

    int insert(share_project record);

    int insertSelective(share_project record);

    share_project selectByPrimaryKey(Integer pid);

    int updateByPrimaryKeySelective(share_project record);

    int updateByPrimaryKey(share_project record);

    public List<share_project> userPorject_list(Integer uid);

    public List<share_project> selectAllproject();
}