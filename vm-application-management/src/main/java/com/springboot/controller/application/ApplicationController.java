package com.springboot.controller.application;

import com.springboot.model.VMApplicationInfo;
import com.springboot.model.VMOrderInfo;
import com.springboot.service.ApplicationService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/application/")
public class ApplicationController {

    @Resource
    private ApplicationService applicationService;

    /**
     * 用户申请虚拟机
     * @param vmApplicationInfo
     * @return
     */
    @RequestMapping("xmApplication")
    @ResponseBody
    public String xmApplication(@RequestBody VMApplicationInfo vmApplicationInfo, HttpSession session){
        String errorMessage = applicationService.insertVMApplication(vmApplicationInfo,session);
        System.out.println("申请虚机操作完成");
        return errorMessage;
    }

    /**
     * 用户查看历史申请
     * @param vmOrderInfo
     * @return
     */
    @RequestMapping("historyApplication")
    @ResponseBody
    public String historyApplication(@RequestBody VMOrderInfo vmOrderInfo){
        String applicationList = applicationService.selectHistoryApplication(vmOrderInfo);
        System.out.println("查看历史申请");
        return applicationList;
    }

    /**
     * 用户查看历史申请测试
     * @param
     * @return
     */
    @RequestMapping("testHistory")
    @ResponseBody
    public String test(){
        VMOrderInfo vmOrderInfo = new VMOrderInfo();

        vmOrderInfo.setId(1);
        vmOrderInfo.setPagesize(1);
        vmOrderInfo.setCurrentpage(1);
        String applicationList = applicationService.selectHistoryApplication(vmOrderInfo);
        return applicationList;
    }


    /**
     * 用户申请续约
     * @param vmApplicationInfo
     * @return
     */
   /* @RequestMapping("applicationAddTime")
    @ResponseBody
    public String applicationAddTime(@RequestBody VMApplicationInfo vmApplicationInfo){
        String errorMessage = applicationService.applicationAddTime(vmApplicationInfo);
        return errorMessage;
    }*/

}
