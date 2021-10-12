package com.ss.monitor;

import com.ss.common.ClientConstant;
import org.jsmpp.bean.*;
import org.jsmpp.session.SMPPSession;

import java.util.Date;
import java.util.concurrent.Callable;

/**
 * @author JDsen99
 * @description
 * @createDate 2021/10/12-18:35
 */
public class BigMessageTask implements Callable<SubmitMultiResult>{
        private Address[] addresses;
        private String sendId;
        private String content;
        private SMPPSession session;

        public BigMessageTask(Address[] addresses, String sendId, String content, SMPPSession session) {
            this.addresses = addresses;
            this.sendId = sendId;
            this.content = content;
            this.session = session;
        }

        @Override
        public SubmitMultiResult call() throws Exception {
            SubmitMultiResult result = session.submitMultiple(ClientConstant.SERVICE_TYPE,
                    TypeOfNumber.NATIONAL,
                    NumberingPlanIndicator.UNKNOWN,
                    sendId,
                    addresses,
                    new ESMClass(),
                    (byte) 0,
                    (byte) 1,
                    ClientConstant.TIME_FORMATTER.format(new Date()),
                    null,
                    new RegisteredDelivery(SMSCDeliveryReceipt.FAILURE),
                    ReplaceIfPresentFlag.REPLACE,
                    new GeneralDataCoding(Alphabet.ALPHA_DEFAULT, MessageClass.CLASS1, false), (byte) 0,
                    content.getBytes());
            return result;
        }
}

