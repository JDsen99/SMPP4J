package com.ss.common;


import com.ss.controller.MessageReceiverListenerImpl;
import com.ss.main.BootStrap;
import com.ss.pojo.Client;
import com.ss.smpp.SMPPClient;
import org.jsmpp.bean.BindType;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.TypeOfNumber;
import org.jsmpp.session.BindParameter;
import org.jsmpp.session.SMPPSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author JDsen99
 * @description
 * @createDate 2021/9/17-16:15
 */
public class Container {
    /**
     * 日志管理器
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Container.class);

    private static final Map<Integer, SMPPClient> clientMap = new ConcurrentHashMap<>();


    public static final Object MONITOR = new Object();

    /**
     * 启动类
     */
    private BootStrap bootStrap;


    /**
     * 获取单例容器对象
     *
     * @return 容器对象
     */
    public static Container getInstance() {

        return ContainerHolder.HOLDER.instance;
    }

    public SMPPClient getClient(int taskId) {
        return clientMap.getOrDefault(taskId, null);
    }

    public void addClient(int id, SMPPClient smppClient) {
        clientMap.put(id,smppClient);
    }

    public void clientStart() {
        Collection<SMPPClient> values = clientMap.values();
        for (SMPPClient client : values) {
            client.doConnect();
        }
    }

    public void setBootStrap(BootStrap bootStrap) {
        this.bootStrap = bootStrap;
    }

    public void showGateWayStatus() {
        Collection<SMPPClient> values = clientMap.values();
        for (SMPPClient client : values) {
            client.showStatus();
        }
    }

    private enum ContainerHolder {
        /**
         * 存储数据容器的枚举对象
         */
        HOLDER;
        private Container instance;
        ContainerHolder() {
            instance = new Container();
        }
    }

//    private SMPPSession initSession() {
//        SMPPSession session = new SMPPSession();
//        try {
//            session.setMessageReceiverListener(new MessageReceiverListenerImpl());
//            String systemId = session.connectAndBind(smppIp, port, new BindParameter(BindType.BIND_TX, username, password, "cp", TypeOfNumber.UNKNOWN, NumberingPlanIndicator.UNKNOWN, null));
//            LOGGER.info("Connected with SMPP with system id {}", systemId);
//        } catch (IOException e) {
//            LOGGER.error("I/O error occured", e);
//            session = null;
//        }
//        return session;
//    }
}
