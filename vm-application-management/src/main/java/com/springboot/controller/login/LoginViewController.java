package com.springboot.controller.login;

import com.alibaba.fastjson.JSON;
import com.springboot.model.UserInfo;
import com.springboot.service.LoginService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
public class LoginViewController {

    @Resource
    private LoginService loginService;

    /**
     * 登录
     * @param userInfo
     * @param request
     * @return
     */
    @RequestMapping("/login")
    @ResponseBody
    public String login(@RequestBody UserInfo userInfo, HttpServletRequest request){
        String errorMessage = loginService.login(userInfo,request);
        return errorMessage;
    }

    /**
     * 登出
     * @param request
     * @return
     */
    @RequestMapping("/logout")
    @ResponseBody
    public String logout(HttpServletRequest request){
        request.getSession().invalidate();
        System.out.println("已退出登录!");
        return "已退出登录!";

    }

    /**获得登录人员信息
     *
     * @param request
     * @return
     */
    @RequestMapping("/getUserInfo")
    @ResponseBody
    public String getUserInfo(HttpServletRequest request){
        Object userInfoObj =  request.getSession().getAttribute("userInfo");
        UserInfo userInfo = (UserInfo) userInfoObj;
        String userInfoString;
        if (null != userInfoObj && !userInfoObj.equals("")){
            userInfoString = JSON.toJSONString(userInfo);
            //userInfoString = "test";
        }else {
            userInfoString = "请登录";
        }
        System.out.println("获取登录用户信息...");
        return userInfoString;
    }

}
