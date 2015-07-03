package com.share.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.share.dao.share_flowMapper;
import com.share.model.share_flow;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Calendar;
/**
 * Created by ElNino on 15/6/17.
 */
@Service
public class share_flowService {
    @Autowired
   share_flowMapper share_flowMapper;

    public Integer userFlow(Integer uid , Integer fid){
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("uid",uid);
        map.put("fid",fid);
        Integer code = share_flowMapper.userflow(map);
        return code;
    }

    public List userFlow_list(){
        Integer uid = (Integer)((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession().getAttribute("uid");
        List<share_flow> share_flows = share_flowMapper.userflowList(uid);
        return share_flows;
    }

    public List userFans_list(){
        Integer uid = (Integer)((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession().getAttribute("uid");
        List<share_flow> share_flows = share_flowMapper.userfansList(uid);
        return share_flows;
    }

    public Map userFlowInfo(){
        Integer uid = (Integer)((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession().getAttribute("uid");
        Map<String,Object> map = new HashMap<String, Object>();
        map = share_flowMapper.flowinfo(uid);
        return map;
    }
}
