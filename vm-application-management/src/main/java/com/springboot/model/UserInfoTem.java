package com.springboot.model;

public class UserInfoTem {

    private Integer id;

    private String name;

    private String password;

    private String newPassword;

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

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getXlsPath() {
        return xlsPath;
    }

    public void setXlsPath(String xlsPath) {
        this.xlsPath = xlsPath;
    }
}
