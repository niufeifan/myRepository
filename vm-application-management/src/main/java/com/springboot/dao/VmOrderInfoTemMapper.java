package com.springboot.dao;

import com.springboot.model.VMOrderInfo;
import com.springboot.model.VmOrderInfoTem;

import java.util.List;

public interface VmOrderInfoTemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(VmOrderInfoTem record);

    int insertSelective(VmOrderInfoTem record);

    VmOrderInfoTem selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(VmOrderInfoTem record);

    int updateByPrimaryKey(VmOrderInfoTem record);

    List<VMOrderInfo> selectAll(VMOrderInfo vmOrderInfo);

    void delectAll(VMOrderInfo vmOrderInfo);
    //查询历史申请信息总条数
    Integer selectCount(VMOrderInfo vmOrderInfo);
}