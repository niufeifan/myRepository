package com.springboot.service.impl;

import com.springboot.dao.UserInfoMapper;
import com.springboot.model.UserInfo;
import com.springboot.service.LoginService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Service
public class LoginServiceImpl implements LoginService {

    @Resource
    private UserInfoMapper userInfoMapper;

    @Override
    public String login(UserInfo userInfo, HttpServletRequest request) {

        if (null == userInfo.getName() || null == userInfo.getPassword() || userInfo.getName() == "" || userInfo.getPassword() == ""){
            return "用户名或密码不能为空...";
        }
        //查询用户名密码是否匹配
        UserInfo loginUserInfo = userInfoMapper.selectUserInfo(userInfo);

        if (loginUserInfo == null) {
            return "用户名密码不匹配!";
        }

        UserInfo returnUserInfo = new UserInfo();
        returnUserInfo.setId(loginUserInfo.getId());
        returnUserInfo.setName(loginUserInfo.getName());

        // 校验通过时，在session里放入一个标识
        // 后续通过session里是否存在该标识来判断用户是否登录
        request.getSession().setAttribute("userInfo", returnUserInfo);
        return "登陆成功";

    }
}
