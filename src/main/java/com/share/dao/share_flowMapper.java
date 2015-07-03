package com.share.dao;

import com.share.model.share_flow;
import org.springframework.stereotype.Repository;
import java.util.List;

import java.awt.*;
import java.util.Map;

@Repository
public interface share_flowMapper {
    int insert(share_flow record);

    int insertSelective(share_flow record);

    int userflow (Map map);

    public List<share_flow> userflowList(Integer uid);

    public List<share_flow> userfansList(Integer fid);

    Map flowinfo(Integer uid);
}