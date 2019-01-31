package com.springboot.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.springboot.dao.UserInfoMapper;
import com.springboot.dao.VMApplicationInfoMapper;
import com.springboot.dao.VMOrderInfoMapper;
import com.springboot.model.*;
import com.springboot.service.VMService;
import jxl.*;
import jxl.format.UnderlineStyle;
import jxl.read.biff.BiffException;
import jxl.write.*;
import jxl.write.Number;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import java.util.*;

@Service
@Transactional
public class VMServiceImpl implements VMService {

    @Resource
    private VMApplicationInfoMapper vmApplicationInfoMapper;
    @Resource
    private VMOrderInfoMapper vmOrderInfoMapper;
    @Resource
    private UserInfoMapper userInfoMapper;

    /**
     * 根据条件查询虚拟机订单信息
     *
     * @param vmOrderInfo
     * @return
     */
    @Override
    public String SelectVMOrderInfoByCondition(VMOrderInfo vmOrderInfo,HttpSession session) {

        if (vmOrderInfo.getApplicant() == "") {
            vmOrderInfo.setApplicant(null);
        }
        if (vmOrderInfo.getEnvironment() == "") {
            vmOrderInfo.setEnvironment(null);
        }

        //首先验证是否是管理员
        UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
        //检查是否是管理员
        Integer userNum = userInfoMapper.selectIfAdmin(userInfo);
        String vmOrderInfoList = null;
        if (userNum != 0) {

            //每页显示条数
            Integer pageSize = vmOrderInfo.getPagesize();
            //当前页
            Integer currentPage = vmOrderInfo.getCurrentpage();
            //显示从第几个数据开始截取
            Integer pageLimit = pageSize * (currentPage - 1);
            vmOrderInfo.setPagelimit(pageLimit);
            //总记录条数
            Integer totalRecord = vmOrderInfoMapper.selectCountByCondition(vmOrderInfo);
            //总页数
            Integer totalPageNum = (totalRecord + pageSize - 1) / pageSize;

            List<VMOrderInfo> orderInfoList = vmOrderInfoMapper.selectByCondition(vmOrderInfo);

            PaginationVO<VMOrderInfo> paginationVO = new PaginationVO<VMOrderInfo>();
            paginationVO.setTotalList(orderInfoList);
            paginationVO.setTotalPageNum(totalPageNum);
            paginationVO.setTotalRecord(totalRecord);

            vmOrderInfoList = JSON.toJSONString(paginationVO, SerializerFeature.WriteMapNullValue);
        }
        return vmOrderInfoList;
    }

    /**
     * 查询所有虚拟机申请待办信息
     *
     * @return
     */
    @Override
    public String SelectVMApplicationInfo(VMApplicationInfo vmApplicationInfo) {

        //每页显示条数
        Integer pageSize = vmApplicationInfo.getPagesize();
        //当前页
        Integer currentPage = vmApplicationInfo.getCurrentpage();
        //显示从第几个数据开始截取
        Integer pageLimit = pageSize * (currentPage - 1);
        vmApplicationInfo.setPagelimit(pageLimit);

        //总记录条数
        Integer totalRecord = vmApplicationInfoMapper.selectCount(vmApplicationInfo);
        //总页数
        Integer totalPageNum = (totalRecord + pageSize - 1) / pageSize;

        List<VMApplicationInfo> applicationInfoList = vmApplicationInfoMapper.selectAllApplicationInfo(vmApplicationInfo);

        List<VMApplicationInfo> applicationInfoListTem = new ArrayList<VMApplicationInfo>();
        for(VMApplicationInfo vmApplicationInfoTem:applicationInfoList){
            if (null != vmApplicationInfoTem.getIncreasethetime()){
                Integer increaseTheTime = vmApplicationInfoTem.getIncreasethetime();
                //86400000
                Date endDate = vmApplicationInfoTem.getEnddate();
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(endDate);
                calendar.add(calendar.DATE,increaseTheTime); //把日期往后增加一天,整数  往后推,负数往前移动
                /*Long time = endDate.getTime();
                time = time + increaseTheTime * 57600000;
                endDate = new Date(time);*/
                endDate=calendar.getTime(); //这个时间就是日期往后推一天的结果
                vmApplicationInfoTem.setEnddate(endDate);
            }
            applicationInfoListTem.add(vmApplicationInfoTem);
        }

        PaginationVO<VMApplicationInfo> paginationVO = new PaginationVO<VMApplicationInfo>();
        paginationVO.setTotalList(applicationInfoListTem);
        paginationVO.setTotalPageNum(totalPageNum);
        paginationVO.setTotalRecord(totalRecord);

        String vmApplicationInfoList = JSON.toJSONString(paginationVO, SerializerFeature.WriteMapNullValue);

        return vmApplicationInfoList;
    }

