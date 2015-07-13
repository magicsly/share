package com.share.controller;

import org.aspectj.apache.bcel.classfile.Code;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

import com.share.model.share_project;
import com.share.model.share_project_info;
import com.share.model.share_project_adj;
import com.share.model.share_project_value;


/**
 * Created by ElNino on 15/6/29.
 */
@Controller
@RequestMapping("/")
public class prjectController {

    @Autowired
    com.share.service.share_projectService share_projectService;

    @Autowired
    com.share.service.share_project_infoService share_project_infoService;

    @Autowired
    com.share.service.share_project_adjService share_project_adjService;

    @Autowired
    com.share.service.share_project_valueService share_project_valueService;

    @RequestMapping(value = "/addproject")
    @ResponseBody
    public Map addProject(@RequestParam(value="name",defaultValue = "",required=false) String name,
                     @RequestParam(value="info",defaultValue = "",required=false) String info,
                     @RequestParam(value="image",defaultValue = "",required=false) String image,
                     @RequestParam(value="type",defaultValue = "",required=false) Integer type,
                     @RequestParam(value="uid",defaultValue = "",required=false) Integer uid
                     ){
        share_project shareProject=new share_project();
        shareProject.setName(name);
        shareProject.setInfo(info);
        shareProject.setImage(image);
        shareProject.setType(type);
        shareProject.setUid(uid);
        shareProject.setIsok((byte)0);
        Integer pid = share_projectService.addProject(shareProject);
        Map<String,Object> map = new HashMap<String, Object>();
        if(pid>0){
            map.put("code",0);
            map.put("pid",pid);
        }else{
            map.put("code",-1);
        }
        return map;
    }

    @RequestMapping(value = "/userproject_list")
    @ResponseBody
    public Map userProject_list(@RequestParam(value="uid",defaultValue = "",required=false) Integer uid){
        List userProject_list = share_projectService.userProject_list(uid);
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("code", 0);
        map.put("list", userProject_list);
        return map;
    }

    @RequestMapping(value = "/addproinfo")
    @ResponseBody
    public Map addProject_info(@RequestParam(value="pid",defaultValue = "",required=false) Integer pid,
                                @RequestParam(value="sid",defaultValue = "",required=false) String sid,
                                @RequestParam(value="size",defaultValue = "",required=false) float perecent
    ){
        Map<String,Object> map = new HashMap<String, Object>();
        float nowprice = share_project_infoService.getOnePrice(sid);
        float buymuch = share_project_infoService.getbuymuch(sid,perecent,pid);
        Integer code = 0;
        if(!share_project_infoService.isMoney(pid,buymuch*nowprice)){
            map.put("code",1);
        }else{
            share_project_info shareProjectInfo = new share_project_info();
            shareProjectInfo.setPid(pid);
            shareProjectInfo.setSid(sid);
            Integer piid =  share_project_infoService.addProjectInfo(shareProjectInfo);

            share_project_adj shareProjectAdj = new share_project_adj();
            shareProjectAdj.setPiId(piid);
            shareProjectAdj.setBuymuch(perecent);
            shareProjectAdj.setBuymoney(nowprice);
            shareProjectAdj.setType(0);
            shareProjectAdj.setBuytime(new Date());
            code = share_project_adjService.addProjectAdj(shareProjectAdj);
            map.put("code",0);
            map.put("piid",piid);
        }
        return map;
    }

    @RequestMapping(value = "/editproadj")
    @ResponseBody
    public Map editProject_adj(@RequestParam(value="piid",defaultValue = "",required=false) Integer piid,
                              @RequestParam(value="pid",defaultValue = "",required=false) Integer pid,
                               @RequestParam(value="sid",defaultValue = "",required=false) String sid,
                               @RequestParam(value="perecent",defaultValue = "",required=false) Float perecent
    ){

        float nowprice = share_project_infoService.getOnePrice(sid);
        float buymuch = share_project_infoService.getbuymuch(sid,perecent,pid);
        Integer code = 0;
        if(!share_project_infoService.isMoney(pid,buymuch*nowprice)){
            code = 1;
        }else {
            share_project_adj shareProjectAdj = new share_project_adj();
            shareProjectAdj.setPiId(piid);
            shareProjectAdj.setBuymuch(perecent);
            shareProjectAdj.setBuymoney(nowprice);
            shareProjectAdj.setType(0);
            shareProjectAdj.setBuytime(new Date());
            code = share_project_adjService.addProjectAdj(shareProjectAdj);
        }
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("code",code);
        return map;
    }

    @RequestMapping(value = "/projectinfo")
    @ResponseBody
    public Map project_info(@RequestParam(value="pid",defaultValue = "",required=false) Integer pid){

        Map<String,Object> map = new HashMap<String, Object>();
        map.put("code",0);

        Map<String,Object> info = new HashMap<String, Object>();
        info.put("name","西格玛粒子投资");
        info.put("info","面对锋范复杂的市场，我们的目标就是不亏钱");
        info.put("useday","253");
        info.put("yearprofit","56.96");
        info.put("allprofit","72.53");
        info.put("dayprofit","1.53");
        info.put("iswl","12.58");
        info.put("winpeople","99.58");
        info.put("maxback","-18.1");
        info.put("move","28");
        info.put("success","28.6");
        map.put("info",info);

        List list=new ArrayList();
        Map<String,Object> obj = new HashMap<String, Object>();
        obj.put("name","光明牛奶");
        obj.put("precent","15.5");
        obj.put("price","20.51");
        obj.put("wl","15.51");
        list.add(obj);
        Map<String,Object> obj2 = new HashMap<String, Object>();
        obj2.put("name","中国核电");
        obj2.put("precent","17.5");
        obj2.put("price","13.51");
        obj2.put("wl","15.51");
        list.add(obj2);
        map.put("sharelist",list);

        List valList=share_project_valueService.provalue_list(4,1);
        map.put("valList",valList);
        return map;
    }

    @RequestMapping(value = "/redis")
    @ResponseBody
    public Map redis(){

//        share_project_infoService.test();
        List<share_project_adj> shareProjectAdjList = share_project_adjService.projectAdj_list();
        Map<String,Object> map = new HashMap<String, Object>();
        return map;
    }
}
