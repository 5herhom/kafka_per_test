package cn.com.sherhom.reno.common.utils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Sherhom
 * @date 2020/9/4 11:33
 */
@Slf4j
public class LogUtil {
    public static void printStackTrace(Throwable e) {
        log.error(ExceptionUtil.toStackTraceString(e));
    }
}
