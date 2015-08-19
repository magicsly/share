package com.share.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;
import com.share.model.share_user;
import com.share.service.share_userService;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by ElNino on 15/6/11.
 */
@Controller
@RequestMapping("/")
public class userController {

    @Autowired
    com.share.service.share_userService share_userService;

    /**
     * 用户注册
     * @param uname 用户名
     * @param password 密码
     * @param sex
     * @param info
     * @param email
     * @param mobile
     * @param image
     * @param level
     * @return
     */
    @RequestMapping(value = "/regrist")
    @ResponseBody
    public Map regrist(@RequestParam(value="uname",defaultValue = "",required=false) String uname,
                       @RequestParam(value="password",defaultValue = "",required=false) String password,
                       @RequestParam(value= "sex",defaultValue = "0",required=false) Byte sex,
                       @RequestParam(value="info",defaultValue = "",required=false) String info,
                       @RequestParam(value="email",defaultValue = "",required=false) String email,
                       @RequestParam(value="mobile",defaultValue = "",required=false) String mobile,
                       @RequestParam(value="image",defaultValue = "",required=false) String image,
                       @RequestParam(value="level",defaultValue = "0",required=false) Byte level
                       ) {

        share_user share_user = new share_user();
        share_user.setUname(uname);
        share_user.setPassword(password);
        share_user.setSex(sex);
        share_user.setInfo(info);
        share_user.setEmail(email);
        share_user.setImage(image);
        share_user.setLev(level);
        share_user.setMobile(mobile);
        share_user.setIsok((byte) 0);
        Integer code = share_userService.regist(share_user);
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("code",code);
        map.put("msg",share_userService.userMsg(code));
        return map;

    }

    @RequestMapping(value = "/login")
    @ResponseBody
    public Map login(@RequestParam(value="uname",defaultValue = "",required=false) String uname,
                     @RequestParam(value="password",defaultValue = "",required=false) String password,
                     HttpServletRequest request,HttpSession session
                    ){
        share_user share_user = new share_user();
        share_user.setUname(uname);
        share_user.setPassword(password);
        Integer code = share_userService.login(share_user,request,session);
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("code",code);
        return map;
    }

    @RequestMapping(value = "/errorlogin")
    @ResponseBody
    public Map login(){
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("code","-1");
        map.put("msg","用户未登录");
        return map;
    }

    @RequestMapping(value = "/edituser")
    @ResponseBody
    public Map edituser(@RequestParam(value="uid",defaultValue = "",required=false) Integer uid,
                        @RequestParam(value= "sex",defaultValue = "0",required=false) Byte sex,
                        @RequestParam(value="info",defaultValue = "",required=false) String info,
                        @RequestParam(value="email",defaultValue = "",required=false) String email,
                        @RequestParam(value="mobile",defaultValue = "",required=false) String mobile,
                        @RequestParam(value="image",defaultValue = "",required=false) String image,
                        @RequestParam(value="level",defaultValue = "0",required=false) Byte level
    ) {

        share_user share_user = new share_user();
        share_user.setUid(uid);
        share_user.setSex(sex);
        share_user.setInfo(info);
        share_user.setEmail(email);
        share_user.setImage(image);
        share_user.setLev(level);
        share_user.setMobile(mobile);
        share_user.setIsok((byte) 0);
        Integer code = share_userService.editUser(share_user);
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("code",code);
        map.put("msg",share_userService.userMsg(code));
        return map;
    }

    @RequestMapping(value = "/userinfo")
    @ResponseBody
    public Map userinfo() {
        Map<String,Object> map = new HashMap<String, Object>();
        map = share_userService.userinfo();
        return map;
    }

    @RequestMapping(value = "/editpw")
    @ResponseBody
    public Map editpw() {

        Map<String,Object> map = new HashMap<String, Object>();
        return map;
    }



}
