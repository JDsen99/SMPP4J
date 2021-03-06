/*
 *    Copyright (c) 2021 JDsen
 *    SMPP4J is licensed under Mulan PSL v2.
 *    You can use this software according to the terms and conditions of the Mulan PSL v2.
 *    You may obtain a copy of Mulan PSL v2 at:
 *             http://license.coscl.org.cn/MulanPSL2
 *    THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 *    See the Mulan PSL v2 for more details.
 */
package com.ss.main;


import com.ss.common.Container;
import com.ss.controller.MessageReceiverListenerImpl;
import com.ss.dao.ClientMapper;
import com.ss.monitor.Adapter;
import com.ss.monitor.CommandTask;
import com.ss.pojo.Client;
import com.ss.net.SMPPClient;
import com.ss.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author JDsen99
 * @description  Bootstrap
 * @createDate 2021/10/12-17:00
 */
public class BootStrap {

    private final static Logger logger = LoggerFactory.getLogger(BootStrap.class);

    private Container container = Container.getInstance();

    /**
     * 最大通道数量
     */
    private final static Integer MAX_GATEWAY_NUM;

    /**
     * 活跃通道数量
     */
    private static Integer MAX_ACTIVE_GATEWAY_NUM;



    /**
     * 线程池
     */
    private final ExecutorService executors = new ThreadPoolExecutor(MAX_ACTIVE_GATEWAY_NUM + 2,
            MAX_GATEWAY_NUM + 2,
            10,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(10));

    private static SqlSession sqlSession = MybatisUtils.getSqlSession();

    static {
        ClientMapper mapper = sqlSession.getMapper(ClientMapper.class);

        MAX_GATEWAY_NUM = mapper.countClient();

        MAX_ACTIVE_GATEWAY_NUM = mapper.countActiveClient();
    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BootStrap bootStrap = new BootStrap();
        Container.getInstance().setBootStrap(bootStrap);
        boolean flag = true;
        System.out.println(" s: 启动 , e: 退出 , r 重启通道 , l 查看已存通道状态 \n 请输入：");

        while (flag) {
            String command = scanner.nextLine();
            switch (command) {
                case "s":
                    bootStrap.init();
                    bootStrap.start();
                    flag = false;
                    break;
                default:
                    logger.warn("无效输入。。。{}", command);
                    break;
            }
        }

        flag = true;

        while (flag) {
            try {
                Thread.sleep(1000);
                bootStrap.container.checkClientStatus();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        logger.info("scanner 已退出。。。主线程睡眠。。。请 X ");
    }


    /**
     * 初始化通道
     */
    private void init() {
        logger.info("init gateWay config... gateWay max number : {}  active number : {}", MAX_GATEWAY_NUM, MAX_ACTIVE_GATEWAY_NUM);
        ClientMapper mapper = sqlSession.getMapper(ClientMapper.class);
        List<Client> clientsList = mapper.listActiveClient();
        for (int i = 0; i < clientsList.size(); i++) {
            Client client = clientsList.get(i);
            SMPPClient smppClient = packageClient(client);
            container.addClient(client.getId(), smppClient);
            System.out.println(client);
        }
    }

    /**
     * 启动
     */
    private void start() {
        //启动客户端
//        container.clientStart();

        Adapter[] adapters = new Adapter[3];
//        //启动线程
        logger.info("测试。。。。。。。。。 短信发送线程启动。。。启动数量 3 大号码发生线程 2 ");
        Adapter a1 = new Adapter();
        Adapter a2 = new Adapter();
        Adapter a3 = new Adapter();

        a1.setLaunch(true);
        a2.setLaunch(true);

        adapters[0] = a1;
        adapters[1] = a2;
        adapters[2] = a3;

        executors.execute(a1);
        executors.execute(a2);
        executors.execute(a3);

        CommandTask commandTask = new CommandTask(adapters);
        executors.execute(commandTask);
    }

    /**
     * 封装SMPPClient
     * @param client Client
     * @return SMPPClient
     */
    private static SMPPClient packageClient(Client client) {
        SMPPClient smppClient = new SMPPClient(client.getId());

        if (client.getServerIP() == null || client.getServerIP().trim().length() == 0) {
            smppClient.setServerAddr(client.getServerName().trim());
        } else {
            smppClient.setServerAddr(client.getServerIP().trim());
        }

        smppClient.setPort(client.getPort());
        smppClient.setAccount(client.getUsername().trim());
        smppClient.setPassword(client.getPassword().trim());
        smppClient.setOpen(client.getStatus() == 1);
        smppClient.setLimiter(client.getTps() * client.getThread());
        if (client.getServerName() != null && client.getServerName().trim().length() != 0) {
            smppClient.setServerName(client.getServerName().trim());
        }
        smppClient.setMessageReceiverListener(new MessageReceiverListenerImpl());
        return smppClient;
    }
}
