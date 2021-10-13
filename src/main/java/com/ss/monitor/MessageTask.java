package com.ss.monitor;

import com.ss.common.ClientConstant;
import com.ss.dao.ClientMapper;
import com.ss.pojo.Message;
import com.ss.smpp.SMPPClient;
import com.ss.utils.CommUtil;
import com.ss.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.jsmpp.InvalidResponseException;
import org.jsmpp.PDUException;
import org.jsmpp.bean.*;
import org.jsmpp.extra.NegativeResponseException;
import org.jsmpp.extra.ResponseTimeoutException;
import org.jsmpp.session.SMPPSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.Callable;

/**
 * @author JDsen99
 * @description
 * @createDate 2021/10/12-18:27
 */
public class MessageTask implements Runnable {

    private SqlSession sqlSession;

    Logger logger = LoggerFactory.getLogger(MessageTask.class);
    private Message message;
    private SMPPClient client;

    public MessageTask(Message message, SMPPClient client) {
        this.message = message;
        this.client = client;
    }


    @Override
    public void run() {
        try {
            String messageId = client.submitShortMessage(message.getSendId().trim(),message.getPhone(),(byte) 1,CommUtil.getSmppCharsetInfo(message.getContent()),message.getContent());
            logger.info("messageId : {}",messageId);
            insertMessage(message,messageId);
        } catch (Exception e) {
            logger.error("短信发送错误 Id :{} gateWay ID : {}  {}",message.getId(),message.getTaskId(),e.getMessage());
        }
    }

    /**
     * 将数据插入数据库 taskInfo
     *
     * @param message message
     * @param messageId integer
     */
    private void insertMessage(Message message, String messageId) {
        sqlSession = MybatisUtils.getSqlSession();
        ClientMapper mapper = sqlSession.getMapper(ClientMapper.class);
        message.setMessageId(messageId);
        mapper.insertMessage(message);
        sqlSession.close();
    }
}
