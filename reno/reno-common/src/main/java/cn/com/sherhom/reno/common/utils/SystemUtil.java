package cn.com.sherhom.reno.common.utils;

import lombok.extern.slf4j.Slf4j;
/**
 * @author Sherhom
 * @date 2020/9/2 20:12
 */
@Slf4j
public class SystemUtil {
    static public void exit(){
        log.info("Start to shutdown.");
        System.exit(0);
    }
}
