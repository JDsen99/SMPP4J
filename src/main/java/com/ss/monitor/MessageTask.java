package com.ss.monitor;

import com.ss.common.ClientConstant;
import org.jsmpp.bean.*;
import org.jsmpp.session.SMPPSession;

import java.util.Date;
import java.util.concurrent.Callable;

/**
 * @author JDsen99
 * @description
 * @createDate 2021/10/12-18:27
 */
public class MessageTask implements Callable<String> {

    private String phone;
    private String sendId;
    private String content;
    private SMPPSession session;

    public MessageTask(String phone, String sendId, String content, SMPPSession session) {
        this.phone = phone;
        this.sendId = sendId;
        this.content = content;
        this.session = session;
    }

    @Override
    public String call() throws Exception {
        String messageId = session.submitShortMessage(ClientConstant.SERVICE_TYPE,
                TypeOfNumber.NATIONAL, NumberingPlanIndicator.UNKNOWN, sendId,
                TypeOfNumber.NATIONAL, NumberingPlanIndicator.UNKNOWN, phone,
                new ESMClass(), (byte)0, (byte)1,  ClientConstant.TIME_FORMATTER.format(new Date()), null,
                new RegisteredDelivery(SMSCDeliveryReceipt.SUCCESS_FAILURE), (byte)0, new GeneralDataCoding(Alphabet.ALPHA_DEFAULT, MessageClass.CLASS1, false), (byte)0,
                content.getBytes());
        return messageId;
    }
}
