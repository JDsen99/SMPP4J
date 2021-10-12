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
