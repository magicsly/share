package com.share.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.share.dao.share_project_valueMapper;
import com.share.model.share_project_value;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

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

    public Integer addProValue (share_project_value shareProjectValue) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date d =new Date();
        String dd =format.format(d);
        Date ddd =format.parse(dd);
        shareProjectValue.setCreatime(ddd);
        if(isproValue(shareProjectValue)){
            share_project_valueMapper.updateByPrimaryKeySelective(shareProjectValue);
            return 1;
        }else{
            share_project_valueMapper.insertSelective(shareProjectValue);
            return 2;
        }
    }

    public boolean isproValue (share_project_value shareProjectValue){
        Integer count = share_project_valueMapper.selectCount(shareProjectValue);
        if(count>0){
            return true;
        }else {
            return false;
        }
    }

}
