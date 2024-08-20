package com.aaa.controller;

import com.aaa.entity.*;
import com.aaa.mapper.PaibanMapper;
import com.aaa.mapper.PaibanTimePeriodMapper;
import com.aaa.service.PaibanService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("paiban")
public class PaibanController {
    @Autowired
    private PaibanService paibanService;
    @Autowired
    private PaibanTimePeriodMapper paibanTimePeriodMapper;
    @Autowired
    private PaibanMapper paibanMapper;

    @RequestMapping("findAllPaiban")
    @ResponseBody
    public Object findAllPaiban(Paiban paiban, Integer page, Integer limit) {
        PageHelper.startPage(page, limit);
        List<Paiban> allPaiban = paibanService.findAllPaiban(paiban);
        PageInfo pageInfo = new PageInfo(allPaiban);
        Map<String, Object> tableData = new HashMap<String, Object>();
        //这是layui要求返回的json数据格式
        tableData.put("code", 0);
        tableData.put("msg", "");
        //将全部数据的条数作为count传给前台（一共多少条）
        tableData.put("count", pageInfo.getTotal());
        //将分页后的数据返回（每页要显示的数据）
        tableData.put("data", pageInfo.getList());
        return tableData;
    }

    @RequestMapping("editPaiban")
    @ResponseBody
    public Object editPaiban(Paiban paiban) {
        int count = paibanService.count(paiban.getDoctorId());
        System.out.println(count);
        if (count == 1) {
            paibanService.editPaiban(paiban);

        } else if (count == 0) {

            paiban.setDoctorId(paiban.getDoctorId());
            paibanService.insertPaiban(paiban);
            paibanService.editPaiban(paiban);
        }
        return "修改成功";
    }

    @RequestMapping("findAllBan")
    @ResponseBody
    public Object findAllBan() {
        List<Ban> allBan = paibanService.findAllBan();
        return allBan;
    }

    @RequestMapping("findAllPaiBanTimePeriod")
    @ResponseBody
    public Object findAllPaiBanTimePeriod() {
        List<PaibanTimePeriod> paiBanTimePeriodList = paibanTimePeriodMapper.findAllPaibanTimePeriod();
        return paiBanTimePeriodList;
    }

    @RequestMapping("getTimePeriodByDoctorId")
    @ResponseBody
    public Object getTimePeriodByDoctorId(PaiBanVo paiBanVo) {
        // 定义日期时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // 将字符串解析为 LocalDate 对象
        LocalDate date = LocalDate.parse(paiBanVo.getTime(), formatter);
        // 获取对应的星期几
        DayOfWeek dayOfWeek = date.getDayOfWeek();

        // 获取星期几的索引值（从 1 开始）
        int dayIndex = dayOfWeek.getValue();
        String week = "";

        switch (dayIndex) {
            case 1:
                week = "one";
                break;
            case 2:
                week = "two";
                break;
            case 3:
                week = "three";
                break;
            case 4:
                week = "four";
                break;
            case 5:
                week = "five";
                break;
            case 6:
                week = "six";
                break;
            case 7:
                week = "seven";
                break;
            default:
                break;
        }
        paiBanVo.setType(week);
        Paiban paiban = paibanMapper.getTimePeriodByDoctorId(paiBanVo);
        String timePeriod = "";
        if (paiban != null) {
            switch (week) {
                case "one":
                    timePeriod = paiban.getOneTimePeriod();
                    break;
                case "two":
                    timePeriod = paiban.getTwoTimePeriod();
                    break;
                case "three":
                    timePeriod = paiban.getThreeTimePeriod();
                    break;
                case "four":
                    timePeriod = paiban.getFourTimePeriod();
                    break;
                case "five":
                    timePeriod = paiban.getFiveTimePeriod();
                    break;
                case "six":
                    timePeriod = paiban.getSixTimePeriod();
                    break;
                case "seven":
                    timePeriod = paiban.getSevenTimePeriod();
                    break;
                default:
                    break;
            }
        }
        Map<String, String> map = new HashMap<>();
        map.put("timePeriod", timePeriod);
        return map;
    }


}
