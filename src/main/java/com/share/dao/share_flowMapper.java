package com.share.dao;

import com.share.model.share_flow;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public interface share_flowMapper {
    int insert(share_flow record);

    int insertSelective(share_flow record);

    int userflow (Map map);
}