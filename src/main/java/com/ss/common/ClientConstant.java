/**
 *    Copyright (c) [2021] [JDsen]
 *    [Software Name] is licensed under Mulan PSL v2.
 *    You can use this software according to the terms and conditions of the Mulan PSL v2.
 *    You may obtain a copy of Mulan PSL v2 at:
 *             http://license.coscl.org.cn/MulanPSL2
 *    THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 *    See the Mulan PSL v2 for more details.
 */
package com.ss.common;

import com.google.common.util.concurrent.RateLimiter;
import org.jsmpp.util.AbsoluteTimeFormatter;
import org.jsmpp.util.TimeFormatter;

/**
 * @author JDsen99
 * @description
 * @createDate 2021/10/12-18:31
 */
public class ClientConstant {

    public static final String SERVICE_TYPE = "CMT";

    public static final TimeFormatter TIME_FORMATTER = new AbsoluteTimeFormatter();

    public static RateLimiter limiter = RateLimiter.create(40);

    public void setLimiter(double rate){
        if (rate > 0)
            limiter.setRate(rate);
    }
}
