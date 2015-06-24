package com.share.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.share.dao.share_flowMapper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ElNino on 15/6/17.
 */
@Service
public class share_flowService {
    @Autowired
   share_flowMapper share_flowMapper;

    public Integer userFlow(Integer uid , Integer fid){
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("uid",uid);
        map.put("fid",fid);
        Integer code = share_flowMapper.userflow(map);
        return code;
    }
}
