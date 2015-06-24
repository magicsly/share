package com.share.service;

import com.share.dao.share_userMapper;
import com.share.model.share_user;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.share.util.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ElNino on 15/6/15.
 */
@Service
public class share_userService {

    @Autowired
    share_userMapper share_userMapper;

    util util = new util();

    public Integer regist(share_user user){
        try {
            Integer code = confUser(user,0);
            if(code == 0){
                String md5pw =DigestUtils.md5Hex(user.getPassword());
                user.setPassword(md5pw);
                user.setCreatime(new Date());
                share_userMapper.insertSelective(user);
            }
            return code;
        }catch (Exception e){
            return -1;
        }
    }

    public Integer editUser(share_user user){
        try {
            Integer code = confUser(user,1);
            if(code == 0){
                share_userMapper.updateByPrimaryKeySelective(user);
            }
            return code;
        }catch (Exception e){
            return -1;
        }
    }

    public Integer login(share_user user){
        try {
            String md5pw =DigestUtils.md5Hex(user.getPassword());
            user.setPassword(md5pw);
            Integer count = share_userMapper.userlogin(user);
            if(count>0){
                return 0;
            }else {
                return 1;
            }
        }catch (Exception e){
            return -1;
        }
    }

    public Map userinfo(Integer uid){
        Map<String,Object> map = new HashMap<String, Object>();
        try {
            share_user user = share_userMapper.selectByPrimaryKey(uid);
            user.setPassword(null);
            map.put("code",0);
            map.put("info",user);
            return map;
        }catch (Exception e){
            map.put("code",-1);
            return map;
        }
    }

    public Integer confUser(share_user user , Integer type) {
        Integer code = 0;
        if (type == 0){//如果是注册,检查用户名和密码
            Integer isuser = share_userMapper.isuser(user.getUname());
            if(isuser > 0) {//用户名存在
                code = 1001;
            }
            //验证用户名
            if (user.getUname() == "") {
                code = 1002;
            } else if (user.getUname().length() > 20) {
                code = 1003;
            }
            //验证密码
            if(user.getPassword()==""){
                code = 2001;
            }else if(user.getPassword().length() > 20){
                code = 2002;
            }else if(user.getPassword().length() < 6){
                code = 2003;
            }
        }
        if(user.getEmail()!=""){
            if (!user.getEmail().matches("[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+")) {
                code = 3001;
            }
        }
        if(user.getMobile()!=""){
            if (!user.getMobile().matches("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$")) {
                code = 4001;
            }
        }
        if(user.getInfo()!=""){

        }
        return code;
    }

    public String userMsg (Integer code){
        String msg = "成功";
        switch (code){
            case 1001 : msg = "用户名已存在";break;
            case 1002 : msg = "请输入用户名";break;
            case 1003 : msg = "用户名过长";break;
            case 2001 : msg = "密码不能为空";break;
            case 2002 : msg = "密码不能大于20位";break;
            case 2003 : msg = "密码不能少于6位";break;
            case 3001 : msg = "邮箱错误";break;
            case 4001 : msg = "手机号码错误";break;

        }
        return msg;
    }
}
