package com.share.service;

import com.share.dao.share_userMapper;
import com.share.model.share_user;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.share.util.util;

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
            Integer code = 0;


            return code;
        }catch (Exception e){
            return -1;
        }
    }

    public Integer confUser(share_user user , Integer type) {
        Integer code = 0;
        if (type == 0){//如果是注册,检查用户名和密码
            //验证用户名
            if (user.getUname() == "") {
                code = 1001;
            } else if (user.getUname().length() > 20) {
                code = 1002;
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
        return code;
    }

    public String userMsg (Integer code){
        String msg = "成功";
        switch (code){
            case 1001 : msg = "请输入用户名" ;
            case 1002 : msg = "用户名过长" ;

        }
        return msg;
    }
}