    /**
     * 管理员将申请信息转换为订单
     *
     * @param vmOrderInfo
     * @return
     */
    @Override
    public String insertApplicationToOrder(VMOrderInfo vmOrderInfo) {

        Integer deleteId = vmOrderInfo.getId();
        String errorMessage = "failed";
        //首先验证是否是管理员
        Integer adminId = vmOrderInfo.getAdminid();
        UserInfo userInfo = new UserInfo();
        userInfo.setId(adminId);
        //检查是否是管理员
        Integer userNum = userInfoMapper.selectIfAdmin(userInfo);
        //如果是管理员
        if (userNum != 0) {
            //申请信息id
            Integer id;
            //查询出申请信息
            VMApplicationInfo vmApplicationInfo;
            if (null == vmOrderInfo.getAppState()){
                id = vmOrderInfo.getId();
                vmApplicationInfo = vmApplicationInfoMapper.selectByPrimaryKey(id);
            }else {
                VMApplicationInfo vmApplicationInfoTem = new VMApplicationInfo();
                vmApplicationInfoTem.setId(deleteId);
                vmApplicationInfoTem.setAppState(vmOrderInfo.getAppState());
                vmApplicationInfo = vmApplicationInfoMapper.selectByAppState(vmApplicationInfoTem);
            }

            //添加订单信息
            vmOrderInfo.setId(null);
            vmOrderInfo.setApplicant(vmApplicationInfo.getApplicant());
            vmOrderInfo.setApplicantid(vmApplicationInfo.getApplicantid());
            vmOrderInfo.setApplication(vmApplicationInfo.getApplication());
            vmOrderInfo.setCpu(vmApplicationInfo.getCpu());
            vmOrderInfo.setEnddate(vmOrderInfo.getEnddate());
            vmOrderInfo.setIncreasethetime(vmApplicationInfo.getIncreasethetime());
            vmOrderInfo.setMemory(vmApplicationInfo.getMemory());
            vmOrderInfo.setNumber(vmApplicationInfo.getNumber());
            vmOrderInfo.setRam(vmApplicationInfo.getRam());
            vmOrderInfo.setRemark(vmApplicationInfo.getRemark());
            vmOrderInfo.setSection(vmApplicationInfo.getSection());
            vmOrderInfo.setStartdate(vmApplicationInfo.getStartdate());
            vmOrderInfo.setState("已同意");
            vmOrderInfo.setSystemname(vmApplicationInfo.getSystemname());
            vmOrderInfo.setVmname(vmApplicationInfo.getVmname());

            boolean flag = false;
            if (vmOrderInfo.getAppState() != null){
                flag = true;
            }

            //表示是续约订单
            if (flag == true){
                vmOrderInfo.setId(vmOrderInfo.getAppState());
                //先查出原有的续约时长
                /*VMOrderInfo vmOrderInfoTem = vmOrderInfoMapper.selectByPrimaryKey(vmOrderInfo.getId());
                Integer increaseTheTime = vmOrderInfoTem.getIncreasethetime();
                Date date = vmOrderInfoTem.getEnddate();
                long time = date.getTime();
                time = time + 86400000;
                date = new Date(time);

                vmOrderInfo.setEnddate(date);

                if (increaseTheTime == null){
                    increaseTheTime = 0;
                }
                increaseTheTime = increaseTheTime + vmOrderInfo.getIncreasethetime();
                vmOrderInfo.setIncreasethetime(increaseTheTime);*/
                vmOrderInfoMapper.updateIncreaseTheRenewalTimeById(vmOrderInfo);
                //把原来的申请信息删除
                int deleteMessage = vmApplicationInfoMapper.deleteByPrimaryKey(deleteId);
                if (deleteMessage != 0) {
                    errorMessage = "续约成功";
                }

            }else {
                //插入订单信息
                int insertNum = vmOrderInfoMapper.insertSelective(vmOrderInfo);
                //插入成功
                if (insertNum != 0) {
                    //把原来的申请信息删除
                    int deleteMessage = vmApplicationInfoMapper.deleteByPrimaryKey(deleteId);
                    if (deleteMessage != 0) {
                        errorMessage = "申请已同意";
                    }
                }
            }


        }

        return errorMessage;
    }

