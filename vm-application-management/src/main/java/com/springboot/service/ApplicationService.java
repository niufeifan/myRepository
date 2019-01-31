package com.springboot.service;

import com.springboot.model.VMApplicationInfo;
import com.springboot.model.VMOrderInfo;

import javax.servlet.http.HttpSession;

public interface ApplicationService {
    //用户提交虚拟机申请操作
    String insertVMApplication(VMApplicationInfo vmApplicationInfo, HttpSession session);
    //用户查看历史申请
    String selectHistoryApplication(VMOrderInfo vmOrderInfo);
    //用户申请续约
    //String applicationAddTime(VMApplicationInfo vmApplicationInfo);
}
