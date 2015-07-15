package com.share.dao;

import com.share.model.share_project_adj;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface share_project_adjMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(share_project_adj record);

    int insertSelective(share_project_adj record);

    share_project_adj selectByPrimaryKey(Integer id);

    share_project_adj selectNewAdj();

    int updateByPrimaryKeySelective(share_project_adj record);

    int updateByPrimaryKey(share_project_adj record);

    int maxTimes(Integer pid);

    public List<share_project_adj> select_adj_list();

    public List<share_project_adj> selectAdjbuyPid(Integer pid);

}