package com.springboot.dao;

import com.springboot.model.VMApplicationInfo;
import com.springboot.model.VMOrderInfo;
import com.springboot.model.VmOrderInfoTem;

import java.util.List;

public interface VMApplicationInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(VMApplicationInfo record);

    int insertSelective(VMApplicationInfo record);

    VMApplicationInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(VMApplicationInfo record);

    int updateByPrimaryKey(VMApplicationInfo record);

    //查询所有申请信息
    List<VMApplicationInfo> selectAllApplicationInfo(VMApplicationInfo vmApplicationInfo);

    //根据申请人id查询申请信息
    List<VmOrderInfoTem> selectByApplicantId(VMOrderInfo vmOrderInfo);
    //根据条件查询申请信息
    List<VMOrderInfo> selectByMessage(VMOrderInfo vmOrderInfo);
    //查询申请信息总条数
    Integer selectCount(VMApplicationInfo vmApplicationInfo);
    //根据订单id查询申请id(续约时)
    VMApplicationInfo selectByAppState(VMApplicationInfo id);
}