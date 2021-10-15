package com.ss.monitor;

import com.ss.common.ClientConstant;
import com.ss.dao.ClientMapper;
import com.ss.pojo.Message;
import com.ss.utils.CommUtil;
import com.ss.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.jsmpp.InvalidResponseException;
import org.jsmpp.PDUException;
import org.jsmpp.bean.*;
import org.jsmpp.extra.NegativeResponseException;
import org.jsmpp.extra.ResponseTimeoutException;
import org.jsmpp.session.SMPPSession;
import org.jsmpp.session.SubmitMultiResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.Callable;

/**
 * @author JDsen99
 * @description
 * @createDate 2021/10/12-18:35
 */
public class BigMessageTask extends AbstractMessageTask implements Runnable{

    Logger logger = LoggerFactory.getLogger(BigMessageTask.class);

    private Address[] addresses;
    private Message message;
    private SMPPSession session;

    public BigMessageTask(Address[] addresses, Message message, SMPPSession session) {
        this.addresses = addresses;
        this.message = message;
        this.session = session;
    }

    @Override
    public void run() {
        try {
            SubmitMultiResult result = session.submitMultiple(ClientConstant.SERVICE_TYPE,
                    TypeOfNumber.NATIONAL,
                    NumberingPlanIndicator.UNKNOWN,
                    message.getSendId().trim(),
                    addresses,
                    new ESMClass(),
                    (byte) 0,
                    (byte) 1,
                    ClientConstant.TIME_FORMATTER.format(new Date()),
                    null,
                    new RegisteredDelivery(SMSCDeliveryReceipt.SUCCESS_FAILURE),
                    ReplaceIfPresentFlag.REPLACE,
                    CommUtil.getSmppCharsetInfo(message.getContent()), (byte) 0,
                    message.getContent().getBytes());
            UnsuccessDelivery[] unSuccessDeliveries = result.getUnsuccessDeliveries();
            String messageId = result.getMessageId();
            StringBuffer sb  = new StringBuffer();

            if (result.getUnsuccessDeliveries() != null && result.getUnsuccessDeliveries().length > 0) {
                for (UnsuccessDelivery unsuccessDelivery : unSuccessDeliveries) {
                    Address address = unsuccessDelivery.getDestinationAddress();
                    sb.append(address.getAddress()).append(",");
                }
//                message.setText(sb.toString());
            }
            logger.info("大短信发送成功数量 : {} 未发送号码 : {} , messageId : {}",unSuccessDeliveries.length,sb,messageId);
//            String messageId = result.getMessageId();
//            insertMessage(message,messageId);
        } catch (PDUException | ResponseTimeoutException | InvalidResponseException | NegativeResponseException | IOException e) {
            logger.error("大短信发送错误 Id :{} gateWay ID : {}  {}",message.getId(),message.getTaskId(),e.getMessage());
        }
    }
}

