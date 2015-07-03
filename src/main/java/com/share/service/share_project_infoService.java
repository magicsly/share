package com.share.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.share.dao.share_project_infoMapper;
import com.share.model.share_project_info;

import javax.annotation.Resource;
import java.util.*;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

/**
 * Created by ElNino on 15/6/29.
 */
@Service
public class share_project_infoService {
    @Resource
    private ShardedJedisPool shardedJedisPool;

    @Autowired
    share_project_infoMapper share_project_infoMapper;


    public Integer addProjectInfo(share_project_info shareProjectInfo,float size){
        String sid = shareProjectInfo.getSid();
        Integer pid = shareProjectInfo.getPid();
        float liveprice = getOnePrice(sid);
        float staFlo = getProMoney(pid);
        float buymuch = (float) (staFlo*size/liveprice);
        shareProjectInfo.setSname("");
        shareProjectInfo.setNowmuch(buymuch);
        shareProjectInfo.setCreatetime(new Date());
        shareProjectInfo.setType((byte) 0);
        share_project_infoMapper.insertSelective(shareProjectInfo);
        moneyUpdate(pid,buymuch*liveprice);
        Integer code = 0;

        return code;
    }

    public float getOnePrice(String sid){
        ShardedJedis shardedJedis = shardedJedisPool.getResource();
        Map map =  shardedJedis.hgetAll(sid+":info");
        String sname = map.get("cname").toString();
        float liveprice = Float.parseFloat(map.get("liveprice").toString());
        float yesterdayprice = Float.parseFloat(map.get("yesterdayprice").toString());
        String state = map.get("state").toString();
        shardedJedisPool.returnResource(shardedJedis);
        return liveprice;
    }
    /**
     * 查看单个方案现有总净值
     * @param pid
     * @return
     */
    public float getProMoney(Integer pid){
        ShardedJedis shardedJedis = shardedJedisPool.getResource();
        List<share_project_info> proList = share_project_infoMapper.selectByPid(pid);
        float staFlo = 0;
        if(proList.size()!=0){
            for (share_project_info info : proList) {
                String infoSid = info.getSid();
                float infoNowmuch = info.getNowmuch();
                float infoLiveprice = 0;
                if(infoSid.equals("money")){
                    infoLiveprice = 1;
                }else{
                    infoLiveprice = Float.parseFloat(shardedJedis.hget(infoSid + ":info", "liveprice"));
                }
                staFlo = staFlo + (infoLiveprice*infoNowmuch);
            }
        }
        shardedJedisPool.returnResource(shardedJedis);
        return staFlo;
    }

    public float moneyUpdate(Integer pid, float money){
        float nowMoney = share_project_infoMapper.selectMoneyByPid(pid);
        nowMoney = nowMoney-money;
        if(nowMoney>0) {
            share_project_info shareProjectInfo = new share_project_info();
            shareProjectInfo.setPid(pid);
            shareProjectInfo.setNowmuch(nowMoney);
            shareProjectInfo.setUsemuch(nowMoney);
            share_project_infoMapper.updateMoney(shareProjectInfo);
        }
        return nowMoney;
    }

    public Integer test(){
        ShardedJedis shardedJedis = shardedJedisPool.getResource();
        String value = shardedJedis.get("foo");
        shardedJedis.set("user","liuling");

        Integer code = 0;
        return code;
    }
}
