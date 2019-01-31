package com.springboot.controller.environment;

import com.springboot.service.MenuService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;


@Controller
@RequestMapping("/dic/")
public class EnvironmentController {

    @Resource
    private MenuService menuService;

    /**
     * 获得云主机列表
     * @return
     */
    @RequestMapping("getEnvironment")
    @ResponseBody
    public String getEnvironment(){
        String environmentList = menuService.environment();
        return environmentList;
    }

}
