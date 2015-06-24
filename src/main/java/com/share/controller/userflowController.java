package com.share.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ElNino on 15/6/17.
 */
@Controller
@RequestMapping("/")
public class userflowController {
    @Autowired
    com.share.service.share_flowService share_flowService;

    @RequestMapping(value = "/userflow")
    @ResponseBody
    public Map userflow(@RequestParam(value="uid",defaultValue = "",required=false) Integer uid,
                        @RequestParam(value="fid",defaultValue = "",required=false) Integer fid){
        Integer code = share_flowService.userFlow(uid, fid);
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("code",code);
        return map;
    }

    @RequestMapping(value = "/userflow_list")
    @ResponseBody
    public Map userflow_list() {

        Map<String,Object> map = new HashMap<String, Object>();
        return map;
    }

    @RequestMapping(value = "/userfans_list")
    @ResponseBody
    public Map userfans_list() {

        Map<String,Object> map = new HashMap<String, Object>();
        return map;
    }
}
