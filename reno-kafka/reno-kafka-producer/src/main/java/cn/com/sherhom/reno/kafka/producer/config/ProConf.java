package cn.com.sherhom.reno.kafka.producer.config;

import cn.com.sherhom.reno.common.utils.ConfUtil;

/**
 * @author Sherhom
 * @date 2020/9/8 13:51
 */
public class ProConf {
    //KB
    public static final String PRO_ALL_KB_SIZE ="reno.kafka.producer.size";
    public static final String PRO_REPORT_INTERVAL="reno.kafka.producer.report.interval";
    public static int allKbSize(){
        return ConfUtil.getInt(PRO_ALL_KB_SIZE,10485760);
    }
    public static int reportInterval(){
        return ConfUtil.getInt(PRO_REPORT_INTERVAL,5000);
    }
}
