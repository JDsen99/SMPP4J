/**
 *    Copyright (c) [2021] [JDsen]
 *    [Software Name] is licensed under Mulan PSL v2.
 *    You can use this software according to the terms and conditions of the Mulan PSL v2.
 *    You may obtain a copy of Mulan PSL v2 at:
 *             http://license.coscl.org.cn/MulanPSL2
 *    THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 *    See the Mulan PSL v2 for more details.
 */
package com.ss.common;


import com.ss.main.BootStrap;
import com.ss.net.SMPPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public void checkClientStatus() {
        Collection<SMPPClient> values = clientMap.values();
        for (SMPPClient value : values) {
            if (!value.getSessionState().isTransmittable()) {
                LOGGER.info("检测到通道未连接 正在连接。。。ID : {}",value.getId());
                value.doConnect();
                if (!value.getSessionState().isTransmittable()) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
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
