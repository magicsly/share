package com.share.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.share.dao.share_projectMapper;
import com.share.model.share_project;
import com.share.dao.share_project_infoMapper;
import com.share.model.share_project_info;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.*;
import com.share.util.util;

/**
 * Created by ElNino on 15/6/29.
 */
@Service
public class share_projectService {

    @Autowired
    share_projectMapper share_projectMapper;

    @Resource
    private ShardedJedisPool shardedJedisPool;

    @Autowired
    share_project_infoMapper share_project_infoMapper;

    util util = new util();
    public Integer addProject(share_project shareProject){
        Integer pid = 0;
        shareProject.setCreatetime(new Date());
        shareProject.setUpdatetime(new Date());
        share_projectMapper.insertSelective(shareProject);
        //生成redis缓存内容
        pid=shareProject.getPid();
        ShardedJedis shardedJedis = shardedJedisPool.getResource();
        Map<String,String> map = new HashMap<String, String>();
        map = com.share.util.util.ConvertObjToMap(shareProject);
        map.put("yearprofit","0");
        map.put("yearprofitPer","0");

        map.put("iswl","0");
        map.put("iswlPer","0");

        map.put("useday","0");
        map.put("usedayPer","0");

        map.put("allprofit","0");
        map.put("allprofitPer","0");

        map.put("dayprofit","0");
        map.put("dayprofitPer","0");

        map.put("maxDrawDown","0");
        map.put("maxDrawDownPer","0");

        map.put("move","0");
        map.put("movePer","0");

        map.put("success","0");
        map.put("successPer","0");
        shardedJedis.hmset("project:"+pid.toString(),map);
        shardedJedisPool.returnResource(shardedJedis);

        return pid;
    }

    public Integer updateProject(share_project shareProject){
        shareProject.setUpdatetime(new Date());
        Integer code =  share_projectMapper.updateByPrimaryKeySelective(shareProject);
        return code;
    }

    public List userProject_list(Integer uid){
        List<share_project> share_project = share_projectMapper.userPorject_list(uid);
        return  share_project;
    }

    public List allProject_list(){
        List<share_project> share_project = share_projectMapper.selectAllproject();
        return  share_project;
    }

    public Map allprojectValRank(){
        ShardedJedis shardedJedis = shardedJedisPool.getResource();
        Map map = new HashMap<String, Object>();
        Set<String> list = shardedJedis.zrange("valRank",0,-1);
        map.put("list",list);
        shardedJedisPool.returnResource(shardedJedis);
        return map;
    }

    public List porjectOrderby_list(){
        ShardedJedis shardedJedis = shardedJedisPool.getResource();
        List pushlist = new ArrayList();
        Set<String> list = shardedJedis.zrange("valRank",50,59);
        for (String str : list) {
            String pid = str;
            Map map =  shardedJedis.hgetAll("project:"+pid);
            pushlist.add(map);
        }
        shardedJedisPool.returnResource(shardedJedis);
        return pushlist;
    }

    public Integer getProDay(Date date){
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        double d =Float.parseFloat(format.format(date));
        double n =Float.parseFloat(format.format(new Date()));
        ShardedJedis shardedJedis = shardedJedisPool.getResource();
        float day = shardedJedis.zcount("stockisopen", d, n);
        shardedJedisPool.returnResource(shardedJedis);
        return (int) day;
    }

}
