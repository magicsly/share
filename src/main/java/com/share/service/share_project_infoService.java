package com.share.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.share.dao.share_project_infoMapper;
import com.share.model.share_project_info;
import com.share.model.redis_shareModel;

import javax.annotation.Resource;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        shareProjectInfo.setNowmuch((float) 0);
        shareProjectInfo.setCreatetime(new Date());
        shareProjectInfo.setType((byte) 0);
        shareProjectInfo.setIsok((byte) 0);
        share_project_infoMapper.insertSelective(shareProjectInfo);
        Integer code = shareProjectInfo.getPiId();
        return code;
    }

    public boolean isPorInfo(Integer pid, String sid){
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("pid",pid);
        map.put("sid",sid);
        Integer count = share_project_infoMapper.countByPid(map);
        if(count>0){
            return true;
        }else {
            return false;
        }
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
        redisShareModel.setShortchar(map.get("shotchar").toString());
        redisShareModel.setState(Integer.parseInt(map.get("state").toString()));
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

    public Map getProValList(Integer pid){
        ShardedJedis shardedJedis = shardedJedisPool.getResource();
        Map map = shardedJedis.hgetAll("projectinfo");
        shardedJedisPool.returnResource(shardedJedis);
        return map;
    }

    public float getProVal(Integer pid){
        ShardedJedis shardedJedis = shardedJedisPool.getResource();
        String str = null;
        try {
            str = shardedJedis.hget("projectinfo", pid.toString());

        }catch (Exception e){
            System.out.println("redis error");
        }

        String[] strArr = str.split("\\|");
        float staFlo = 0;
        if(strArr.length!=0){
            for (String info : strArr) {
                String infoSid = info.split(",")[0];
                float infoNowmuch = Float.parseFloat(info.split(",")[1]);
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

    public List proInfolist(Integer pid){
        List<share_project_info> proList = share_project_infoMapper.selectByPid(pid);
        return  proList;
    }

    public List getRedisInfoList(Integer pid){
        ShardedJedis shardedJedis = shardedJedisPool.getResource();
        List list=new ArrayList();
        List<share_project_info> proList = share_project_infoMapper.selectByPid(pid);
        float staFlo = 0;
        if(proList.size()!=0) {
            for (share_project_info info : proList) {
                Map<String,Object> obj = new HashMap<String, Object>();
                String sid = info.getSid();
                String sname= info.getSname();
                float costprice = 0;
                float liveprice = 0;
                float precent = info.getNowmuch();
                if(sid.equals("money")){
                    costprice = 1;
                    liveprice = 1;
                }else{
                    costprice = info.getCostprice();
                    liveprice = this.getOnePrice(sid);
                }
                obj.put("symbol",sid);
                obj.put("name",sname);
                obj.put("costprice",costprice);
                obj.put("nowprice",liveprice);
                obj.put("targetprice",88.88);
                obj.put("precent",precent);
                //float
                list.add(obj);
            }
        }

        shardedJedisPool.returnResource(shardedJedis);
        return list;
    }

    public float moneyUpdate(Integer pid, float money){
        try {
            float nowMoney = share_project_infoMapper.selectMoneyByPid(pid);
            nowMoney = nowMoney+money;
            if(nowMoney>0) {
                share_project_info shareProjectInfo = new share_project_info();
                shareProjectInfo.setPid(pid);
                shareProjectInfo.setNowmuch(nowMoney);
                shareProjectInfo.setUsemuch(nowMoney);
                share_project_infoMapper.updateMoney(shareProjectInfo);
            }
            return nowMoney;
        }catch (Exception e){
            share_project_info shareProjectInfo = new share_project_info();
            shareProjectInfo.setPid(pid);
            shareProjectInfo.setSid("money");
            shareProjectInfo.setSname("money");
            shareProjectInfo.setCreatetime(new Date());
            shareProjectInfo.setNowmuch((float)1);
            shareProjectInfo.setUsemuch((float)1);
            share_project_infoMapper.insertSelective(shareProjectInfo);
            return 1;
        }
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

    public String getRound(){
        ShardedJedis shardedJedis = shardedJedisPool.getResource();
        String str = "";
        str =shardedJedis.srandmember("stocknamelist");
        float price = getOnePrice(str);
        shardedJedisPool.returnResource(shardedJedis);
        return str;
    }
    public Integer test(){
        ShardedJedis shardedJedis = shardedJedisPool.getResource();
        String value = shardedJedis.get("foo");
        shardedJedis.set("user","liuling");

        Integer code = 0;
        shardedJedisPool.returnResource(shardedJedis);
        return code;
    }

    public ArrayList searchstock(String str) {
        System.out.println("serach.....");
        ShardedJedis j = shardedJedisPool.getResource();
        Set<String> list = j.smembers("stocknamelist");
        int num = 0;
        ArrayList retlist = new ArrayList();
        for (String s : list) {
            Map<String, String> x = j.hgetAll(s + ":info");
            Pattern pattern = Pattern.compile("^" + str + ".*");
            Matcher matcher = pattern.matcher(x.get("code"));
            Matcher matcher2 = pattern.matcher(x.get("shotchar"));
            if ((matcher.matches() || matcher2.matches())) {
                if(x.get("liveprice") != null){
                    retlist.add(x);
                    num++;
                }
            }
            if (num >= 5) {
                break;
            }
        }

        shardedJedisPool.returnResource(j);
        return retlist;
    }
}
