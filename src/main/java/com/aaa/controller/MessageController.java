package com.aaa.controller;

import com.aaa.entity.Message;
import com.aaa.entity.MessageVo;
import com.aaa.entity.ReportVo;
import com.aaa.entity.User;
import com.aaa.enumpakage.MessageEnum;
import com.aaa.mapper.MessageMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("history")
public class MessageController {
    @Autowired
    private MessageMapper messageMapper;


    @RequestMapping("prescription")
    public Object prescriptionList() {
        return "history/prescription";
    }

    @RequestMapping("message")
    public Object message() {
        return "history/message";
    }

    @RequestMapping("listMessagePage")
    @ResponseBody
    public Object listMessagePage(HttpServletRequest request, MessageVo messageVo, Integer page, Integer limit) {
        User user = (User) request.getSession().getAttribute("user");
        PageHelper.startPage(page, limit);
        messageVo.setReceiverId(user.getUserid());
        List<Message> messageList = messageMapper.listMessage(messageVo);
        messageList.forEach(message -> {
            message.setMessageTypeName(MessageEnum.getMessageTypeName(message.getMessageType()));
        });
        PageInfo pageInfo = new PageInfo(messageList);
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

    @RequestMapping("updateIsRead")
    @ResponseBody
    public Object updateIsRead(MessageVo messageVo) {
        int resultStatus = messageMapper.updateIsRead(messageVo.getMessageId());
        return resultStatus;
    }
}
