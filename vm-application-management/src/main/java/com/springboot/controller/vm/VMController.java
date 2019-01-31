package com.springboot.controller.vm;

import com.springboot.dao.UserInfoMapper;
import com.springboot.model.UserInfo;
import com.springboot.model.UserInfoTem;
import com.springboot.model.VMApplicationInfo;
import com.springboot.model.VMOrderInfo;
import com.springboot.service.VMService;
import jxl.read.biff.BiffException;
import jxl.write.WriteException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;


@Controller
@RequestMapping("/VM/")
public class VMController {

    @Resource
    private VMService vmService;
    @Value("${local.address}")
    private String localAddress;

    /**
     * 根据条件查询虚拟机订单信息
     * @param vmOrderInfo
     * @return
     */
    @RequestMapping("selectVM")
    @ResponseBody
    public String selectVM(@RequestBody VMOrderInfo vmOrderInfo,HttpSession session){

         String vmOrderInfoList = vmService.SelectVMOrderInfoByCondition(vmOrderInfo,session);
        System.out.println("查到的虚拟机订单信息为--->" + vmOrderInfoList);
        return vmOrderInfoList;
    }

    /**
     * 查询所有待办信息
     * @return
     */
    @RequestMapping("myApplication")
    @ResponseBody
    public String selectMyApplication(@RequestBody VMApplicationInfo vmApplicationInfo){
        String vmApplicationInfoList = vmService.SelectVMApplicationInfo(vmApplicationInfo);
        System.out.println("所有待办信息为:" + vmApplicationInfoList);
        return vmApplicationInfoList;
    }

    /**
     * 查询我的已办信息
     * @param vmOrderInfoTem
     * @return
     */
    @RequestMapping("myOrder")
    @ResponseBody
    public String selectMyOrder(@RequestBody VMOrderInfo vmOrderInfoTem,HttpSession session){

        VMOrderInfo vmOrderInfo = new VMOrderInfo();
        vmOrderInfo.setAdminid(vmOrderInfoTem.getId());
        vmOrderInfo.setCurrentpage(vmOrderInfoTem.getCurrentpage());
        vmOrderInfo.setPagesize(vmOrderInfoTem.getPagesize());

        String vmOrderInfoList = vmService.SelectVMOrderInfoByCondition(vmOrderInfo,session);
        System.out.println("我的已办信息为--->" + vmOrderInfoList);
        return vmOrderInfoList;
    }

    /**
     * 管理员将申请信息转换为订单
     * @param vmOrderInfo
     * @return
     */
    @RequestMapping("transitionToOrder")
    @ResponseBody
    public String transitionToOrder(@RequestBody VMOrderInfo vmOrderInfo){

        String errorMessage = vmService.insertApplicationToOrder(vmOrderInfo);
        System.out.println("管理员同意申请操作完成");
        return errorMessage;
    }

    /**
     * 管理员拒绝申请
     * @param vmApplicationInfo
     * @return
     */
    @RequestMapping("disTransitionToOrder")
    @ResponseBody
    public String disTransitionToOrder(@RequestBody VMApplicationInfo vmApplicationInfo){
        String errorMessage = vmService.disApplicationToOrder(vmApplicationInfo);
        System.out.println("管理员已拒绝此条申请");
        return errorMessage;
    }



    @Resource
    private UserInfoMapper userInfoMapper;

    @RequestMapping("test")
    @ResponseBody
    public String test(){

        VMOrderInfo vmOrderInfo = new VMOrderInfo();
        vmOrderInfo.setId(3);
        vmOrderInfo.setUsername("username003");
        vmOrderInfo.setPassword("password003");
        vmOrderInfo.setAdmin("user01");
        vmOrderInfo.setAdminid(1);
        vmOrderInfo.setEnvironment("云环境003");
        vmOrderInfo.setIp("127.0.0.1");
        String errorMessage = vmService.insertApplicationToOrder(vmOrderInfo);
        return errorMessage;

    }

    /**
     * 导出数据
     * @param response
     * @throws IOException
     * @throws WriteException
     */
    @RequestMapping("exportData")
    public void exportData(HttpServletResponse response,@RequestBody UserInfo userInfo){
        try {
            vmService.exportData(response,userInfo);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 导入数据
     * @param file
     */
    @RequestMapping("importData")
    @ResponseBody
    public String importData(MultipartFile file, HttpSession session){

        String errorMessage;
        try {
            errorMessage = vmService.importData(file,session,localAddress);
        } catch (IOException e) {
            errorMessage = "failed";
            e.printStackTrace();
        } catch (BiffException e) {
            errorMessage = "failed";
            e.printStackTrace();
        }

        return errorMessage;
    }

    /**
     * 判断申请性质
     * @return
     */
    @RequestMapping("judgeApplication")
    @ResponseBody
    public String judgeApplication(@RequestBody VMApplicationInfo vmApplicationInfo){
        String errorMessage = vmService.judgeApplication(vmApplicationInfo);
        return errorMessage;
    }

    /**
     * 增加虚拟机使用时间批复
     * @param vmOrderInfo
     * @return
     */
    @RequestMapping("increaseTheRenewalTime")
    @ResponseBody
    public String increaseTheRenewalTime(@RequestBody VMOrderInfo vmOrderInfo){
        String errorMessage = vmService.increaseTheRenewalTime(vmOrderInfo);
        return errorMessage;
    }

}
