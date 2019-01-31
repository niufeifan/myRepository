package com.springboot.model;

public class UserInfo {
    private Integer id;

    private String name;

    private String password;

    private String xlsPath;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getXlsPath() {
        return xlsPath;
    }

    public void setXlsPath(String xlsPath) {
        this.xlsPath = xlsPath;
    }
}