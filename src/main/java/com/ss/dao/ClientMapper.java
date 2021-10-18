package com.ss.dao;

import com.ss.pojo.Client;
import com.ss.pojo.Message;
import com.ss.pojo.Mobile;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author JDsen99
 * @description
 * @createDate 2021/9/17-10:40
 */
public interface ClientMapper {

    /**
     * 列出所有的通道
     * @return List<Client>
     */
    List<Client> listAllClient();

    /**
     * 列出所有的活跃的通道
     * @return List<Client>
     */
    List<Client> listActiveClient();

    /**
     * 通道总数
     * @return Integer
     */
    Integer countClient();

    /**
     * 最大或与通道数
     * @return Integer
     */
    Integer countActiveClient();

    /**
     * 查询所有未发生（bProcess = 0 && sendCount !< 200）的短信
     * @return List<Message>
     */
    List<Message> listUnDeliverMessage();

    /**
     * 查询所有未发生（bProcess = 0 && sendCount > 200）的 大短信
     * @return List<Message>
     */
    List<Message> listBigUnDeliverMessage();

    /**
     * 获取 mobile info 的号码
     * @return List<Mobile>
     */
    List<Mobile> listMobile(@Param("taskId") int taskId);

    /**
     * 更新taskInfo表的状态
     * @param id  id
     * @param status status
     */
    void updateMessageStatus(@Param("id") int id,@Param("status") int status);

    /**
     * 更新大号码表 状态
     * @param id id
     * @param status status
     */
    void updateMobileStatus(@Param("id") int id,@Param("status") int status);

    /**
     * 将数据插入SendInfo
     * @param message message对象
     */
    void insertSendInfo(Message message);

    /**
     * 获取指定ID 的 通道
     * @param id id
     * @return Client
     */
    Client getClientById(int id);

    /**
     * 大号码未发生 回滚
     * @param status 3
     * @param index index
     * @param messageId messageId
     */
    void rollBackBigMessage(@Param("status") int status, @Param("index") String index,@Param("id") int messageId);

    /**
     * 更新SendInfo 的状态
     * @param status  状态
     * @param errorCode 错误码
     * @param messageId messageId
     */
    void updateSendInfo(@Param("status") String status,@Param("errorCode") String errorCode, @Param("messageId") String messageId);

    /**
     * TODO
     * @param id id
     * @param status status
     */
    void updateBigMessageStatus(@Param("id") int id,@Param("status") int status);

}
