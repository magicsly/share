package com.share.service;

import com.share.model.share_project;
import com.share.util.util;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.*;
import java.io.Serializable;


/**
 * Created by ElNino on 15/7/24.
 */
public class ThreadPoolTask implements Runnable, Serializable {

    private static final long serialVersionUID = 0;
    private static int consumeTaskSleepTime = 2000;

    share_projectService share_projectService;

    share_project_infoService share_project_infoService;

    share_project_adjService share_project_adjService;

    share_project_valueService share_project_valueService;

    private ShardedJedisPool shardedJedisPool;

    // 保存任务所需要的数据
    private List<share_project> threadPoolTaskData;

    ThreadPoolTask(List<share_project> tasks,ShardedJedisPool shardedJedisPool,share_project_infoService share_project_infoService,
                   share_projectService share_projectService,share_project_adjService share_project_adjService,
                   share_project_valueService share_project_valueService)
    {
        this.threadPoolTaskData = tasks;
        this.shardedJedisPool = shardedJedisPool;
        this.share_projectService = share_projectService;
        this.share_project_infoService = share_project_infoService;
        this.share_project_adjService = share_project_adjService;
        this.share_project_valueService =share_project_valueService;
    }


    public void run()
    {

//        处理一个任务，这里的处理方式太简单了，仅仅是一个打印语句
//        System.out.println(Thread.currentThread().getName());
//        System.out.println("start .." + threadPoolTaskData);
        ShardedJedis shardedJedis = shardedJedisPool.getResource();
        try
        {
            for(share_project sp : threadPoolTaskData){
                Integer pid = sp.getPid();
                try {
                    float val = share_project_infoService.getProVal(pid);
                    //净值
                    shardedJedis.zadd("valRank",val,sp.getPid().toString());
                    float index = shardedJedis.zrank("valRank", sp.getPid().toString());
                    float count = shardedJedis.zcard("valRank");
                    float per = (1-(index/count))*100;
                    shardedJedis.hset("project:"+pid,"allprofit",val+"");
                    shardedJedis.hset("project:"+pid,"allprofitPer",per+"");
                }catch (Exception e){
                    System.out.println("计算净值-------方案出现错误 id:"+pid+"---------时间"+ util.nowTime());
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }finally {

        threadPoolTaskData = null;
        shardedJedisPool.returnResource(shardedJedis);
        }
    }
}
