package com.aaa.enumpakage;

import com.aaa.entity.Message;
import com.aaa.entity.User;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @Description: TODO
 * @author: scott
 * @date: 2024年05月23日 10:36
 */
public enum MessageEnum {
    // 缴费通知
    PAY_NOTIFICATION("PAY_NOTIFICATION", "缴费通知"),
    // 预约通知
    BOOKING_NOTIFICATION("BOOKING_NOTIFICATION", "预约通知"),
    // 消息通知
    MESSAGE_NOTIFICATION("MESSAGE_NOTIFICATION", "消息通知");

    private String messageType;
    private String messageTypeName;

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMessageTypeName() {
        return messageTypeName;
    }

    public void setMessageTypeName(String messageTypeName) {
        this.messageTypeName = messageTypeName;
    }

    MessageEnum(String messageType, String messageTypeName) {
        this.messageType = messageType;
        this.messageTypeName = messageTypeName;
    }

    public static List toList() {
        //Lists.newArrayList()其实和new ArrayList()几乎一模
        List list = new ArrayList();
        //  一样, 唯一它帮你做的(其实是javac帮你做的), 就是自动推导(不是"倒")尖括号里的数据类型.

        for (MessageEnum messageEnum : MessageEnum.values()) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", messageEnum.getMessageTypeName());
            map.put("code", messageEnum.getMessageType());
            list.add(map);
        }
        return list;
    }

//    public static Message setMessageByMessageType(HttpServletRequest request, String messageType) {
//        User user = (User) request.getSession().getAttribute("user");
//        Message message = new Message();
//        message.setReceiverId(user.getUserid());
//        for (MessageEnum messageEnum : MessageEnum.values()) {
//            if (messageEnum.getMessageType().equals(messageType)) {
//                message.setMessageType(messageEnum.getMessageType());
////                message.setMessageTitle(messageEnum.getMessageTitle());
////                message.setMessageContent(messageEnum.getMessageContent());
////                message.setMessageTypeName(messageEnum.getMessageTypeName());
//                break;
//            }
//        }
//        return message;
//    }

    public static String getMessageTypeName(String key) {
        for (MessageEnum messageEnum : values()) {
            if (messageEnum.getMessageType().equals(key)) {
                return messageEnum.getMessageTypeName();
            }
        }
        return null;
    }
}
