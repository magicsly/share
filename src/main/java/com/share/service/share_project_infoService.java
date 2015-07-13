package com.share.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.share.dao.share_project_infoMapper;
import com.share.model.share_project_info;
import com.share.model.redis_shareModel;

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


    public Integer addProjectInfo(share_project_info shareProjectInfo){
        String sid = shareProjectInfo.getSid();
        Integer pid = shareProjectInfo.getPid();
        redis_shareModel redisShareModel = new redis_shareModel();
        redisShareModel = getShare(sid);
        String sname = redisShareModel.getCname();
        shareProjectInfo.setSid(sid);
        shareProjectInfo.setPid(pid);
        shareProjectInfo.setSname(sname);
        shareProjectInfo.setNowmuch((float)0);
        shareProjectInfo.setCreatetime(new Date());
        shareProjectInfo.setType((byte) 0);
        share_project_infoMapper.insertSelective(shareProjectInfo);
        Integer code = 0;
        return code;
    }

    public Integer updateProjectInfo(share_project_info shareProjectInfo){

        Integer code = 0;
        return code;
    }

    public float getbuymuch(String sid,float size,Integer pid){
        float liveprice = getOnePrice(sid);
        float staFlo = getProMoney(pid);
        float buymuch = (float) (staFlo*size/liveprice);
        return buymuch;
    }

    public float getBuyprice(float liveprice,float size,Integer pid){
        float staFlo = getProMoney(pid);
        float buymuch = (float) (staFlo*size/liveprice);
        return buymuch;
    }
    /**
     * 查找单个股票现在信息
     * @param sid
     * @return
     */
    public redis_shareModel getShare(String sid){
        redis_shareModel redisShareModel = new redis_shareModel();
        ShardedJedis shardedJedis = shardedJedisPool.getResource();
        Map map =  shardedJedis.hgetAll(sid+":info");
        redisShareModel.setCname(map.get("cname").toString());
        redisShareModel.setCode(map.get("code").toString());
        redisShareModel.setLiveprice(Float.parseFloat(map.get("liveprice").toString()));
        redisShareModel.setYesterdayprice(Float.parseFloat(map.get("yesterdayprice").toString()));
        redisShareModel.setShortchar(map.get("shortchar").toString());
        redisShareModel.setState(Integer.parseInt(map.get("yesterdayprice").toString()));
        redisShareModel.setUpdatetime(map.get("updatetime").toString());
        shardedJedisPool.returnResource(shardedJedis);
        return redisShareModel;
    }

    /**
     * 查找单个股票现价
     * @param sid
     * @return
     */
    public float getOnePrice(String sid){
        ShardedJedis shardedJedis = shardedJedisPool.getResource();
        Map map =  shardedJedis.hgetAll(sid+":info");
        float liveprice = Float.parseFloat(map.get("liveprice").toString());
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

    public boolean isMoney(Integer pid, float money){
        float nowMoney = share_project_infoMapper.selectMoneyByPid(pid);
        nowMoney = nowMoney-money;
        if(nowMoney>0) {
           return true;
        }else{
            return false;
        }
    }

    public share_project_info getOneInfo(Integer piid){

        share_project_info shareProjectInfo = new share_project_info();
        shareProjectInfo= share_project_infoMapper.selectByPrimaryKey(piid);
        return  shareProjectInfo;
    }

    public Integer updateOne(share_project_info share_project_info){
        Integer code = share_project_infoMapper.updateByPrimaryKey(share_project_info);
        return  code;
    }

    public Integer test(){
        ShardedJedis shardedJedis = shardedJedisPool.getResource();
        String value = shardedJedis.get("foo");
        shardedJedis.set("user","liuling");

        Integer code = 0;
        return code;
    }
}
