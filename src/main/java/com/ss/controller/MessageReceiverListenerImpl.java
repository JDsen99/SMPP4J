package com.ss.controller;

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

/**
 * @author JDsen99
 * @description
 * @createDate 2021/10/12-17:12
 */
public class MessageReceiverListenerImpl implements MessageReceiverListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageReceiverListenerImpl.class);

    private static final String DATA_SM_NOT_IMPLEMENTED = "data_sm not implemented";



    public void onAcceptDeliverSm(DeliverSm deliverSm) throws ProcessRequestException {

        if (MessageType.SMSC_DEL_RECEIPT.containedIn(deliverSm.getEsmClass())) {

            try {
                DeliveryReceipt delReceipt = deliverSm.getShortMessageAsDeliveryReceipt();
                DeliveryReceiptState finalStatus = delReceipt.getFinalStatus();
                String messageId = delReceipt.getId();
                if (finalStatus == DeliveryReceiptState.DELIVRD) {

                }else {
                    int value = finalStatus.value();
                }
//                long id = Long.parseLong(delReceipt.getId()) & 0xffffffff;
//                String messageId = Long.toString(id, 16).toUpperCase();

                LOGGER.info("Receiving delivery receipt for message '{}' from {} to {}: {}",
                        messageId, deliverSm.getSourceAddr(), deliverSm.getDestAddress(), delReceipt);
            } catch (InvalidDeliveryReceiptException e) {
                LOGGER.error("Failed getting delivery receipt {}", e.getMessage());
            }
        }
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