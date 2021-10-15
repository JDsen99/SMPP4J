package com.ss.monitor;

import com.ss.common.Container;
import com.ss.controller.MessageReceiverListenerImpl;
import com.ss.dao.ClientMapper;
import com.ss.main.BootStrap;
import com.ss.net.SMPPClient;
import com.ss.pojo.Client;
import com.ss.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class CommandTask implements Runnable{

    private final static Logger logger = LoggerFactory.getLogger(BootStrap.class);

    private Container container = Container.getInstance();

    private static SqlSession sqlSession ;

    private Adapter[] adapters = new Adapter[3];

    public CommandTask(Adapter[] adapters) {
        this.adapters = adapters;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        boolean flag = true;
        System.out.println("e: 退出 , r 重启通道 , l 查看已存通道状态 \n 请输入：");
        while (flag) {
            String command = scanner.nextLine();
            switch (command) {
                case "e":
                    flag = false;
                    close();
                    break;
                case "r":
                    System.out.println("请输入通道ID 进行重启");
                    int id = 0;
                    try {
                        id = scanner.nextInt();
                    } catch (Exception e) {
                        logger.warn("无效输入。。。{}", command);
                    }
                    if (id != 0) {
                        reStartGateWay(id);
                    }
                    break;
                case "l":
                    showGateWayStatus();
                    break;
                default:
                    logger.warn("无效输入。。。{}", command);
                    break;
            }
        }
        logger.info("scanner 已退出。。。主线程睡眠。。。请 X ");

        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
        }
    }
    private static void reStartGateWay(int id) {
        sqlSession = MybatisUtils.getSqlSession();
        ClientMapper mapper = sqlSession.getMapper(ClientMapper.class);
        Client client = mapper.getClientById(id);
        if (client != null) {
            if (client.getStatus() == 1) {
                SMPPClient smppClient = packageClient(client);
                Container.getInstance().addClient(client.getId(), smppClient);
            }
        } else {
            logger.warn("该通道不存在。。。id {}", id);
        }
        sqlSession.close();
    }
    private void close() {
        logger.info("线程开始关闭 。。。");
        for (Adapter adapter : adapters) {
            adapter.setRunning(false);
        }
        logger.info("等待所有的状态报告中 。。。请自行关闭");
    }

    private void showGateWayStatus() {
        container.showGateWayStatus();
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
