/*
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package org.jsmpp.session;

import org.jsmpp.SMPPConstant;
import org.jsmpp.bean.Command;
import org.jsmpp.util.HexUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author uudashr
 *
 */
class PDUProcessOutboundTask implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(PDUProcessOutboundTask.class);

    private final Command pduHeader;
    private final byte[] pdu;
    private final SMPPOutboundSessionContext sessionContext;
    private final OutboundResponseHandler responseHandler;
    private final ActivityNotifier activityNotifier;
    private final Runnable onIOExceptionTask;

    PDUProcessOutboundTask(Command pduHeader, byte[] pdu,
                                  SMPPOutboundSessionContext sessionContext, OutboundResponseHandler responseHandler,
                                  ActivityNotifier activityNotifier, Runnable onIOExceptionTask) {
        this.pduHeader = pduHeader;
        this.pdu = pdu;
        this.sessionContext = sessionContext;
        this.responseHandler = responseHandler;
        this.activityNotifier = activityNotifier;
        this.onIOExceptionTask = onIOExceptionTask;
    }

    @Override
    public void run() {
        try {
            if (logger.isDebugEnabled()) {
                logger.debug("Received PDU {}", HexUtil.convertBytesToHexString(pdu, 0, pdu.length));
            }

            switch (pduHeader.getCommandId()) {
            case SMPPConstant.CID_BIND_RECEIVER:
            case SMPPConstant.CID_BIND_TRANSMITTER:
            case SMPPConstant.CID_BIND_TRANSCEIVER:
                activityNotifier.notifyActivity();
                sessionContext.getStateProcessor().processBind(pduHeader, pdu, responseHandler);
                break;
            case SMPPConstant.CID_GENERIC_NACK:
                activityNotifier.notifyActivity();
                sessionContext.getStateProcessor().processGenericNack(pduHeader, pdu, responseHandler);
                break;
            case SMPPConstant.CID_ENQUIRE_LINK:
                activityNotifier.notifyActivity();
                sessionContext.getStateProcessor().processEnquireLink(pduHeader, pdu, responseHandler);
                break;
            case SMPPConstant.CID_ENQUIRE_LINK_RESP:
                activityNotifier.notifyActivity();
                sessionContext.getStateProcessor().processEnquireLinkResp(pduHeader, pdu, responseHandler);
                break;
            case SMPPConstant.CID_DELIVER_SM_RESP:
                activityNotifier.notifyActivity();
                sessionContext.getStateProcessor().processDeliverSmResp(pduHeader, pdu, responseHandler);
                break;
            case SMPPConstant.CID_UNBIND:
                activityNotifier.notifyActivity();
                sessionContext.getStateProcessor().processUnbind(pduHeader, pdu, responseHandler);
                break;
            case SMPPConstant.CID_UNBIND_RESP:
                activityNotifier.notifyActivity();
                sessionContext.getStateProcessor().processUnbindResp(pduHeader, pdu, responseHandler);
                break;
            default:
            	sessionContext.getStateProcessor().processUnknownCid(pduHeader, pdu, responseHandler);
            }
        } catch (IOException e) {
            onIOExceptionTask.run();
        }
    }
}
