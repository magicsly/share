package com.share.cron;
import com.share.controller.prjectController;
import com.sun.tools.internal.ws.processor.model.Response;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import com.share.service.share_project_infoService;
import com.share.service.share_project_adjService;
import com.share.service.share_projectService;
import com.share.service.share_project_valueService;

import com.share.model.share_project;
import com.share.model.share_project_info;
import com.share.model.share_project_adj;
import com.share.model.share_project_value;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.share.util.util;

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
            }catch (Exception e){
                //更新调仓状态
                shareProjectAdj.setSuretime(new Date());
                shareProjectAdj.setType(-1);
                share_project_adjService.updateadj(shareProjectAdj);
            }
        }
        shardedJedisPool.returnResource(shardedJedis);
    }

    @Scheduled(fixedDelay = 1000*30)
    private void projectVal() throws ParseException {
        ShardedJedis shardedJedis = shardedJedisPool.getResource();
        List<share_project> share_projects = share_projectService.allProject_list();
        for(share_project sp : share_projects){
            Integer pid = sp.getPid();
            float val = share_project_infoService.getProMoney(pid);
            share_project_value shareProjectValue = new share_project_value();
            shareProjectValue.setPid(pid);
            shareProjectValue.setDayval(val);
            share_projectService.updateProject(sp);
            Integer code = share_project_valueService.addProValue(shareProjectValue);
            shardedJedis.zadd("valRank",val,sp.getPid().toString());

            float index = shardedJedis.zrank("valRank", sp.getPid().toString());
            float count = shardedJedis.zcard("valRank");
            float per = (1-(index/count))*100;
            shardedJedis.hset("project:"+sp.getPid(),"allprofit",val+"");
            shardedJedis.hset("project:"+sp.getPid(),"allprofitPer",per+"");

        }
        shardedJedisPool.returnResource(shardedJedis);
    }

    //@Scheduled(fixedDelay = 1000)
    private void dddd() {
        util util = new util();
        String aa = util.sendGet("http://localhost:8080/addproject?name=dddd&info=rrrrr&type=1&uid=4", "gbk");
    }
}
