package com.springboot.service;

import com.springboot.model.UserInfo;

import javax.servlet.http.HttpServletRequest;

public interface LoginService {
    //登陆操作
    String login(UserInfo userInfo, HttpServletRequest request);
}
