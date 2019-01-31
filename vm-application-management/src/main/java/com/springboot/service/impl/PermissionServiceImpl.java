package com.springboot.service.impl;

import com.springboot.dao.UserInfoMapper;
import com.springboot.dao.UserRoleMapper;
import com.springboot.model.*;
import com.springboot.service.PermissionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PermissionServiceImpl implements PermissionService {

    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private UserRoleMapper userRoleMapper;

    /**
     * 获得要跳转到的页面
     * @param userInfo
     * @return
     */
    @Override
    public String toPageAddr(UserInfo userInfo) {

        String pageAddress = userInfoMapper.SelectPageAddress(userInfo);

        return pageAddress;
    }

    /**
     * 获得菜单信息
     * @param userInfo
     * @return
     */
    @Override
    public List<Permission> toMenu(UserInfo userInfo) {

        //顶层菜单
        List<Permission> permissions = userInfoMapper.selectMenuMessageTop(userInfo);
        //子菜单
        List<Permission> permissionList = userInfoMapper.selectMenuMessageSec(userInfo);

        List<Permission> menuMessageList = new ArrayList<Permission>();

        for (Permission permissionTop:permissions) {
            List<Permission> permissiones = new ArrayList<Permission>();
            for (Permission permissionSec:permissionList){
                if (permissionSec.getParentid() == permissionTop.getId()){
                    permissiones.add(permissionSec);
                }
            }
            permissionTop.setChildren(permissiones);
            menuMessageList.add(permissionTop);
        }

        return menuMessageList;
    }

    /**
     * 添加用户
     * @param userInfo
     * @return
     */
    @Override
    public String addUserInfo(UserInfo userInfo) {

        //先检查有没有相同用户名
        Integer repetitionMessage = userInfoMapper.selectIfRepetition(userInfo);

        String errorMessage;
        //有相同用户名
        if (repetitionMessage != 0){
            errorMessage = "userInfo repetition";
        //没有相同用户名
        }else {
            //插入用户信息
            userInfoMapper.insert(userInfo);
            //获取所插入用户的id
            Integer userId = userInfoMapper.selectByUserName(userInfo);
            UserRole userRole = new UserRole();
            userRole.setRoleid(2);
            userRole.setUserid(userId);
            //在用户角色表中增加数据
            Integer addMessage = userRoleMapper.insert(userRole);

            if (addMessage == 1){
                errorMessage = "success";
            }else {
                errorMessage = "failed";
            }

        }

        return errorMessage;
    }

    /**
     * 修改密码
     * @param userInfoTem
     * @return
     */
    @Override
    public String changePassword(UserInfoTem userInfoTem) {

        UserInfo userInfo = new UserInfo();
        userInfo.setId(userInfoTem.getId());
        userInfo.setName(userInfoTem.getName());
        userInfo.setPassword(userInfoTem.getPassword());

        String returnMessage;

        //先检查密码是否正确
        Integer errorMessage = userInfoMapper.selectUserMessage(userInfo);
        //如果信息正确
        if (errorMessage == 1){
            //修改密码
            userInfo.setPassword(userInfoTem.getNewPassword());
            Integer updateMessage = userInfoMapper.updateByPrimaryKeySelective(userInfo);
            if (updateMessage != 0){
                returnMessage = "密码修改成功!";
            }else {
                returnMessage = "密码修改失败!";
            }
        }else {
            returnMessage = "初始密码不正确!";
        }

        return returnMessage;
    }
}