    /**
     * 管理员拒绝申请操作
     *
     * @param vmApplicationInfoTem
     * @return
     */
    @Override
    public String disApplicationToOrder(VMApplicationInfo vmApplicationInfoTem) {

        VMApplicationInfo vmApplicationInfo = vmApplicationInfoMapper.selectByPrimaryKey(vmApplicationInfoTem.getId());

        VMOrderInfo vmOrderInfo = new VMOrderInfo();

        vmOrderInfo.setApplicant(vmApplicationInfo.getApplicant());
        vmOrderInfo.setApplicantid(vmApplicationInfo.getApplicantid());
        vmOrderInfo.setApplication(vmApplicationInfo.getApplication());
        vmOrderInfo.setCpu(vmApplicationInfo.getCpu());
        vmOrderInfo.setEnddate(vmApplicationInfo.getEnddate());
        vmOrderInfo.setIncreasethetime(vmApplicationInfo.getIncreasethetime());
        vmOrderInfo.setMemory(vmApplicationInfo.getMemory());
        vmOrderInfo.setNumber(vmApplicationInfo.getNumber());
        vmOrderInfo.setRam(vmApplicationInfo.getRam());
        vmOrderInfo.setRemark(vmApplicationInfo.getRemark());
        vmOrderInfo.setSection(vmApplicationInfo.getSection());
        vmOrderInfo.setStartdate(vmApplicationInfo.getStartdate());
        vmOrderInfo.setState("已拒绝");
        vmOrderInfo.setSystemname(vmApplicationInfo.getSystemname());
        vmOrderInfo.setVmname(vmApplicationInfo.getVmname());

        String errorMessage = "failed";

        //把拒绝的信息插入到订单信息中
        Integer insertNum = vmOrderInfoMapper.insertSelective(vmOrderInfo);
        if (insertNum != 0) {
            Integer delNum = vmApplicationInfoMapper.deleteByPrimaryKey(vmApplicationInfo.getId());
            if (delNum != 0) {
                errorMessage = "申请已拒绝";
            }
        }

        return errorMessage;
    }

    @Override
    public String test() {

        Date date = new Date();
        VMOrderInfo vmOrderInfo = new VMOrderInfo();
        vmOrderInfo.setStartdate(date);
        vmOrderInfo.setCpu(123123);
        vmOrderInfoMapper.insertSelective(vmOrderInfo);

        return null;
    }

