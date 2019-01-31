package com.springboot.dao;

import com.springboot.model.Environment;

import java.util.List;

public interface EnvironmentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Environment record);

    int insertSelective(Environment record);

    Environment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Environment record);

    int updateByPrimaryKey(Environment record);

    //查询云环境列表
    List<Environment> selectAll();
}