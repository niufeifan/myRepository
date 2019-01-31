package com.springboot.model;

import java.util.List;

public class ReturnVO<T> {

    private List<T> menuList;

    private String address;

    public List<T> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<T> menuList) {
        this.menuList = menuList;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
