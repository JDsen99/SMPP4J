package com.ss.controller;

import com.ss.dao.ClientMapper;
import com.ss.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.jsmpp.SMPPConstant;
import org.jsmpp.bean.*;
import org.jsmpp.extra.ProcessRequestException;
import org.jsmpp.session.DataSmResult;
import org.jsmpp.session.MessageReceiverListener;
import org.jsmpp.session.Session;
import org.jsmpp.util.DeliveryReceiptState;
import org.jsmpp.util.InvalidDeliveryReceiptException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

/**
 * @author JDsen99
 * @description
 * @createDate 2021/10/12-17:12
 */
public class MessageReceiverListenerImpl implements MessageReceiverListener {

    private SqlSession sqlSession;

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageReceiverListenerImpl.class);

    private static final String DATA_SM_NOT_IMPLEMENTED = "data_sm not implemented";



    public void onAcceptDeliverSm(DeliverSm deliverSm) throws ProcessRequestException {

        if (MessageType.SMSC_DEL_RECEIPT.containedIn(deliverSm.getEsmClass())) {

            try {
                DeliveryReceipt delReceipt = deliverSm.getShortMessageAsDeliveryReceipt();
                DeliveryReceiptState finalStatus = delReceipt.getFinalStatus();
                String messageId = delReceipt.getId();
                LOGGER.info("messageId : {} ",messageId);
                LOGGER.info("finalStatus : {} ",finalStatus);
                LOGGER.info("getDelivered : {} ",delReceipt.getDelivered());
                LOGGER.info("getError : {} ",delReceipt.getError());
                LOGGER.info("getSubmitted : {} ",delReceipt.getSubmitted());

                if (finalStatus == DeliveryReceiptState.DELIVRD) {
                    updateSendInfo(messageId,finalStatus.value());
                }else {
                    int value = finalStatus.value();
                    System.out.println(value);
                }
//                long id = Long.parseLong(delReceipt.getId()) & 0xffffffff;
//                String messageId = Long.toString(id, 16).toUpperCase();

                LOGGER.info("Receiving delivery receipt for message '{}' from {} to {}: {}",
                        messageId, deliverSm.getSourceAddr(), deliverSm.getDestAddress(), delReceipt);
            } catch (InvalidDeliveryReceiptException e) {
                LOGGER.error("Failed getting delivery receipt {}", e.getMessage());
            }
        }
        LOGGER.info("Receiving delivery receipt for message '{}' from {} to {}",
                deliverSm.getId(), deliverSm.getSourceAddr(), deliverSm.getDestAddress());
    }

    private void updateSendInfo(String messageId, int value) {
        sqlSession = MybatisUtils.getSqlSession();
        ClientMapper mapper = sqlSession.getMapper(ClientMapper.class);
        mapper.updateMessage(messageId,String.valueOf(value));
        sqlSession.close();
    }

    public void onAcceptAlertNotification(AlertNotification alertNotification) {
        LOGGER.info("AlertNotification not implemented");
    }

    public DataSmResult onAcceptDataSm(DataSm dataSm, Session source)
            throws ProcessRequestException {
//        LOGGER.info("DataSm not implemented");
//        throw new ProcessRequestException(DATA_SM_NOT_IMPLEMENTED, SMPPConstant.STAT_ESME_RINVCMDID);
        return null;
    }
}