/*
 *    Copyright (c) 2021 JDsen
 *    SMPP4J is licensed under Mulan PSL v2.
 *    You can use this software according to the terms and conditions of the Mulan PSL v2.
 *    You may obtain a copy of Mulan PSL v2 at:
 *             http://license.coscl.org.cn/MulanPSL2
 *    THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 *    See the Mulan PSL v2 for more details.
 */
package com.ss.utils;

import org.jsmpp.bean.Alphabet;
import org.jsmpp.bean.DataCoding;
import org.jsmpp.bean.GeneralDataCoding;
import org.jsmpp.bean.MessageClass;

import java.nio.charset.StandardCharsets;

/**
 * @author JDsen99
 * @description
 * @createDate 2021/10/12-19:57
 */
public class CommUtil {
    /**
     * SMPP短信内容字符集判断
     *
     * @param content content
     * @return DataCoding
     */
    public static DataCoding getSmppCharsetInfo(String content) {
        DataCoding ret = new GeneralDataCoding(Alphabet.ALPHA_UCS2, MessageClass.CLASS2, false);
//        DataCoding ret = new GeneralDataCoding(Alphabet.ALPHA_UCS2, MessageClass.CLASS1, false);
        if (CommUtil.checkLatinCharset(content)) {
            ret = new GeneralDataCoding(Alphabet.ALPHA_DEFAULT, MessageClass.CLASS1, false);
        }
        return ret;
    }
    /*
     * 判断短信内容是否是纯英文拉丁字符
     */
    public static boolean checkLatinCharset(String content) {
        try {
            char[] chars = content.toCharArray();
            byte[] bytes = content.getBytes(StandardCharsets.ISO_8859_1);
            for (int index = 0; index < bytes.length; index++) {
                if (bytes[index] == 63) {
                    if (chars[index] != 63) {
                        return false;
                    }
                }
            }
            return true;
        } catch (Exception ignored) {
        }
        return false;
    }
}
