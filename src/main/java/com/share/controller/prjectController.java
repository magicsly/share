package com.share.controller;

import org.aspectj.apache.bcel.classfile.Code;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import com.share.model.share_project;
import com.share.model.share_project_info;


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
                                @RequestParam(value="size",defaultValue = "",required=false) float size
    ){
        share_project_info shareProjectInfo = new share_project_info();
        shareProjectInfo.setPid(pid);
        shareProjectInfo.setSid(sid);
        share_project_infoService.addProjectInfo(shareProjectInfo,size);
        Map<String,Object> map = new HashMap<String, Object>();
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
        info.put("move","28.6");
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
        map.put("list",list);

        return map;
    }

    @RequestMapping(value = "/redis")
    @ResponseBody
    public Map redis(){

        share_project_infoService.test();
        Map<String,Object> map = new HashMap<String, Object>();
        return map;
    }
}
