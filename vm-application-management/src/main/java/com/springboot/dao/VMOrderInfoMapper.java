package com.springboot.dao;

import com.springboot.model.UserInfo;
import com.springboot.model.VMApplicationInfo;
import com.springboot.model.VMOrderInfo;
import com.springboot.model.VmOrderInfoTem;

import java.util.List;

public interface VMOrderInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(VMOrderInfo record);

    int insertSelective(VMOrderInfo record);

    VMOrderInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(VMOrderInfo record);

    int updateByPrimaryKey(VMOrderInfo record);

    //根据条件查询订单信息
    List<VMOrderInfo> selectByCondition(VMOrderInfo vmOrderInfo);

    //根据申请人信息查询订单信息
    List<VmOrderInfoTem> selectByApplicantId(VMOrderInfo vmOrderInfo);

    Integer selectCountByCondition(VMOrderInfo vmOrderInfo);
    //查询所有订单信息
    List<VMOrderInfo> selectAll();
    //更新续约时间信息
    Integer updateIncreaseTheRenewalTimeById(VMOrderInfo vmOrderInfo);
}