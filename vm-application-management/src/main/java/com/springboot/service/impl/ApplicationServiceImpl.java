package com.springboot.service.impl;

import com.alibaba.fastjson.JSON;
import com.springboot.dao.UserInfoMapper;
import com.springboot.dao.VMApplicationInfoMapper;
import com.springboot.dao.VMOrderInfoMapper;
import com.springboot.dao.VmOrderInfoTemMapper;
import com.springboot.model.*;
import com.springboot.service.ApplicationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

@Service
@Transactional
public class ApplicationServiceImpl implements ApplicationService {

    @Resource
    private VMApplicationInfoMapper vmApplicationInfoMapper;
    @Resource
    private VMOrderInfoMapper vmOrderInfoMapper;
    @Resource
    private VmOrderInfoTemMapper vmOrderInfoTemMapper;
    @Resource
    private UserInfoMapper userInfoMapper;

    /**
     * 用户提交虚拟机申请操作
     * @param vmApplicationInfo
     * @param session
     * @return
     */
    @Override
    public String insertVMApplication(VMApplicationInfo vmApplicationInfo, HttpSession session) {

        /*Date date = vmApplicationInfo.getStartdate();
        System.out.println(date.getTime());*/

        UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");

        Integer userNum = userInfoMapper.ifUser(userInfo);
        String returnMessage;
        if (userNum != 0){
            vmApplicationInfoMapper.insertSelective(vmApplicationInfo);
            returnMessage = "续约申请提交成功";
        }else {
            returnMessage = "您没有申请虚拟机的权限!";
        }

        return returnMessage;
    }

    /**
     * 用户查看历史申请
     * @param vmOrderInfo
     * @return
     */
    @Override
    public String selectHistoryApplication(VMOrderInfo vmOrderInfo) {

        //首先查寻出未处理申请
        List<VmOrderInfoTem> vmApplicationList = vmApplicationInfoMapper.selectByApplicantId(vmOrderInfo);

        //然后查出已处理申请(订单)
        List<VmOrderInfoTem> vmOrderList = vmOrderInfoMapper.selectByApplicantId(vmOrderInfo);

        //然后遍历vmApplicationList
        for ( VmOrderInfoTem vmOrderInfoTem :vmApplicationList) {
            vmOrderInfoTem.setState("未办");
            vmOrderInfoTem.setId(null);
            vmOrderInfoTemMapper.insertSelective(vmOrderInfoTem);
        }

        //遍历vmOrderList
        for ( VmOrderInfoTem vmOrderInfoTem :vmOrderList) {
            vmOrderInfoTem.setAppState(vmOrderInfoTem.getId());
            vmOrderInfoTem.setId(null);
            vmOrderInfoTemMapper.insertSelective(vmOrderInfoTem);
        }

        //每页显示条数
        Integer pageSize = vmOrderInfo.getPagesize();
        //当前页
        Integer currentPage = vmOrderInfo.getCurrentpage();
        //显示从第几个数据开始截取
        Integer pageLimit = pageSize * (currentPage-1);
        vmOrderInfo.setPagelimit(pageLimit);

        //总记录条数
        Integer totalRecord = vmOrderInfoTemMapper.selectCount(vmOrderInfo);
        //总页数
        Integer totalPageNum = (totalRecord + pageSize - 1) / pageSize;

        //查询申请信息
        List<VMOrderInfo> vmOrderInfoList = vmOrderInfoTemMapper.selectAll(vmOrderInfo);

        PaginationVO<VMOrderInfo> paginationVO = new PaginationVO<VMOrderInfo>();
        paginationVO.setTotalList(vmOrderInfoList);
        paginationVO.setTotalPageNum(totalPageNum);
        paginationVO.setTotalRecord(totalRecord);

        //删除临时表中的信息
        vmOrderInfoTemMapper.delectAll(vmOrderInfo);

        String historyApplication = JSON.toJSONString(paginationVO);

        return historyApplication;
    }

    /**
     * 用户申请续约
     * @param vmApplicationInfo
     * @return
     */
   /* @Override
    public String applicationAddTime(VMApplicationInfo vmApplicationInfo) {

        //1:表示是续约申请
        vmApplicationInfo.setState(vmApplicationInfo.getId());
        vmApplicationInfo.setId(null);

        Integer insertNum = vmApplicationInfoMapper.insertSelective(vmApplicationInfo);

        if (insertNum == 1){
            return "申请提交成功";
        }

        return "申请提交失败";
    }*/


}
