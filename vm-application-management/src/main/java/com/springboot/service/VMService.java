package com.springboot.service;

import com.springboot.model.UserInfo;
import com.springboot.model.VMApplicationInfo;
import com.springboot.model.VMOrderInfo;
import jxl.read.biff.BiffException;
import jxl.write.WriteException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public interface VMService {
    //String SelectVMApplicationInfoByCondition(VMApplicationInfo vmApplicationInfo);
    String SelectVMOrderInfoByCondition(VMOrderInfo vmOrderInfo,HttpSession session);

    //查询所有待办信息
    String SelectVMApplicationInfo(VMApplicationInfo vmApplicationInfo);
    //管理员将申请信息转换为订单
    String insertApplicationToOrder(VMOrderInfo vmOrderInfo);
    //管理员拒绝申请操作
    String disApplicationToOrder(VMApplicationInfo vmApplicationInfo);

    String test();
    //导出数据
    void exportData(HttpServletResponse response, UserInfo userInfo) throws IOException, WriteException;
    //导入数据
    String importData(MultipartFile file, HttpSession session, String localAddress) throws IOException, BiffException;
    //判断申请性质
    String judgeApplication(VMApplicationInfo vmApplicationInfo);
    //转换续约申请
    String increaseTheRenewalTime(VMOrderInfo vmOrderInfo);
}
