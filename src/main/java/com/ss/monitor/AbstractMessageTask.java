/**
 *    Copyright (c) [2021] [JDsen]
 *    [Software Name] is licensed under Mulan PSL v2.
 *    You can use this software according to the terms and conditions of the Mulan PSL v2.
 *    You may obtain a copy of Mulan PSL v2 at:
 *             http://license.coscl.org.cn/MulanPSL2
 *    THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 *    See the Mulan PSL v2 for more details.
 */
package com.ss.monitor;

import com.ss.dao.ClientMapper;
import com.ss.pojo.Message;
import com.ss.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractMessageTask implements Runnable {

    private Logger logger = LoggerFactory.getLogger(AbstractMessageTask.class);

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
        mapper.insertSendInfo(message);
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

    protected void updateMessage(Integer id, Integer status) {
        if (status == 2) {
            logger.info("一条记录已完成 。。。 ID : {}", id);
        }

        sqlSession = MybatisUtils.getSqlSession();
        ClientMapper mapper = sqlSession.getMapper(ClientMapper.class);
        mapper.updateMessageStatus(id, status);
        sqlSession.close();
    }
}
