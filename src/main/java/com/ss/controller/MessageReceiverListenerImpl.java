/*
 *    Copyright (c) 2021 JDsen
 *    SMPP4J is licensed under Mulan PSL v2.
 *    You can use this software according to the terms and conditions of the Mulan PSL v2.
 *    You may obtain a copy of Mulan PSL v2 at:
 *             http://license.coscl.org.cn/MulanPSL2
 *    THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 *    See the Mulan PSL v2 for more details.
 */
package com.ss.controller;

import com.ss.dao.ClientMapper;
import com.ss.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.jsmpp.bean.*;
import org.jsmpp.extra.ProcessRequestException;
import org.jsmpp.session.DataSmResult;
import org.jsmpp.session.MessageReceiverListener;
import org.jsmpp.session.Session;
import org.jsmpp.util.InvalidDeliveryReceiptException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author JDsen99
 * @description
 * @createDate 2021/10/12-17:12
 */
public class MessageReceiverListenerImpl implements MessageReceiverListener {

    private SqlSession sqlSession;

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageReceiverListenerImpl.class);


    public void onAcceptDeliverSm(DeliverSm deliverSm) throws ProcessRequestException {

        if (MessageType.SMSC_DEL_RECEIPT.containedIn(deliverSm.getEsmClass())) {

            try {
                DeliveryReceipt delReceipt = deliverSm.getShortMessageAsDeliveryReceipt();
                updateSendInfo(delReceipt);
            } catch (InvalidDeliveryReceiptException e) {
                LOGGER.error("状态报告 接受错误。。。 {}", e.getMessage());
            }
        }

//        try {
//            LOGGER.info("Receiving delivery receipt for message '{}' {}",
//                    deliverSm.getId(), deliverSm.getShortMessageAsDeliveryReceipt());
//        } catch (InvalidDeliveryReceiptException e) {
//            e.printStackTrace();
//        }
    }


    private void updateSendInfo(DeliveryReceipt delReceipt) {
        try {
            if (delReceipt == null) {
                LOGGER.error("状态报告 接受错误。。。 没有delReceipt对象");
            }
            sqlSession = MybatisUtils.getSqlSession();
            ClientMapper mapper = sqlSession.getMapper(ClientMapper.class);
            if (delReceipt.getFinalStatus() == null) {
                mapper.updateSendInfo("NULL",delReceipt.getError(),delReceipt.getId());
            }else {
                mapper.updateSendInfo(delReceipt.getFinalStatus().toString(),delReceipt.getError(),delReceipt.getId());
            }

            sqlSession.close();
        } catch (Exception e) {
            LOGGER.error("状态报告 错误 。。。 messageId {}",delReceipt.getId());
        }
    }

    public void onAcceptAlertNotification(AlertNotification alertNotification) {
        LOGGER.info("AlertNotification not implemented");
    }

    public DataSmResult onAcceptDataSm(DataSm dataSm, Session source)
            throws ProcessRequestException {
//        LOGGER.info("DataSm not implemented");
//        throw new ProcessRequestException(DATA_SM_NOT_IMPLEMENTED, SMPPConstant.STAT_ESME_RINVCMDID);
        LOGGER.info("onAcceptDataSm....");
        return null;
    }
}