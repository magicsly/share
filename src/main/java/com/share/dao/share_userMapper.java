package com.share.dao;

import com.share.model.share_user;
import org.springframework.stereotype.Repository;

@Repository
public interface share_userMapper {
    int deleteByPrimaryKey(Integer uid);

    int insert(share_user record);

    int insertSelective(share_user record);

    share_user selectByPrimaryKey(Integer uid);

    share_user selectByUname(String uname);

    int updateByPrimaryKeySelective(share_user record);

    int updateByPrimaryKey(share_user record);

    int userlogin (share_user record);

    int isuser (String uname);
}