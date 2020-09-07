package cn.com.sherhom.reno.kafka.common.utils;

import cn.com.sherhom.reno.common.utils.ConfUtil;

/**
 * @author Sherhom
 * @date 2020/9/4 14:04
 */
public class KfkConf {
    public static final String ZK_MAX_WAIT_TIME_KEY="zookeeper.max.wait.time";
    public static final String ZK_MAX_WAIT_RETRY_KEY="zookeeper.max.retry.time";
    public static final String ZK_IP_KEY="zookeeper.server.ip";
    public static final String KFK_IP_KEY="kafka.server.ip";
    public static final String CONSUMER_SERVER="reno.kafka.consumer.server";
    public static final String CONSUMER_SERVER_PORT="reno.kafka.consumer.server.port";

    public static int zkMaxWaitTime(){
        return Integer.parseInt(ConfUtil.get(ZK_MAX_WAIT_TIME_KEY,"15000"));
    }

    public static int zkMaxRetry(){
        return Integer.parseInt(ConfUtil.get(ZK_MAX_WAIT_TIME_KEY,"10000"));
    }
    public static int consumerServerPort(){
        return Integer.parseInt(ConfUtil.get(CONSUMER_SERVER_PORT,"14040"));
    }
    public static int consumerServer(){
        return Integer.parseInt(ConfUtil.get(CONSUMER_SERVER,"localhost"));
    }
    public static String zkIp(){
        return ConfUtil.get(ZK_IP_KEY,"localhost:2181");
    }
    public static String kfkIp(){
        return ConfUtil.get(KFK_IP_KEY,"localhost:9092");
    }
}
