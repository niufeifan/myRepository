package com.springboot.dao;

import com.springboot.model.Permission;
import com.springboot.model.UserInfo;
import com.springboot.model.VMApplicationInfo;

import java.util.List;

public interface UserInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserInfo record);

    int insertSelective(UserInfo record);

    UserInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserInfo record);

    int updateByPrimaryKey(UserInfo record);

    //根据用户信息查询是否是管理员
    Integer selectIfAdmin(UserInfo userInfo);
    //查询
    String SelectPageAddress(UserInfo userInfo);

    List<Permission> selectMenuMessageTop(UserInfo userInfo);

    List<Permission> selectMenuMessageSec(UserInfo userInfo);

    //检查用户名是否重复
    Integer selectIfRepetition(UserInfo userInfo);
    //根据用户名查id
    Integer selectByUserName(UserInfo userInfo);
    //根据用户id,用户名,用户密码查询用户
    Integer selectUserMessage(UserInfo userInfo);
    //根据用户名,密码查询用户信息
    UserInfo selectUserInfo(UserInfo userInfo);
    //检查是否是普通用户
    Integer ifUser(UserInfo vmApplicationInfo);
}