package cn.com.sherhom.reno.kafka.common.utils;

import cn.com.sherhom.reno.common.utils.ConfUtil;

/**
 * @author Sherhom
 * @date 2020/9/4 14:04
 */
public class KfkConf {
    public static final String ZK_MAX_WAIT_TIME_KEY="zookeeper.max.wait.time";
    public static final String ZK_MAX_WAIT_RETRY_KEY="zookeeper.max.retry.time";

    public static int getZkMaxWaitTime(){
        return Integer.parseInt(ConfUtil.get(ZK_MAX_WAIT_TIME_KEY,"15000"));
    }

    public static int getZkMaxRetry(){
        return Integer.parseInt(ConfUtil.get(ZK_MAX_WAIT_TIME_KEY,"10000"));
    }
}
