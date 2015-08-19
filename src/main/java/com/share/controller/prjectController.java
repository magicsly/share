package com.share.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
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
import com.share.model.share_user;


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

    @Autowired
    com.share.service.share_userService share_userService;

    @RequestMapping(value = "/addproject")
    @ResponseBody
    public Map addProject(@RequestParam(value="name",defaultValue = "",required=false) String name,
                     @RequestParam(value="info",defaultValue = "",required=false) String info,
                     @RequestParam(value="image",defaultValue = "",required=false) String image,
                     @RequestParam(value="type",defaultValue = "",required=false) Integer type,
                     @RequestParam(value="str",defaultValue = "",required=false) String str,
                     @RequestParam(value="uid",defaultValue = "",required=false) Integer uid
                     ){


//        str = share_project_infoService.getRound()+"|"+"0.3"+";"+share_project_infoService.getRound()+"|"+"0.3"+";"+share_project_infoService.getRound()+"|"+"0.3";
//        Random random = new Random();
//        name = "project_"+Math.abs(random.nextInt())%100000;
//        info = "test";
//        uid = Math.abs(random.nextInt())%1000;
        type = 1;
        //1登录
        //aop执行
        //2新建方案
        share_project shareProject=new share_project();
        shareProject.setName(name);
        shareProject.setInfo(info);
        shareProject.setImage(image);
        shareProject.setType(type);
        shareProject.setUid(uid);
        shareProject.setIsok((byte)0);
        Integer pid = share_projectService.addProject(shareProject);
        //3新建现金
        float money = share_project_infoService.moneyUpdate(pid,(float)1);
        String[] strArr = str.split(";");
        for(String sh : strArr){
            String sid = sh.split("\\|")[0];
            float precent = Float.parseFloat(sh.split("\\|")[1]);
            share_project_info shareProjectInfo = new share_project_info();
            shareProjectInfo.setPid(pid);
            shareProjectInfo.setSid(sid);
            shareProjectInfo.setCostprice((float)0);
            //4新建项目内容（多条）
            Integer piId = share_project_infoService.addProjectInfo(shareProjectInfo);
            share_project_adj shareProjectAdj = new share_project_adj();
            shareProjectAdj.setPiId(piId);
            shareProjectAdj.setAdjtimes(0);
            shareProjectAdj.setPercent(precent);
            //5新建项目调整（对应多条）
            Integer adjId = share_project_adjService.addProjectAdj(shareProjectAdj);
        }
        //调整完成
        Map<String,Object> map = new HashMap<String, Object>();
        if(pid>0){
            map.put("code",0);
            map.put("pid",pid);
        }else{
            map.put("code",-1);
        }
        return map;
    }



    @RequestMapping(value = "/editproject")
    @ResponseBody
    public Map editProject(@RequestParam(value="pid",defaultValue = "",required=false) Integer pid,
                          @RequestParam(value="str",defaultValue = "",required=false) String str
    ){
            //str = "33|sh600169|0.3;34|sh600168|0.4;-1|sh600160|0.2";
        String[] strArr = str.split(";");
        Integer adjtimes = share_project_adjService.getMaxTimes(pid)+1;
        for(String sh : strArr){
            Integer piId = Integer.parseInt(sh.split("\\|")[0]);
            String sid = sh.split("\\|")[1];
            float precent = Float.parseFloat(sh.split("\\|")[2]);
            if(piId == -1){
                share_project_info shareProjectInfo = new share_project_info();
                shareProjectInfo.setPid(pid);
                shareProjectInfo.setSid(sid);
                piId = share_project_infoService.addProjectInfo(shareProjectInfo);
            }
            //获取更新次数
            share_project_adj shareProjectAdj = new share_project_adj();
            shareProjectAdj.setPiId(piId);
            shareProjectAdj.setAdjtimes(adjtimes);
            shareProjectAdj.setPercent(precent);
            //5新建项目调整（对应多条）
            Integer adjId = share_project_adjService.addProjectAdj(shareProjectAdj);
        }

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

    @RequestMapping(value = "/projectinfo_list")
    @ResponseBody
    public Map projectInfo_list(@RequestParam(value="pid",defaultValue = "",required=false) Integer pid){
        List<share_project_info> proInfo_list = share_project_infoService.proInfolist(pid);
        float val = share_project_infoService.getProMoney(pid);
        List sharprice = new ArrayList();
            for(share_project_info shareProjectInfo : proInfo_list){
            if(!shareProjectInfo.getSid().equals("money")){
                sharprice.add(shareProjectInfo.getSid()+","+share_project_infoService.getOnePrice(shareProjectInfo.getSid()));
            }
        }
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("code", 0);
        map.put("val",val);
        map.put("list", proInfo_list);
        map.put("nowprice",sharprice);
        return map;
    }

    @RequestMapping(value = "/proadj_list")
    @ResponseBody
    public Map proAdj_list(@RequestParam(value="pid",defaultValue = "",required=false) Integer pid){

        List proList = share_project_adjService.getProAdj_list(pid);
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("code", 0);
        map.put("list", proList);
        return map;
    }

    @RequestMapping(value = "/projectOrderby")
    @ResponseBody
    public Map projectOrderby(@RequestParam(value="order",defaultValue = "",required=false) String order){

        List proList = share_projectService.porjectOrderby_list();
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("code", 0);
        map.put("list", proList);
        return map;
    }

    @RequestMapping(value = "/projectval_list")
    @ResponseBody
    public Map projectVal_list(){

        Map<String,Object> map = new HashMap<String, Object>();
        map.put("code", 0);
        map.put("list", share_projectService.allprojectValRank());
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
        try {
            map.put("code",0);

            share_project shareProject = share_projectService.getproInfo(pid);
            Map<String,Object> info = new HashMap<String, Object>();
            info.put("id",shareProject.getPid());
            info.put("name",shareProject.getName());
            info.put("info",shareProject.getInfo());
            info.put("follow",106);

            info.put("userimg",share_userService.getone(shareProject.getUid()).getImage());
            info.put("username",share_userService.getone(shareProject.getUid()).getUname());


            Map redisMap = share_projectService.getReisInfo(pid);
            info.put("creattime",redisMap.get("createtime"));
            info.put("updatetime",redisMap.get("updatetime"));
            info.put("yearprofit",redisMap.get("yearprofit"));
            info.put("yearprofitPer",redisMap.get("yearprofitPer"));
            info.put("iswl",redisMap.get("iswl"));
            info.put("iswlPer",redisMap.get("iswlPer"));
            info.put("useday",redisMap.get("useday"));
            info.put("usedayPer",redisMap.get("usedayPer"));
            info.put("allprofit",redisMap.get("allprofit"));
            info.put("allprofitPer",redisMap.get("allprofitPer"));
            info.put("dayprofit",redisMap.get("dayprofit"));
            info.put("dayprofitPer",redisMap.get("dayprofitPer"));
            info.put("maxDrawDown",redisMap.get("maxDrawDown"));
            info.put("maxDrawDownPer",redisMap.get("maxDrawDownPer"));
            info.put("move",redisMap.get("move"));
            info.put("movePer",redisMap.get("movePer"));
            info.put("success",redisMap.get("success"));
            info.put("successPer",redisMap.get("successPer"));

            map.put("info",info);

            List list=new ArrayList();
            list = share_project_infoService.getRedisInfoList(pid);
            map.put("sharelist",list);

            List valList=share_project_valueService.provalue_list(pid,1);
            map.put("valList",valList);
            return map;
        }catch (Exception e){
            map.put("code","-1");
            map.put("msg","方案错误");
            return map;
        }
    }

    @RequestMapping(value = "/getsid")
    @ResponseBody
    public Map getSid(@RequestParam(value="sname",defaultValue = "",required=false) String sname){
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("code",0);
        List list=new ArrayList();
        list = share_project_infoService.searchstock(sname);
        map.put("list",list);
        return map;
    }


    @RequestMapping(value = "/redis")
    @ResponseBody
    public Map redis(){
        float str = share_project_valueService.maxBack(99);
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("str",str);
        return map;
    }
}
