package com.share.service;

import org.aspectj.apache.bcel.classfile.Code;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.share.dao.share_project_adjMapper;
import com.share.model.share_project_adj;

import javax.annotation.Resource;
import java.util.*;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
/**
 * Created by ElNino on 15/7/2.
 */
public class share_project_adjService {
    @Resource
    private ShardedJedisPool shardedJedisPool;

    @Autowired
    share_project_adjMapper share_project_adjMapper;

    public Integer addProjectAdj(share_project_adj share_project_adj){
        share_project_adjMapper.insertSelective(share_project_adj);
        Integer code = 0;
        return code;
    }
}
