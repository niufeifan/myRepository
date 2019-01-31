package com.springboot.model;

import java.util.Date;

public class VMApplicationInfo {
    private Integer id;
    //虚拟机名称
    private String vmname;
    //用途
    private String application;
    //系统名称
    private String systemname;
    //内存
    private Integer ram;
    //硬盘空间存储大小(G)
    private Integer memory;
    //cpu是几核的
    private Integer cpu;
    //数量(台)
    private Integer number;
    //申请人
    private String applicant;
    //所属部门
    private String section;
    //开始使用时间
    private Date startdate;
    //使用结束时间
    private Date enddate;
    //续约时长
    private Integer increasethetime;
    //备注
    private String remark;
    //申请人id
    private Integer applicantid;
    //页面容量
    private Integer pagesize;
    //当前页码
    private Integer currentpage;
    //页面数据截取标记
    private Integer pagelimit;
    //虚拟机用户名
    private String username;
    //虚拟机密码
    private String password;
    //虚拟机ip
    private String ip;
    //申请状态
    private Integer appState;

    private String environment;

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getAppState() {
        return appState;
    }

    public void setAppState(Integer appState) {
        this.appState = appState;
    }

    public Integer getPagesize() {
        return pagesize;
    }

    public void setPagesize(Integer pagesize) {
        this.pagesize = pagesize;
    }

    public Integer getCurrentpage() {
        return currentpage;
    }

    public void setCurrentpage(Integer currentpage) {
        this.currentpage = currentpage;
    }

    public Integer getPagelimit() {
        return pagelimit;
    }

    public void setPagelimit(Integer pagelimit) {
        this.pagelimit = pagelimit;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVmname() {
        return vmname;
    }

    public void setVmname(String vmname) {
        this.vmname = vmname;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getSystemname() {
        return systemname;
    }

    public void setSystemname(String systemname) {
        this.systemname = systemname;
    }

    public Integer getRam() {
        return ram;
    }

    public void setRam(Integer ram) {
        this.ram = ram;
    }

    public Integer getMemory() {
        return memory;
    }

    public void setMemory(Integer memory) {
        this.memory = memory;
    }

    public Integer getCpu() {
        return cpu;
    }

    public void setCpu(Integer cpu) {
        this.cpu = cpu;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getApplicant() {
        return applicant;
    }

    public void setApplicant(String applicant) {
        this.applicant = applicant;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public Date getStartdate() {
        return startdate;
    }

    public void setStartdate(Date startdate) {
        this.startdate = startdate;
    }

    public Date getEnddate() {
        return enddate;
    }

    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }

    public Integer getIncreasethetime() {
        return increasethetime;
    }

    public void setIncreasethetime(Integer increasethetime) {
        this.increasethetime = increasethetime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getApplicantid() {
        return applicantid;
    }

    public void setApplicantid(Integer applicantid) {
        this.applicantid = applicantid;
    }
}