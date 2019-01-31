package com.springboot.service;

import com.springboot.model.Permission;
import com.springboot.model.UserInfo;
import com.springboot.model.UserInfoTem;

import java.util.List;

public interface PermissionService {
    //获得跳转到的页面
    String toPageAddr(UserInfo userInfo);
    //获得菜单信息
    List<Permission> toMenu(UserInfo userInfo);
    //添加用户
    String addUserInfo(UserInfo userInfo);

    String changePassword(UserInfoTem userInfoTem);
}
