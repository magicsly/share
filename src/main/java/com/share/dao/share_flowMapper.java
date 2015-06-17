package com.share.dao;

import com.share.model.share_flow;

public interface share_flowMapper {
    int insert(share_flow record);

    int insertSelective(share_flow record);
}