package com.ss.smpp;

import com.google.common.util.concurrent.RateLimiter;
import com.ss.common.ClientConstant;
import com.ss.dao.ClientMapper;
import com.ss.main.BootStrap;
import com.ss.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.jsmpp.InvalidResponseException;
import org.jsmpp.PDUException;
import org.jsmpp.bean.*;
import org.jsmpp.extra.NegativeResponseException;
import org.jsmpp.extra.ResponseTimeoutException;
import org.jsmpp.session.BindParameter;
import org.jsmpp.session.SMPPSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author JDsen99
 * @description
 * @createDate 2021/10/12-19:00
 */
public class SMPPClient extends SMPPSession {

    private final static Logger logger = LoggerFactory.getLogger(BootStrap.class);

    private int id;

    private String account;

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
    private String serverName;

    /**
     * 服务器地址
     */
    private String serverAddr;

    /**
     * 发送的数量
     */
    private AtomicInteger sendCount = new AtomicInteger(0);

    private RateLimiter limiter = null;

    private SqlSession sqlSession = MybatisUtils.getSqlSession();

    private ClientMapper clientMapper = sqlSession.getMapper(ClientMapper.class);


    public SMPPClient(int id) {
        this.id = id;
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

    public void doConnect() {
        try {
            super.connectAndBind(serverAddr,port,new BindParameter(BindType.BIND_TX, account, password, "cp", TypeOfNumber.UNKNOWN, NumberingPlanIndicator.UNKNOWN, null));
        } catch (IOException e) {
            logger.warn("通道启动错误... ID : {}",id);
        }
    }
    public void showStatus() {
        logger.info("ID : {} ,sendCount : {} isOpen : {}  status(可发送) : {}  流速 : {}",id,sendCount,isOpen,getSessionState().isTransmittable(),limiter.getRate());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return serverAddr;
    }

    public void setServerName(String serverName) {
        serverName = serverName;
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

    public void setLimiter(double rate) {
        if (limiter == null) {
            this.limiter  = RateLimiter.create(rate);
        }
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerAddr(String serverAddr) {
        this.serverAddr = serverAddr;
    }



}
