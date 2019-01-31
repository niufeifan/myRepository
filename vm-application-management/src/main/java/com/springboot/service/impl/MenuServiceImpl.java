package com.springboot.service.impl;

import com.alibaba.fastjson.JSON;
import com.springboot.dao.EnvironmentMapper;
import com.springboot.model.Environment;
import com.springboot.service.MenuService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

@Service
public class MenuServiceImpl implements MenuService {

    @Resource
    private EnvironmentMapper environmentMapper;

    @Override
    public String environment() {

        List<Environment> environmentList = environmentMapper.selectAll();

        String environmentListJSON = JSON.toJSONString(environmentList);

        return environmentListJSON;
    }
}
