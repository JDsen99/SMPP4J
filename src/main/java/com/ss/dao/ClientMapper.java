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

    List<Message> listUnDeliverMessage();

    /**
     * 获取 mobile info 的号码
     * @return
     */
    List<Message> listBigUnDeliverMessage();

    /**
     * 获取 mobile info 的号码
     * @return
     */
    List<Mobile> listMobile(@Param("taskId") int taskId);

    /**
     * 更新taskInfo表的状态
     * @param id
     * @param status
     */
    void updateMessageStatus(@Param("id") int id,@Param("status") int status);

    /**
     * 更新大号码表 状态
     * @param id
     * @param status
     */
    void updateMobileStatus(@Param("id") int id,@Param("status") int status);

    void insertMessage(Message message);

    void updateMessage(@Param("messageId") String messageId ,@Param("status") String status);

    Client getClientById(int id);

    void rollBackBigMessage(@Param("status") int status, @Param("index") String index,@Param("id") int messageId);

    void updateSendInfo(@Param("status") int status, @Param("seqNo") String seqNo, @Param("messageId") String messageId);

    void updateBigMessageStatus(@Param("id") int id,@Param("status") int status);
}
