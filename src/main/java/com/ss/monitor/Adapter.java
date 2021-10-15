package com.ss.monitor;
import com.ss.common.Container;
import com.ss.dao.ClientMapper;
import com.ss.pojo.Message;
import com.ss.pojo.Mobile;
import com.ss.net.SMPPClient;
import com.ss.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.jsmpp.bean.Address;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.TypeOfNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

public class Adapter implements Runnable {

    private Logger logger = LoggerFactory.getLogger(Adapter.class);

    private Container container = Container.getInstance();

    private volatile boolean running = true;


    /**
     * 是否为大号码发送线程
     */
    private boolean launch = false;

    private SqlSession sqlSession;

    private List<Message> preData = Collections.synchronizedList(new ArrayList<>());

    private final ExecutorService executors =
            new ThreadPoolExecutor(10,
                    100,
                    1,
                    TimeUnit.SECONDS,
                    new ArrayBlockingQueue<>(1000),
                    Executors.defaultThreadFactory(),
                    new ThreadPoolExecutor.CallerRunsPolicy());

    @Override
    public void run() {

        int times = 1;
        while (running) {
            getMessageFromDatabase();
            if (preData.size() <= 0) {
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    // do nothing
                }
                continue;
            }
            times++;
            logger.info("开始发送短信。。。此次记录总量：{}  threadName : {}", preData.size(), Thread.currentThread().getName());
            for (Message message : preData) {
                SMPPClient client = container.getClient(message.getTaskId());
                try {
                    //验证通道状态
                    if (checkClient(client)) {
                        String phone = message.getPhone();
                        //判断是否为大短信
                        if (launch) {
                            List<Mobile> mobiles = getMobile(message.getId());
                            Address[] addresses = prepareAddress(mobiles);
                            sendMessage(addresses,message,client);
                        } else {
                            //判断是否 为多号码
                            if (phone.contains(",")) {
                                if (phone.endsWith(",")) phone = phone.substring(0, phone.length() - 1);
                                String[] split = phone.split(",");
                                Address[] addresses = prepareAddress(split);
                                sendMessage(addresses,message,client);
                            } else {
                                sendMessage(message,client);
                            }
                        }
                    } else {
                        logger.warn("通道错误，重新链接失败。。记录已回写。。id {}", client.getId());
                        updateMessage(message.getId(), 3);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    updateMessage(message.getId(), 4);
                }
            }
        }
        logger.warn("adapter 线程已关闭 发送次数 : {}  线程名 : {} ", times, Thread.currentThread().getName());
    }


    /**
     * 验证 通道
     *
     * @param client client
     * @return boolean
     */
    private boolean checkClient(SMPPClient client) {
        if (client == null) return false;
        synchronized (client) {
            int times = 0;
            while (times++ < 10 && !client.getSessionState().isTransmittable()) {
                try {
                    logger.warn("通道未连接 正在尝试重新链接。。重连次数 {}", times);
                    System.out.println(client);
                    client.doConnect();
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException();
                }
            }
            return client.getSessionState().isTransmittable();
        }
    }

    /**
     * 获取数据
     */
    private void getMessageFromDatabase() {
        synchronized (Container.MONITOR) {
            sqlSession = MybatisUtils.getSqlSession();
            ClientMapper mapper = sqlSession.getMapper(ClientMapper.class);
            List<Message> messages;
            if (launch) {
                messages = mapper.listBigUnDeliverMessage();
            } else {
                messages = mapper.listUnDeliverMessage();
            }
            logger.info("读取数据库。。。threadName : {}", Thread.currentThread().getName());
            preData.clear();
            if (messages != null) {
                if (messages.size() != 0) {
                    for (Message data : messages) {
                        data.setStatus(1);
                        preData.add(data);
                        mapper.updateMessageStatus(data.getId(), data.getStatus());
                    }
                }
            }
            sqlSession.close();
            Container.MONITOR.notifyAll();
        }
    }

    /**
     * 获取 大号码 短信
     *
     * @param taskId taskId
     * @return List<Mobile>
     */
    private List<Mobile> getMobile(int taskId) {
        sqlSession = MybatisUtils.getSqlSession();
        ClientMapper mapper = sqlSession.getMapper(ClientMapper.class);

        logger.info("读取数据库 mobileInfo。。。threadName : {}", Thread.currentThread().getName());

        List<Mobile> mobiles = mapper.listMobile(taskId);
        if (mobiles != null) {
            if (mobiles.size() != 0) {
                for (Mobile data : mobiles) {
                    data.setStatus(1);
                    mapper.updateMobileStatus(data.getId(), data.getStatus());
                }
            }
        }
        sqlSession.close();
        return mobiles;
    }

    private void updateMessage(Integer id, Integer status) {
        sqlSession = MybatisUtils.getSqlSession();
        ClientMapper mapper = sqlSession.getMapper(ClientMapper.class);
        mapper.updateMessageStatus(id, status);
        sqlSession.close();
        logger.warn("发送短信过程中出错。。。 数据已写回  threadName : {}", Thread.currentThread().getName());
    }

    /**
     * 发送短信
     *
     * @param message 短信
     * @param client  客户端
     */
    private void sendMessage(Message message, SMPPClient client){
        logger.info("send message ... : {} id {} phone {}" ,message,message.getId(),message.getPhone());
        MessageTask task = new MessageTask(message, client);
        executors.submit(task);
    }

    /**
     * 发送短信
     *
     * @param message 短信
     * @param client  客户端
     * @throws ExecutionException   异常
     * @throws InterruptedException 异常
     */
    private void sendMessage(Address[] address,Message message, SMPPClient client) throws Exception {
        BigMessageTask task = new BigMessageTask(address,message, client);
        executors.submit(task);
    }

    /**
     * 将数据插入数据库 taskInfo
     *
     * @param message message
     * @param messageId integer
     */
    private void insertMessage(Message message, String messageId) {
        sqlSession = MybatisUtils.getSqlSession();
        ClientMapper mapper = sqlSession.getMapper(ClientMapper.class);
        message.setMessageId(messageId);
        mapper.insertMessage(message);
        sqlSession.close();
    }

    /**
     * 封装号码到 Address
     * @param numbers phone
     * @return Address
     */
    private  Address[] prepareAddress(List<Mobile> numbers) {
        List<Address> addresses = new ArrayList<>();
        for (Mobile number : numbers) {
            if (checkPhone(number.getPhone())) addresses.add(new Address(TypeOfNumber.NATIONAL, NumberingPlanIndicator.UNKNOWN, number.getPhone()));
        }
        return (Address[]) addresses.toArray();
    }

    /**
     * 封装号码到 Address
     * @return Address
     */
    private boolean checkPhone(String phone) {
        if (phone.startsWith("86")) {
            logger.error("中国短信 {} 拒绝发送...", phone);
            return false;
        }
        return true;
    }

    /**
     * 封装号码到 Address
     * @param numbers phone
     * @return Address
     */
    private Address[] prepareAddress(String[] numbers) {
        List<Address> addresses = new ArrayList<>();
        for (String number : numbers) {
            if (checkPhone(number))
                addresses.add(new Address(TypeOfNumber.NATIONAL, NumberingPlanIndicator.UNKNOWN, number));
        }
        return (Address[]) addresses.toArray();
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isLaunch() {
        return launch;
    }

    public void setLaunch(boolean launch) {
        this.launch = launch;
    }
}
