package com.springboot.controller.permission;

import com.alibaba.fastjson.JSON;
import com.springboot.model.Permission;
import com.springboot.model.ReturnVO;
import com.springboot.model.UserInfo;
import com.springboot.model.UserInfoTem;
import com.springboot.service.PermissionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/permission/")
public class PermissionController {

    @Resource
    PermissionService permissionService;

    /**
     * 获得首页菜单
     * @param userInfo
     * @return
     */
    @RequestMapping("afterLogin")
    @ResponseBody
    public String afterLogin(@RequestBody UserInfo userInfo){
        //@RequestBody UserInfo userInfo, Model model
        /*UserInfo userInfo = new UserInfo();
        userInfo.setId(1);*/
        //获得跳转到的页面
        String toRole = permissionService.toPageAddr(userInfo);
        //获得菜单信息
        List<Permission> menuMessage = permissionService.toMenu(userInfo);
        System.out.println(menuMessage);
        ReturnVO<Permission> returnVO = new ReturnVO<>();
        returnVO.setAddress(toRole);
        returnVO.setMenuList(menuMessage);

        String returnMessage = JSON.toJSONString(returnVO);
        return returnMessage;
    }

    /**
     * 添加用户
     * @param userInfo
     * @return
     */
    @RequestMapping("addUserInfo")
    @ResponseBody
    public String addUserInfo(@RequestBody UserInfo userInfo){
        String errorMessage  = permissionService.addUserInfo(userInfo);
        System.out.println("添加用户");
        return errorMessage;
    }

    /**
     * 修改密码
     * @param userInfoTem
     * @return
     */
    @RequestMapping("changePassword")
    @ResponseBody
    public String changePassword(@RequestBody UserInfoTem userInfoTem){
        String errorMessage = permissionService.changePassword(userInfoTem);
        System.out.println("修改密码");
        return errorMessage;
    }


}
