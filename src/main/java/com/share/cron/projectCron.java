package com.share.cron;
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

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


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

    //@Scheduled(fixedDelay = 1000*20)
    private void projcetddd(){

        ShardedJedis shardedJedis = shardedJedisPool.getResource();
        List<share_project_adj> shareProjectAdjList = share_project_adjService.projectAdj_list();
        for(share_project_adj shareProjectAdj : shareProjectAdjList){
            float buymoney = shareProjectAdj.getBuymoney();
            float buymuch = shareProjectAdj.getBuymuch();
            Integer piid = shareProjectAdj.getPiId();
            share_project_info shareProjectInfo = new share_project_info();
            shareProjectInfo = share_project_infoService.getOneInfo(piid);
            float infoBuyMuch = shareProjectInfo.getNowmuch();
            Integer pid = shareProjectInfo.getPid();
            float infoCostprice = shareProjectInfo.getCostprice();

            //更新金额
            float much =  share_project_infoService.getBuyprice(buymoney,buymuch,pid);
            float money = (much - infoBuyMuch)*buymoney;
            float nowmoney = share_project_infoService.moneyUpdate(pid,money);

            //更新方案内容
            float newCostprice = ((infoBuyMuch*infoCostprice)+(buymoney*much))/(infoBuyMuch+much);
            shareProjectInfo.setNowmuch(much);
            shareProjectInfo.setUsemuch(infoBuyMuch-much);
            shareProjectInfo.setCostprice(newCostprice);
            share_project_infoService.updateOne(shareProjectInfo);

            //更新调仓状态
            shareProjectAdj.setSuretime(new Date());
            shareProjectAdj.setType(9);
            share_project_adjService.updateadj(shareProjectAdj);



        }
        shardedJedisPool.returnResource(shardedJedis);
    }
}
