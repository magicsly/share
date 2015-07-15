package com.share.service;

import org.aspectj.apache.bcel.classfile.Code;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.share.dao.share_project_adjMapper;
import com.share.model.share_project_adj;

import javax.annotation.Resource;
import java.util.*;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
/**
 * Created by ElNino on 15/7/2.
 */
@Service
public class share_project_adjService {
    @Resource
    private ShardedJedisPool shardedJedisPool;

    @Autowired
    share_project_adjMapper share_project_adjMapper;

    /**
     * 添加方案调整
     * @param share_project_adj
     * @return
     */
    public Integer addProjectAdj(share_project_adj share_project_adj){
        share_project_adj.setBuytime(new Date());
        share_project_adj.setUpdatetime(new Date());
        share_project_adj.setType(0);
        Integer code = share_project_adjMapper.insertSelective(share_project_adj);
        return code;
    }

    /**
     * 更新方案调整
     * @param share_project_adj
     * @return
     */
    public Integer updateadj(share_project_adj share_project_adj){
        share_project_adj.setUpdatetime(new Date());
        Integer code =  share_project_adjMapper.updateByPrimaryKey(share_project_adj);
        return code;
    }

    /**
     * 最新方案调整列表
     * @return
     */
    public List projectAdj_list(){
        List<share_project_adj> shareProjectAdjList = share_project_adjMapper.select_adj_list();
        return shareProjectAdjList;
    }

    /**
     * 获取最新一条方案调整内容
     * @return
     */
    public  share_project_adj getNewAdj(){
        share_project_adj shareProjectAdj = new share_project_adj();
        shareProjectAdj = share_project_adjMapper.selectNewAdj();
        return  shareProjectAdj;
    }

    /**
     * 获取该方案调整次数
     * @param pid
     * @return
     */
    public Integer getMaxTimes(Integer pid){
        Integer times = share_project_adjMapper.maxTimes(pid);
        return  times;
    }

    public List getProAdj_list(Integer pid){
        List proAdj_list = share_project_adjMapper.selectAdjbuyPid(pid);
        return  proAdj_list;
    }



}
