package com.ss.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author JDsen99
 * @description
 * @createDate 2021/9/17-12:28
 */
public class MybatisUtils {
    private static InputStream inputStream;
    private static SqlSessionFactory sqlSessionFactory;
    //从 SqlSessionFactory 中获取 SqlSession
    static {
        try {
            String resource = "mybatis-config.xml";
            inputStream = Resources.getResourceAsStream(resource);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static SqlSession getSqlSession() {
        return sqlSessionFactory.openSession(true);
    }
}
