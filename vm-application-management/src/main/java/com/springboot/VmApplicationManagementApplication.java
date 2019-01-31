package com.springboot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.springboot.dao")
@EnableTransactionManagement
public class VmApplicationManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(VmApplicationManagementApplication.class, args);
    }

}

