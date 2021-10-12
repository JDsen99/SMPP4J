package com.ss.smpp;

import com.google.common.util.concurrent.RateLimiter;
import com.ss.common.ClientConstant;
import com.ss.dao.ClientMapper;
import com.ss.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.jsmpp.InvalidResponseException;
import org.jsmpp.PDUException;
import org.jsmpp.bean.*;
import org.jsmpp.extra.NegativeResponseException;
import org.jsmpp.extra.ResponseTimeoutException;
import org.jsmpp.session.SMPPSession;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author JDsen99
 * @description
 * @createDate 2021/10/12-19:00
 */
public class SMPPClient extends SMPPSession {

    private int id;

    private String username;

    private String password;


    /**
     * 是否启用通道
     */
    private boolean isOpen = false;

    /**
     * 服务器端口
     */
    private int port;

    /**
     * 服务器名称
     */
    private String ServerName;

    /**
     * 服务器地址
     */
    private String ServerAddr;

    /**
     * 发送的数量
     */
    private AtomicInteger sendCount = new AtomicInteger(0);

    private RateLimiter limiter = RateLimiter.create(40);

    private SqlSession sqlSession = MybatisUtils.getSqlSession();

    private ClientMapper clientMapper = sqlSession.getMapper(ClientMapper.class);


    public SMPPClient(int id, String username, String password) {

    }

    public String submitShortMessage(String sendId,String phone, byte priorityFlag,DataCoding dataCoding, String content) {
        String result = null;
        try {
            result = super.submitShortMessage(ClientConstant.SERVICE_TYPE,
                    TypeOfNumber.NATIONAL, NumberingPlanIndicator.UNKNOWN, sendId,
                    TypeOfNumber.NATIONAL, NumberingPlanIndicator.UNKNOWN, phone,
                    new ESMClass(), (byte) 0, priorityFlag, ClientConstant.TIME_FORMATTER.format(new Date()), null,
                    new RegisteredDelivery(SMSCDeliveryReceipt.SUCCESS_FAILURE), (byte) 0, dataCoding, (byte) 0,
                    content.getBytes());
        } catch (PDUException | ResponseTimeoutException | InvalidResponseException | NegativeResponseException | IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public SubmitMultiResult submitMultiple(String sendId,Address[] addresses ,DataCoding dataCoding ,String content){
        SubmitMultiResult submitMultiResult = null;
        try {
            submitMultiResult = submitMultiple(ClientConstant.SERVICE_TYPE, TypeOfNumber.NATIONAL, NumberingPlanIndicator.UNKNOWN, sendId,
                    addresses, new ESMClass(), (byte) 0, (byte) 1, ClientConstant.TIME_FORMATTER.format(new Date()), null,
                    new RegisteredDelivery(SMSCDeliveryReceipt.FAILURE), ReplaceIfPresentFlag.REPLACE,
                    dataCoding, (byte) 0,
                    content.getBytes());
        } catch (PDUException | ResponseTimeoutException | InvalidResponseException | NegativeResponseException | IOException e) {
            e.printStackTrace();
        }
        return submitMultiResult;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getServerName() {
        return ServerName;
    }

    public void setServerName(String serverName) {
        ServerName = serverName;
    }

    public AtomicInteger getSendCount() {
        return sendCount;
    }

    public void setSendCount(AtomicInteger sendCount) {
        this.sendCount = sendCount;
    }

    public RateLimiter getLimiter() {
        return limiter;
    }

    public void setLimiter(RateLimiter limiter) {
        this.limiter = limiter;
    }
}
