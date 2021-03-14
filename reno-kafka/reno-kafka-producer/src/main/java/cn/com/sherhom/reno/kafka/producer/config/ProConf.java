package cn.com.sherhom.reno.kafka.producer.config;

import cn.com.sherhom.reno.common.utils.ConfUtil;

/**
 * @author Sherhom
 * @date 2020/9/8 13:51
 */
public class ProConf {
    //KB
    public static final String PRO_ALL_KB_SIZE ="reno.kafka.producer.size.kb";
    public static final String PRO_REPORT_INTERVAL="reno.kafka.producer.report.interval";
    public static final String PRO_REPORT_TO_FILE="reno.kafka.producer.report.toFile";
    public static final String PRO_REPORT_PATH="reno.kafka.producer.report.path";
    public static long allKbSize(){
        return ConfUtil.getLong(PRO_ALL_KB_SIZE,104857600);
    }
    public static int reportInterval(){
        return ConfUtil.getInt(PRO_REPORT_INTERVAL,5000);
    }
    public static boolean isReportToFile(){
        return ConfUtil.getBoolean(PRO_REPORT_TO_FILE,false);
    }

    public static String reportPath(){
        return ConfUtil.get(PRO_REPORT_PATH,"/data/reno/kfk/producer/report");
    }
}
