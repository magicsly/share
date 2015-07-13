package com.share.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.share.dao.share_project_valueMapper;
import com.share.model.share_project_value;

import javax.annotation.Resource;
import java.util.*;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

/**
 * Created by ElNino on 15/7/3.
 */
@Service
public class share_project_valueService {
    @Autowired
    share_project_valueMapper share_project_valueMapper;

    public List provalue_list(Integer pid , Integer type){
        List<share_project_value> share_project_values = share_project_valueMapper.selectByPid(pid);
        return share_project_values;
    }

}
