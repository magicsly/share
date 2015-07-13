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
import java.util.*;

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

    public Integer addProject(share_project shareProject){
        Integer pid = 0;
        shareProject.setCreatetime(new Date());
        share_projectMapper.insertSelective(shareProject);
        pid=shareProject.getPid();
        share_project_info shareProjectInfo = new share_project_info();
        shareProjectInfo.setPid(pid);
        shareProjectInfo.setSid("money");
        shareProjectInfo.setNowmuch((float)1);
        shareProjectInfo.setUsemuch((float)1);
        shareProjectInfo.setType((byte)0);
        shareProjectInfo.setCreatetime(new Date());
        share_project_infoMapper.insertSelective(shareProjectInfo);

        ShardedJedis shardedJedis = shardedJedisPool.getResource();
        Map<String,String> map = new HashMap<String, String>();

        shardedJedis.hmset("project:"+pid.toString(),map);
        shardedJedisPool.returnResource(shardedJedis);
        return pid;
    }

    public List userProject_list(Integer uid){
        List<share_project> share_project = share_projectMapper.userPorject_list(uid);
        return  share_project;
    }



}
