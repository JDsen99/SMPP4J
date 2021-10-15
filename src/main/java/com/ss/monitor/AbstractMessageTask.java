package com.ss.monitor;

import com.ss.dao.ClientMapper;
import com.ss.pojo.Message;
import com.ss.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;

public abstract class AbstractMessageTask implements Runnable {

    private SqlSession sqlSession;

    /**
     * 将数据插入数据库 taskInfo
     *
     * @param message message
     * @param messageId integer
     */
    protected void insertMessage(Message message, String messageId) {
        sqlSession = MybatisUtils.getSqlSession();
        ClientMapper mapper = sqlSession.getMapper(ClientMapper.class);
        message.setMessageId(messageId);
        mapper.insertMessage(message);
        sqlSession.close();
    }

    /**
     * 封装号码到 Address
     * @return Address
     */
    protected boolean checkPhone(String phone) {
        if (phone.startsWith("86")) {
            return false;
        }
        return true;
    }
}
