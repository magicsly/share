package com.share.cron;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import com.share.service.share_project_infoService;
import com.share.service.share_project_adjService;
import com.share.service.share_projectService;
import com.share.service.share_project_valueService;
import com.share.service.projectTask;

import com.share.model.share_project;
import com.share.model.share_project_info;
import com.share.model.share_project_adj;
import com.share.model.share_project_value;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.*;

import com.share.util.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by ElNino on 15/7/7.
 */
@Component
public class projectCron {

    @Autowired
    share_project_infoService share_project_infoService;

    @Autowired
    share_project_adjService share_project_adjService;

    @Autowired
    share_projectService share_projectService;

    @Autowired
    share_project_valueService share_project_valueService;

    @Autowired
    projectTask projectTask;

    @Resource
    private ShardedJedisPool shardedJedisPool;


    @Scheduled(fixedDelay = 1000*30)
    private void projcetUpdate(){
        ShardedJedis shardedJedis = shardedJedisPool.getResource();
        List<share_project_adj> shareProjectAdjList = share_project_adjService.projectAdj_list();
        for(share_project_adj shareProjectAdj : shareProjectAdjList){

            float buymuch = shareProjectAdj.getPercent();
            Integer piid = shareProjectAdj.getPiId();
            share_project_info shareProjectInfo = new share_project_info();
            shareProjectInfo = share_project_infoService.getOneInfo(piid);
            float infoBuyMuch = shareProjectInfo.getNowmuch();
            String sid = shareProjectInfo.getSid();
            float buymoney = share_project_infoService.getOnePrice(sid);
            Integer pid = shareProjectInfo.getPid();
            float infoCostprice = shareProjectInfo.getCostprice();

            try{
                //更新金额
                float much =  share_project_infoService.getBuyprice(buymoney,buymuch,pid);
                float money = (infoBuyMuch - much)*buymoney;
                float nowmoney = share_project_infoService.moneyUpdate(pid,money);

                //更新方案内容
                float newCostprice = ((infoBuyMuch*infoCostprice)+(buymoney*much))/(infoBuyMuch+much);
                shareProjectInfo.setNowmuch(much);
                shareProjectInfo.setUsemuch(infoBuyMuch-much);
                shareProjectInfo.setCostprice(newCostprice);
                share_project_infoService.updateOne(shareProjectInfo);

                //更新调仓状态
                shareProjectAdj.setBuymuch(much);
                shareProjectAdj.setBuymoney(buymoney);
                shareProjectAdj.setSuretime(new Date());
                shareProjectAdj.setType(9);
                share_project_adjService.updateadj(shareProjectAdj);

                List<share_project_info> shareProjectInfos = share_project_infoService.proInfolist(pid);
                String str = "";
                for(share_project_info spi : shareProjectInfos){
                    String jsid = spi.getSid();
                    DecimalFormat df = new DecimalFormat("0.0000");
                    double jmuch = spi.getNowmuch();
                    String db = df.format(jmuch);
                    str = str + jsid +","+db+"|";
                }
                str = str.substring(0,str.length()-1);
                shardedJedis.hset("projectinfo",pid.toString(),str);
                shardedJedis.hset("project:"+pid.toString(),"updatetime",com.share.util.util.nowTime());

            }catch (Exception e){
                //更新调仓状态
                shareProjectAdj.setSuretime(new Date());
                shareProjectAdj.setType(-1);
                share_project_adjService.updateadj(shareProjectAdj);
            }
        }
        shardedJedisPool.returnResource(shardedJedis);
    }

    @Scheduled(fixedDelay = 1000*60*60)
    private void projectVal() throws ParseException {
        long aa = System.currentTimeMillis();
        ShardedJedis shardedJedis = shardedJedisPool.getResource();
        List<share_project> share_projects = share_projectService.allProject_list();
        System.out.println("获取方案时间"+(System.currentTimeMillis()-aa));
        for(share_project sp : share_projects){
            Integer pid = sp.getPid();
            try {
                float val = share_project_infoService.getProVal(pid);
                //share_project_value shareProjectValue = new share_project_value();
                //shareProjectValue.setPid(pid);
                //shareProjectValue.setDayval(val);
                //share_projectService.updateProject(sp);
                //Integer code = share_project_valueService.addProValue(shareProjectValue);
                ////净值
                //bb = System.currentTimeMillis();
                //shardedJedis.zadd("valRank",val,sp.getPid().toString());
                //float index = shardedJedis.zrank("valRank", sp.getPid().toString());
                //float count = shardedJedis.zcard("valRank");
                //float per = (1-(index/count))*100;
                //shardedJedis.hset("project:"+pid,"allprofit",val+"");
                //shardedJedis.hset("project:"+pid,"allprofitPer",per+"");
                //System.out.println("存入净值需要时间:"+(System.currentTimeMillis()-bb));

                //交易天数
                Date creattime = sp.getCreatetime();
                Integer saleDays = share_projectService.getProDay(creattime);
                shardedJedis.zadd("useday",saleDays,sp.getPid().toString());
                float index = shardedJedis.zrank("useday", sp.getPid().toString());
                float count = shardedJedis.zcard("useday");
                float per = (1-(index/count))*100;
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

                //盈亏比
                float yearprofit =  (val-1)/(saleDays/365);
                float iswl = yearprofit/maxback;
                shardedJedis.zadd("iswl",iswl,sp.getPid().toString());
                index = shardedJedis.zrank("iswl", sp.getPid().toString());
                count = shardedJedis.zcard("iswl");
                per = (1-(index/count))*100;
                shardedJedis.hset("project:"+pid,"iswl",iswl+"");
                shardedJedis.hset("project:"+pid,"iswl",per+"");
            }catch (Exception e){
                System.out.println("每日计算-------方案出现错误 id:"+pid+"---------时间"+util.nowTime());
            }

        }
        System.out.println(System.currentTimeMillis()-aa);
        shardedJedisPool.returnResource(shardedJedis);
    }

    @Scheduled(fixedDelay = 5000)
    private void aaa() {
        projectTask.run();
    }
}
