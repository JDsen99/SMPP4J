package com.ss.common;


import com.ss.main.BootStrap;
import com.ss.pojo.Client;
import com.ss.smpp.SMPPClient;
import org.jsmpp.session.SMPPSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
}
