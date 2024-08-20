package com.aaa.controller;

import com.aaa.entity.*;
import com.aaa.enumpakage.MessageEnum;
import com.aaa.mapper.*;
import com.aaa.service.CTakeService;
import com.aaa.service.CreportService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("cao")
public class CreportController {
    @Autowired
    private CTakeMapper cTakeMapper;
    @Autowired
    private CreportService creportService;
    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private DepartmentMapper departmentMapper;
    @Autowired
    private DoctorMapper doctorMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CTakeService cTakeService;

    //查询所有数据
    @RequestMapping("index")
    public Object toreport(ReportVo reportVo, Model model, String params, Integer cc, HttpServletRequest request) {
        reportVo.setCc(cc);
        HttpSession session = request.getSession();
        //将数据存储到session中
        session.setAttribute("ban", cc);
        creportService.upddang();
        String reportName = params;
        reportVo.setReportName(reportName);
        List<ReportVo> sel = creportService.sel(reportVo);
        Integer roleId = (Integer) request.getSession().getAttribute("roleId");
        model.addAttribute("report", sel);
        if (roleId != 5) {
            return "cao/report";
        }
        return "cao/PatientRegistration";
    }

    @RequestMapping("listReportPage")
    @ResponseBody
    public Object listReportPage(ReportVo reportVo, Integer page, Integer limit) {
        PageHelper.startPage(page, limit);
        List<ReportVo> reportVoList = creportService.sel(reportVo);
        PageInfo pageInfo = new PageInfo(reportVoList);
        Map<String, Object> tableData = new HashMap<String, Object>();
        //这是layui要求返回的json数据格式，如果后台没有加上这句话的话需要在前台页面手动设置
        tableData.put("code", 0);
        tableData.put("msg", "");
        //将全部数据的条数作为count传给前台（一共多少条）
        tableData.put("count", pageInfo.getTotal());
        //将分页后的数据返回（每页要显示的数据）
        tableData.put("data", pageInfo.getList());
        return tableData;
    }

    //查询所有的科室
    @RequestMapping("seldep")
    @ResponseBody
    public Object seldep() {
        List<CDepartments> seldep = creportService.seldep();
        return seldep;
    }

    //查询所有的挂号类型
    @RequestMapping("/selreg")
    @ResponseBody
    public Object selreg() {
        List<CRegisteredtype> selreg = creportService.selreg();
        return selreg;
    }

    //查询医生信息
    @RequestMapping("seldoctor")
    @ResponseBody
    public Object seldoctor(CDoctor cDoctor) {
        Calendar calendar = Calendar.getInstance();
        Integer data = calendar.get(Calendar.DAY_OF_WEEK) - 1;//获取当前是星期几
        if (data == 1) {
            List<ReportVo> one = creportService.one(cDoctor);
            return one;
        } else if (data == 2) {
            List<ReportVo> two = creportService.two(cDoctor);
            return two;
        } else if (data == 3) {
            List<ReportVo> three = creportService.three(cDoctor);
            return three;
        } else if (data == 4) {
            List<ReportVo> four = creportService.four(cDoctor);
            return four;
        } else if (data == 5) {
            System.out.println(data);
            List<ReportVo> five = creportService.five(cDoctor);
            return five;
        } else if (data == 6) {
            List<ReportVo> six = creportService.six(cDoctor);
            return six;
        } else {
            List<ReportVo> seven = creportService.seven(cDoctor);
            return seven;
        }
    }

    //根据挂号类型查找该类型的价格
    @RequestMapping("seltymo")
    @ResponseBody
    public Object seltymo(CRegisteredtype cRegisteredtype) {
        Integer seltymo = creportService.seltymo(cRegisteredtype);
        return seltymo;
    }

