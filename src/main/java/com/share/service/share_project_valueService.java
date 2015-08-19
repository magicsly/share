package com.share.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.share.dao.share_project_valueMapper;
import com.share.model.share_project_value;
import com.share.util.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import javax.annotation.Resource;

/**
 * Created by ElNino on 15/7/3.
 */
@Service
public class share_project_valueService {
    @Autowired
    share_project_valueMapper share_project_valueMapper;

    @Resource
    private ShardedJedisPool shardedJedisPool;

    public List provalue_list(Integer pid , Integer type){
        ShardedJedis shardedJedis = shardedJedisPool.getResource();
        List<share_project_value> share_project_values = share_project_valueMapper.selectByPid(pid);
        List list = new ArrayList();
        Integer pagesize = 0;
        Integer i = 0;
        if (type == 1){
            pagesize = 5;
        }else if (type == 2){
            pagesize = 22;
        }
        for (share_project_value sinfo : share_project_values){
            Map<String,String> map = new HashMap<String, String>();
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            String s = "0000300day:"+format.format(sinfo.getCreatime()).toString();
            Map<String,String> rdis = new HashMap<String, String>();
            String hs300 = shardedJedis.hget(s,"shou");
            map.put("pid",sinfo.getPid().toString());
            map.put("dayval",sinfo.getDayval().toString());
            map.put("id",sinfo.getId().toString());
            map.put("hs300val",hs300);
            map.put("creatime",util.toTimeString(sinfo.getCreatime()));
            list.add(map);
            i++;
            if(i==type){
                break;
            }
        }
        shardedJedisPool.returnResource(shardedJedis);
        return list;
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

    public float yestodayVal(Integer pid){
        try {
            float val = share_project_valueMapper.selectTodaybyPid(pid).getDayval();
            return val;
        }catch (Exception e){
            return 1;
        }
    }

    public float maxBack (Integer pid){
        Map map = share_project_valueMapper.maxmin(pid);
        Iterator iterator = map.keySet().iterator();
        String min = map.get(iterator.next()).toString();
        String max = map.get(iterator.next()).toString();
        float maxback = (Float.parseFloat(max)-Float.parseFloat(min))/Float.parseFloat(max);
        return maxback;
    }

}
