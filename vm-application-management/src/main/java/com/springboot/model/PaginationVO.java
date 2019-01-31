package com.springboot.model;

import java.util.List;

public class PaginationVO<T> {

    /*//每页显示条数
    Integer pageSize = vmOrderInfo.getPagesize();
    //当前页
    Integer currentPage = vmOrderInfo.getCurrentpage();
    //显示从第几个数据开始截取
    Integer pageLimit = pageSize * (currentPage-1);
            vmOrderInfo.setPagelimit(pageLimit);
    //总记录条数
    Integer totalRecord = vmOrderInfoMapper.selectTotalRecord();
    //总页数
    Integer totalPageNum = (totalRecord + pageSize - 1) / pageSize;*/

    //总记录条数
    private Integer totalRecord;
    //总页数
    private Integer totalPageNum;
    //数据列表
    private List<T> totalList;

    public Integer getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(Integer totalRecord) {
        this.totalRecord = totalRecord;
    }

    public Integer getTotalPageNum() {
        return totalPageNum;
    }

    public void setTotalPageNum(Integer totalPageNum) {
        this.totalPageNum = totalPageNum;
    }

    public List<T> getTotalList() {
        return totalList;
    }

    public void setTotalList(List<T> totalList) {
        this.totalList = totalList;
    }
}
