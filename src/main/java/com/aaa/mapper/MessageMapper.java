package com.aaa.mapper;


import com.aaa.entity.Message;
import com.aaa.entity.MessageVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MessageMapper {

    List<Message> listMessage(MessageVo messageVo);

    int updateIsRead(long messageId);

    Message selectMessageById(long id);

    void insertMessage(Message message);

    void updateMessage(Message message);

    void deleteMessage(long id);
}
