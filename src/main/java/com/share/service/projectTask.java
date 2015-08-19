package com.share.service;

import com.share.model.share_project;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.*;

import com.share.model.share_project_value;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


/**
 * Created by ElNino on 15/7/23.
 */
@Service
public class projectTask {

    @Resource
    private ThreadPoolTaskExecutor threadpool;

    @Autowired
    share_projectService share_projectService;

    @Autowired
    share_project_infoService share_project_infoService;

    @Autowired
    share_project_adjService share_project_adjService;

    @Autowired
    share_project_valueService share_project_valueService;

    @Resource
    private ShardedJedisPool shardedJedisPool;


    private void projectVal(List<share_project> share_projects) throws ParseException {
        long aa = System.currentTimeMillis();
        ShardedJedis shardedJedis = shardedJedisPool.getResource();

        for(share_project sp : share_projects){
            Integer pid = sp.getPid();
            try {

                long bb = System.currentTimeMillis();
                float val = share_project_infoService.getProVal(pid);
                System.out.println("获取净值需要时间:"+(System.currentTimeMillis()-bb));
                share_project_value shareProjectValue = new share_project_value();
                shareProjectValue.setPid(pid);
                shareProjectValue.setDayval(val);
                share_projectService.updateProject(sp);
                Integer code = share_project_valueService.addProValue(shareProjectValue);
                //净值
                bb = System.currentTimeMillis();
                shardedJedis.zadd("valRank",val,sp.getPid().toString());
                float index = shardedJedis.zrank("valRank", sp.getPid().toString());
                float count = shardedJedis.zcard("valRank");
                float per = (1-(index/count))*100;
                shardedJedis.hset("project:"+pid,"allprofit",val+"");
                shardedJedis.hset("project:"+pid,"allprofitPer",per+"");
                System.out.println("存入净值需要时间:"+(System.currentTimeMillis()-bb));
                System.out.println("========================================================");
                System.out.println(System.currentTimeMillis()-aa);

                //交易天数
                Date creattime = sp.getCreatetime();
                Integer saleDays = share_projectService.getProDay(creattime);
                shardedJedis.zadd("useday",saleDays,sp.getPid().toString());
                index = shardedJedis.zrank("useday", sp.getPid().toString());
                count = shardedJedis.zcard("useday");
                per = (1-(index/count))*100;
                shardedJedis.hset("project:"+pid,"useday",saleDays+"");
                shardedJedis.hset("project:"+pid,"usedayPer",per+"");

                //日收益
                float dayval = share_project_infoService.getProMoney(pid);
                float todayval =share_project_valueService.yestodayVal(pid);
                float dayprofit = dayval-todayval;
                shardedJedis.zadd("dayprofit",dayprofit,sp.getPid().toString());
                index = shardedJedis.zrank("dayprofit", sp.getPid().toString());
                count = shardedJedis.zcard("dayprofit");
                per = (1-(index/count))*100;
                shardedJedis.hset("project:"+pid,"dayprofit",saleDays+"");
                shardedJedis.hset("project:"+pid,"dayprofitPer",per+"");

                //调仓次数
                Integer times = share_project_adjService.getTimesByPid(pid);
                shardedJedis.zadd("move",times,sp.getPid().toString());
                index = shardedJedis.zrank("move", sp.getPid().toString());
                count = shardedJedis.zcard("move");
                per = (1-(index/count))*100;
                shardedJedis.hset("project:"+pid,"move",times+"");
                shardedJedis.hset("project:"+pid,"movePer",per+"");

                //最大回撤
                float maxback = share_project_valueService.maxBack(pid);
                shardedJedis.zadd("maxDrawDown",maxback,sp.getPid().toString());
                index = shardedJedis.zrank("maxDrawDown", sp.getPid().toString());
                count = shardedJedis.zcard("maxDrawDown");
                per = (1-(index/count))*100;
                shardedJedis.hset("project:"+pid,"maxDrawDown",maxback+"");
                shardedJedis.hset("project:"+pid,"maxDrawDownPer",per+"");

               // 盈亏比


            }catch (Exception e){
                System.out.println(pid);
            }

        }
        shardedJedisPool.returnResource(shardedJedis);
    }

    public void  run(){
        long bb = System.currentTimeMillis();
        List<share_project> share_projects = share_projectService.allProject_list();
        System.out.println("现有方案"+share_projects.size());
        int page = share_projects.size()/50;
        int yu = share_projects.size() % 50;
        for(int i=0;i<page; i++){
            List<share_project> newProjeceList =new ArrayList<share_project>();
            if(i == page-1) {
               newProjeceList = share_projects.subList(i * 50, 50 * (i + 1) + yu);
            }else{
               newProjeceList = share_projects.subList(i * 50, 50 * (i + 1) );
            }
            threadpool.execute(new ThreadPoolTask(newProjeceList,shardedJedisPool,share_project_infoService,share_projectService,share_project_adjService,share_project_valueService));
        }



        while (true) {
            if (threadpool.getActiveCount() == 0) {
                break;
            }
        }
        System.out.println("需要时间:"+(System.currentTimeMillis()-bb));
    }

}
