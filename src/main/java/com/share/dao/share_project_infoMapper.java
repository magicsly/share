package com.share.dao;

import com.share.model.share_project_info;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

@Repository
public interface share_project_infoMapper {
    int deleteByPrimaryKey(Integer piId);

    int insert(share_project_info record);

    int insertSelective(share_project_info record);

    share_project_info selectByPrimaryKey(Integer piId);

    int updateByPrimaryKeySelective(share_project_info record);

    int updateByPrimaryKey(share_project_info record);

    public List<share_project_info> selectByPid(Integer pid);

    float selectMoneyByPid(Integer pid);

    int updateMoney(share_project_info record);

    int countByPid(Map map);
}