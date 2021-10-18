package com.ss.monitor;
import com.ss.pojo.Message;
import com.ss.net.SMPPClient;
import com.ss.utils.CommUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author JDsen99
 * @description
 * @createDate 2021/10/12-18:27
 */
public class MessageTask extends AbstractMessageTask {

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
            if (checkPhone(message.getPhone())) {
                String messageId = client.submitShortMessage(message.getSendId().trim(),message.getPhone(),(byte) 1,CommUtil.getSmppCharsetInfo(message.getContent()),message.getContent());
                insertMessage(message,messageId);
            }else {
                logger.error("中国短信 拒绝发送 。。。 Id :{} ",message.getId());
            }
        } catch (Exception e) {
            logger.error("短信发送错误 phone Id :{}  gateWay ID : {}  {}",message.getId(),message.getTaskId(),e.getMessage());
        }
    }


}