    /**
     * 导出数据到excel
     *
     * @param response
     * @param userInfo
     * @return
     */
    @Override
    public void exportData(HttpServletResponse response, UserInfo userInfo) throws IOException, WriteException {

        //检查是否是管理员
        Integer adminNum = userInfoMapper.selectIfAdmin(userInfo);
        if (adminNum != 1){
            return;
        }

        //获得输出流，该输出流的输出介质是客户端浏览器
        OutputStream output = response.getOutputStream();
        response.reset();
        response.setHeader("Content-disposition", "attachment;           filename=temp01.xls");
        response.setContentType("application/msexcel");

        //创建可写入的Excel工作薄，且内容将写入到输出流，并通过输出流输出给客户端浏览
        WritableWorkbook wk = Workbook.createWorkbook(output);

        //创建可写入的Excel工作表
        WritableSheet sheet = wk.createSheet("虚拟机申请表", 0);

        //把单元格（column, row）到单元格（column1, row1）进行合并。
        //mergeCells(column, row, column1, row1);

        sheet.mergeCells(0, 0, 34, 2);
        sheet.mergeCells(0, 3, 2, 3);
        sheet.mergeCells(3, 3, 4, 3);
        sheet.mergeCells(5, 3, 7, 3);
        sheet.mergeCells(8, 3, 9, 3);
        sheet.mergeCells(10, 3, 11, 3);
        sheet.mergeCells(12, 3, 13, 3);
        sheet.mergeCells(14, 3, 15, 3);
        sheet.mergeCells(16, 3, 17, 3);
        sheet.mergeCells(18, 3, 19, 3);
        sheet.mergeCells(20, 3, 21, 3);
        sheet.mergeCells(22, 3, 23, 3);
        //sheet.mergeCells(24,3, 24,3);
        sheet.mergeCells(25, 3, 26, 3);
        sheet.mergeCells(27, 3, 28, 3);
        sheet.mergeCells(29, 3, 30, 3);
        //sheet.mergeCells(31,3, 31,3);
        sheet.mergeCells(32, 3, 33, 3);


        //创建WritableFont 字体对象，参数依次表示黑体、字号12、粗体、非斜体、不带下划线、黄色
        WritableFont titleFont = new WritableFont(WritableFont.createFont("黑体"), 20, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
        //创建WritableCellFormat对象，将该对象应用于单元格从而设置单元格的样式
        WritableCellFormat titleFormat = new WritableCellFormat();
        //设置字体格式
        titleFormat.setFont(titleFont);
        //设置文本水平居中对齐
        titleFormat.setAlignment(Alignment.CENTRE);
        //设置文本垂直居中对齐
        titleFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
        //设置背景颜色
        titleFormat.setBackground(Colour.YELLOW);
        //设置自动换行
        titleFormat.setWrap(true);

        //添加Label对象，参数依次表示在第一列，第一行，内容，使用的格式
        Label lab_00 = new Label(0, 0, "虚拟机申请表", titleFormat);

        sheet.addCell(lab_00);

        //将定义好的Label对象添加到工作表上，这样工作表的第一列第一行的内容为‘虚拟机申请表’并应用了titleFormat定义的样式
        NumberFormat nf = new NumberFormat("0");
        WritableCellFormat cloumnTitleFormatNum = new WritableCellFormat(nf);
        cloumnTitleFormatNum.setFont(new WritableFont(WritableFont.createFont("宋体"), 10, WritableFont.NO_BOLD, false));
        cloumnTitleFormatNum.setAlignment(Alignment.CENTRE);

        //定义日期格式
        DateFormat df = new DateFormat("yyyy-MM-dd");
        //创建WritableCellFormat对象
        WritableCellFormat datewcf = new WritableCellFormat(df);
        datewcf.setFont(new WritableFont(WritableFont.createFont("宋体"), 10, WritableFont.NO_BOLD, false));
        datewcf.setAlignment(Alignment.CENTRE);

        //创建cloumnTitleFormat对象
        WritableCellFormat cloumnTitleFormat = new WritableCellFormat();
        cloumnTitleFormat.setFont(new WritableFont(WritableFont.createFont("黑体"), 10, WritableFont.BOLD, false));
        cloumnTitleFormat.setAlignment(Alignment.CENTRE);

        //创建cloumnFormat对象
        WritableCellFormat cloumnFormat = new WritableCellFormat();
        cloumnFormat.setFont(new WritableFont(WritableFont.createFont("宋体"), 10, WritableFont.NO_BOLD, false));
        cloumnFormat.setAlignment(Alignment.CENTRE);


        Label lab_01 = new Label(0, 3, "组件名称（虚拟机的名称）", cloumnTitleFormat);
        Label lab_11 = new Label(3, 3, "用途", cloumnTitleFormat);
        Label lab_21 = new Label(5, 3, "系统", cloumnTitleFormat);
        Label lab_31 = new Label(8, 3, "内存（G）", cloumnTitleFormat);
        Label lab_41 = new Label(10, 3, "硬盘（G）", cloumnTitleFormat);
        Label lab_51 = new Label(12, 3, "CPU（核）", cloumnTitleFormat);
        Label lab_61 = new Label(14, 3, "数量（台）", cloumnTitleFormat);
        Label lab_71 = new Label(16, 3, "云环境", cloumnTitleFormat);
        Label lab_81 = new Label(18, 3, "虚拟机用户名", cloumnTitleFormat);
        Label lab_91 = new Label(20, 3, "密码", cloumnTitleFormat);
        Label lab_101 = new Label(22, 3, "IP地址", cloumnTitleFormat);
        Label lab_111 = new Label(24, 3, "申请人", cloumnTitleFormat);
        Label lab_121 = new Label(25, 3, "申请部门", cloumnTitleFormat);
        Label lab_131 = new Label(27, 3, "开始使用日期", cloumnTitleFormat);
        Label lab_141 = new Label(29, 3, "使用结束日期", cloumnTitleFormat);
        Label lab_151 = new Label(31, 3, "续期时长", cloumnTitleFormat);
        Label lab_161 = new Label(32, 3, "备注", cloumnTitleFormat);


        List<VMOrderInfo> vmOrderInfoList = vmOrderInfoMapper.selectAll();

        int x = 3;

        for (VMOrderInfo vmOrderInfo : vmOrderInfoList) {

           /* String vmname;  虚拟机名称
            String application;  用途
            String systemname;  系统名称
            Integer ram;  内存
            Integer memory;  硬盘空间存储大小(G)
            Integer cpu;  cpu是几核的
            Integer number;  数量(台)
            String applicant;  申请人
            Integer applicantid;  申请人id(当前登录用户的id)
            String section;  申请部门
            Date startdate;  开始时间
            Date enddate;  结束时间
            String remark;  备注*/

            x++;

            //合并单元格
            sheet.mergeCells(0, x, 2, x);
            sheet.mergeCells(3, x, 4, x);
            sheet.mergeCells(5, x, 7, x);
            sheet.mergeCells(8, x, 9, x);
            sheet.mergeCells(10, x, 11, x);
            sheet.mergeCells(12, x, 13, x);
            sheet.mergeCells(14, x, 15, x);
            sheet.mergeCells(16, x, 17, x);
            sheet.mergeCells(18, x, 19, x);
            sheet.mergeCells(20, x, 21, x);
            sheet.mergeCells(22, x, 23, x);
            //sheet.mergeCells(24,x, 24,x);
            sheet.mergeCells(25, x, 26, x);
            sheet.mergeCells(27, x, 28, x);
            sheet.mergeCells(29, x, 30, x);
            //sheet.mergeCells(31,x, 31,x);
            sheet.mergeCells(32, x, 33, x);

            //虚拟机名称
            String vmName = vmOrderInfo.getVmname();
            //用途
            String application = vmOrderInfo.getApplication();
            //系统
            String systemName = vmOrderInfo.getSystemname();
            //内存
            Integer RAM = vmOrderInfo.getRam();
            //硬盘
            Integer memory = vmOrderInfo.getMemory();
            //cpu
            Integer cpu = vmOrderInfo.getCpu();
            //数量
            Integer number = vmOrderInfo.getNumber();
            //云环境
            String environment = vmOrderInfo.getEnvironment();
            //虚拟机用户名
            String username = vmOrderInfo.getUsername();
            //虚拟机密码
            String password = vmOrderInfo.getPassword();
            //IP
            String IP = vmOrderInfo.getIp();
            //申请人
            String applicant = vmOrderInfo.getApplicant();
            //申请部门
            String section = vmOrderInfo.getSection();
            //开始使用时间
            Date startDate = vmOrderInfo.getStartdate();
            //结束使用时间
            Date endDate = vmOrderInfo.getEnddate();
            //续期时长
            Integer increaseTheTime = vmOrderInfo.getIncreasethetime();
            //备注
            String remark = vmOrderInfo.getRemark();


            Label lab_a = new Label(0, x, vmName, cloumnFormat);
            Label lab_b = new Label(3, x, application, cloumnFormat);
            Label lab_c = new Label(5, x, systemName, cloumnFormat);
            Number lab_d = new Number(8, x, RAM, cloumnTitleFormatNum);
            Number lab_e = new Number(10, x, memory, cloumnTitleFormatNum);
            Number lab_f = new Number(12, x, cpu, cloumnTitleFormatNum);
            Number lab_g = new Number(14, x, number, cloumnTitleFormatNum);
            Label lab_h = new Label(16, x, environment, cloumnFormat);
            Label lab_i = new Label(18, x, username, cloumnFormat);
            Label lab_j = new Label(20, x, password, cloumnFormat);
            Label lab_k = new Label(22, x, IP, cloumnFormat);
            Label lab_l = new Label(24, x, applicant, cloumnFormat);
            Label lab_m = new Label(25, x, section, cloumnFormat);
            DateTime lab_n = new DateTime(27, x, startDate, datewcf);
            DateTime lab_o = new DateTime(29, x, endDate, datewcf);
            Number lab_p = null;
            Label lab_q = null;
            if (increaseTheTime != null) {
                lab_p = new Number(31, x, increaseTheTime, cloumnFormat);
            }
            if (remark != null) {
                lab_q = new Label(32, x, remark, cloumnFormat);
            }

            sheet.addCell(lab_a);
            sheet.addCell(lab_b);
            sheet.addCell(lab_c);
            sheet.addCell(lab_d);
            sheet.addCell(lab_e);
            sheet.addCell(lab_f);
            sheet.addCell(lab_g);
            sheet.addCell(lab_h);
            sheet.addCell(lab_i);
            sheet.addCell(lab_j);
            sheet.addCell(lab_k);
            sheet.addCell(lab_l);
            sheet.addCell(lab_m);
            sheet.addCell(lab_n);
            sheet.addCell(lab_o);

            if (increaseTheTime != null) {
                sheet.addCell(lab_p);
            }
            if (remark != null) {
                sheet.addCell(lab_q);
            }


        }

        sheet.addCell(lab_01);
        sheet.addCell(lab_11);
        sheet.addCell(lab_21);
        sheet.addCell(lab_31);
        sheet.addCell(lab_41);
        sheet.addCell(lab_51);
        sheet.addCell(lab_61);
        sheet.addCell(lab_71);
        sheet.addCell(lab_81);
        sheet.addCell(lab_91);
        sheet.addCell(lab_101);
        sheet.addCell(lab_111);
        sheet.addCell(lab_121);
        sheet.addCell(lab_131);
        sheet.addCell(lab_141);
        sheet.addCell(lab_151);
        sheet.addCell(lab_161);

        //将定义的工作表输出到之前指定的介质中（这里是客户端浏览器）
        wk.write();

        //操作完成时，关闭对象，释放占用的内存空间
        wk.close();


    }

    /**
     * 导入数据
     * @param
     * @param file
     * @param localAddress
     * @return
     */
    @Override
    public String importData(MultipartFile file, HttpSession session, String localAddress) throws IOException, BiffException {

        String errorMessage;

        UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");

        //检查是否是管理员
        Integer adminNum = userInfoMapper.selectIfAdmin(userInfo);
        if (adminNum != 1){
            return "您没有导入数据的权限!";
        }

        //file对象名记得和前端name属性值一致
        //System.out.println(file.getOriginalFilename());
        String fileName = file.getOriginalFilename();
        System.out.println(fileName);
        String suffix = fileName.substring(fileName.lastIndexOf('.'));
        String newFileName = new Date().getTime() + suffix;
        String path = localAddress;
        File newFile = new File(path + newFileName);
        try {
            file.transferTo(newFile);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        String xlsPath = path + newFileName;

        //导入已存在的Excel文件，获得只读的工作薄对象
        FileInputStream fis = new FileInputStream(xlsPath);

        Workbook wk = Workbook.getWorkbook(fis);

        //获取第一张Sheet表
        Sheet sheet = wk.getSheet(0);
        //获取总行数
        int rowNum = sheet.getRows();

        //从数据行开始迭代每一行
        for (int i = 4; i < rowNum; i++) {
            VMOrderInfo vmOrderInfo = new VMOrderInfo();
            //getCell(column,row)，表示取得指定列指定行的单元格（Cell）
            //getContents()获取单元格的内容，返回字符串数据。适用于字符型数据的单元格
            //使用实体类封装单元格数据

           /* sheet.mergeCells(0, x, 2, x);
            sheet.mergeCells(3, x, 4, x);
            sheet.mergeCells(5, x, 7, x);
            sheet.mergeCells(8, x, 9, x);
            sheet.mergeCells(10, x, 11, x);
            sheet.mergeCells(12, x, 13, x);
            sheet.mergeCells(14, x, 15, x);
            sheet.mergeCells(16, x, 17, x);
            sheet.mergeCells(18, x, 19, x);
            sheet.mergeCells(20, x, 21, x);
            sheet.mergeCells(22, x, 23, x);
            //sheet.mergeCells(24,x, 24,x);
            sheet.mergeCells(25, x, 26, x);
            sheet.mergeCells(27, x, 28, x);
            sheet.mergeCells(29, x, 30, x);
            //sheet.mergeCells(31,x, 31,x);
            sheet.mergeCells(32, x, 33, x);*/

            if (sheet.getCell(0, i).getContents() == "" & sheet.getCell(27, i).getContents() == ""){
                break;
            }

            //虚拟机名称
            vmOrderInfo.setVmname(sheet.getCell(0, i).getContents());
            //用途
            vmOrderInfo.setApplication(sheet.getCell(3, i).getContents());
            //系统
            vmOrderInfo.setSystemname(sheet.getCell(5, i).getContents());

            //内存
            Cell numCellRam =sheet.getCell(8, i);
            //NumberCell的getValue()方法取得单元格的数值型数据
            vmOrderInfo.setRam(Integer.valueOf(numCellRam.getContents()));

            //硬盘
            Cell numCellMemory =sheet.getCell(10, i);
            //NumberCell的getValue()方法取得单元格的数值型数据
            vmOrderInfo.setMemory(Integer.valueOf(numCellMemory.getContents()));
            //cpu
            Cell numCellCpu = sheet.getCell(12, i);
            //NumberCell的getValue()方法取得单元格的数值型数据
            vmOrderInfo.setCpu(Integer.valueOf(numCellCpu.getContents()));
            //数量
            Cell numCellNumber = sheet.getCell(14, i);
            //NumberCell的getValue()方法取得单元格的数值型数据
            vmOrderInfo.setNumber(Integer.valueOf(numCellNumber.getContents()));
            //云环境
            vmOrderInfo.setEnvironment(sheet.getCell(16, i).getContents());
            //虚拟机用户名
            vmOrderInfo.setUsername(sheet.getCell(18, i).getContents());
            //虚拟机密码
            vmOrderInfo.setPassword(sheet.getCell(20, i).getContents());
            //IP
            vmOrderInfo.setIp(sheet.getCell(22, i).getContents());
            //申请人
            vmOrderInfo.setApplicant(sheet.getCell(24, i).getContents());
            //申请部门
            vmOrderInfo.setSection(sheet.getCell(25, i).getContents());
            //状态
            vmOrderInfo.setState("已同意");
            //开始使用时间
            DateCell dateCellStart = (DateCell) sheet.getCell(27, i);

            Date dateStart = dateCellStart.getDate();
            long dateStartLong = dateStart.getTime();
            dateStartLong = dateStartLong - 28800000;
            Date dateMyStart = new Date(dateStartLong);

            //DateCell的getDate()方法取得单元格的日期型数据
            vmOrderInfo.setStartdate(dateMyStart);

            //结束使用时间
            DateCell dateCellEnd = (DateCell) sheet.getCell(29, i);

            Date dateEnd = dateCellEnd.getDate();
            long dateEndLong = dateEnd.getTime();
            dateEndLong = dateEndLong - 28800000;
            Date dateMyEnd = new Date(dateEndLong);

            //DateCell的getDate()方法取得单元格的日期型数据
            vmOrderInfo.setEnddate(dateMyEnd);
            //续期时长
            vmOrderInfo.setIncreasethetime(31);
            //备注
            vmOrderInfo.setRemark(sheet.getCell(32, i).getContents());

            vmOrderInfoMapper.insertSelective(vmOrderInfo);

        }

        fis.close();
        errorMessage = "导入成功";

        wk.close();

        return errorMessage;
    }

    /**
     * 判断申请性质(点击同意后)
     * @param vmApplicationInfo
     * @return
     */
    @Override
    public String judgeApplication(VMApplicationInfo vmApplicationInfo) {

        String returnMessage;
        if (null == vmApplicationInfo.getIncreasethetime()){
            returnMessage = "/VM/transitionToOrder";
        }else {
            returnMessage = "/VM/increaseTheRenewalTime";
        }

        return returnMessage;
    }

    /**
     * 转换申请续约
     * @param vmOrderInfo
     * @return
     */
    @Override
    public String increaseTheRenewalTime(VMOrderInfo vmOrderInfo) {
        //先查出原有的续约时长
        VMOrderInfo vmOrderInfoTem = vmOrderInfoMapper.selectByPrimaryKey(vmOrderInfo.getId());
        Integer increaseTheTime = vmOrderInfoTem.getIncreasethetime();
        if (increaseTheTime == null){
            increaseTheTime = 0;
        }
        increaseTheTime = increaseTheTime + vmOrderInfo.getIncreasethetime();
        vmOrderInfo.setIncreasethetime(increaseTheTime);
        Integer errorMessage= vmOrderInfoMapper.updateIncreaseTheRenewalTimeById(vmOrderInfo);
        String returnMessage;
        if (errorMessage == 1){
            returnMessage = "已同意申请";
        }else {
            returnMessage = "续约时间更新失败";
        }

        return returnMessage;
    }


}