    //添加新的挂号
    @RequestMapping("addre")
    @ResponseBody
    public Object addre(HttpServletRequest request, CReport cReport) {
        Date currentDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = sdf.format(currentDate);

        User user = (User) request.getSession().getAttribute("user");
        Integer addre = creportService.addre(cReport);
        Departments departments = departmentMapper.getDepartmentsById(cReport.getDepartment());


        DoctorVo doctorVo = new DoctorVo();
        doctorVo.setDoctorId((long) cReport.getDoctor());

        Doctor doctor = doctorMapper.getDoctorByParam(doctorVo);


        UserVo userVo = new UserVo();
        userVo.setRealName(doctor.getDoctorName());
        User doctorUser = userMapper.getUserByParam(userVo);
        Message payMessage = new Message();
        payMessage.setMessageType(MessageEnum.PAY_NOTIFICATION.getMessageType());
        payMessage.setReceiverId(user.getUserid());
        payMessage.setMessageTitle("缴费提醒");
        payMessage.setMessageContent(cReport.getReportName() + "您好！您预约的" + departments.getDepartment() + cReport.getTime() + cReport.getTimePeriod() + "的挂号已预约成功，请尽快进行缴费，超过20分钟未缴费将自动取消！");
        messageMapper.insertMessage(payMessage);
        Message bookingMessage = new Message();
        bookingMessage.setMessageType(MessageEnum.BOOKING_NOTIFICATION.getMessageType());
        bookingMessage.setReceiverId(user.getUserid());
        bookingMessage.setMessageTitle("预约提醒");
        bookingMessage.setMessageContent(cReport.getReportName() + "您好！您在" + formattedDate + "时分预约的" + cReport.getTime() + cReport.getTimePeriod() + departments.getDepartment() + doctor.getDoctorName() + "医生，已预约成功！");
        messageMapper.insertMessage(bookingMessage);
        Message doctorMessage = new Message();
        doctorMessage.setMessageType(MessageEnum.MESSAGE_NOTIFICATION.getMessageType());
        doctorMessage.setReceiverId(doctorUser.getUserid());
        doctorMessage.setMessageTitle("消息提醒");
        doctorMessage.setMessageContent("患者名：" + cReport.getReportName() + "，在" + formattedDate + "预约了" + cReport.getTime() + cReport.getTimePeriod() + "您的挂号。");
        messageMapper.insertMessage(doctorMessage);
        return addre;

    }

    //根据id删除某个患者信息
    @RequestMapping("delre")
    @ResponseBody
    public Object delre(Integer id) {
        int delre = creportService.delre(id);
        if (delre == 1) {
            return "删除成功";
        } else {
            return "删除失败";
        }

    }

    //根据id删除某个患者信息
    @RequestMapping("selById")
    @ResponseBody
    public Object selById(Integer reportId) {
        List<ReportVo> reportVos = creportService.selById(reportId);
        return reportVos;
    }

    @RequestMapping("selDocReport")
    @ResponseBody
    public  Object selDocReport(PaiBanVo paiBanVo){
        Integer data = creportService.selDocReport(paiBanVo);
        return data;
    }

    //判断就诊用户电话身份证号唯一
    @RequestMapping("phone")
    @ResponseBody
    public Object phone(CReport cReport) {
        int carid = creportService.carid(cReport);
        int phone = creportService.phone(cReport);
        if (carid >= 1) {
            return 2;
        } else if (phone >= 1) {
            return 1;
        } else {
            return 3;
        }
    }

    //转入住院部
    @RequestMapping("zhuanyuan")
    @ResponseBody
    public Object zhuanyuan(Integer id, CReport cReport, String zhuan) {
        cReport.setReportId(id);
        cReport.setZhuan(zhuan);
        Integer zhuanyuan = creportService.zhuanyuan(cReport);
        return zhuanyuan;
    }

    //我的就诊
    @RequestMapping("myVisit")
    public Object myVisit() {

        return "cao/myVisit";
    }

    @RequestMapping("selhuan")
    @ResponseBody
    public Object selhuan(HttpServletRequest request, ReportVo reportVo, Integer page, Integer limit) {
        User user = (User) request.getSession().getAttribute("user");
        UserVo userVo = new UserVo();
        userVo.setUserId((long) user.getUserid());
        User doctorUser = userMapper.getUserByParam(userVo);
        PageHelper.startPage(page, limit);
        DoctorVo doctorVo = new DoctorVo();
        doctorVo.setDoctorName(doctorUser.getRealname());
        Doctor doctor = doctorMapper.getDoctorByParam(doctorVo);
        reportVo.setDdoctorid(doctor.getDoctorId());
        List<ReportVo> reportVoList = cTakeMapper.queryHuan(reportVo);
        PageInfo pageInfo = new PageInfo(reportVoList);
        Map<String, Object> tableData = new HashMap<String, Object>();
        //这是layui要求返回的json数据格式，如果后台没有加上这句话的话需要在前台页面手动设置
        tableData.put("code", 0);
        tableData.put("msg", "");
        //将全部数据的条数作为count传给前台（一共多少条）
        tableData.put("count", pageInfo.getTotal());
        //将分页后的数据返回（每页要显示的数据）
        tableData.put("data", pageInfo.getList());
        return tableData;
    }

    @RequestMapping("updateZhuanById")
    @ResponseBody
    public Object updateZhuanById(ReportVo reportVo){
        int resultStatus = cTakeMapper.updateZhuanById(reportVo);
        return resultStatus;
    }
}
