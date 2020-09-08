package cn.com.sherhom.reno.kafka.common.holder;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Sherhom
 * @date 2020/9/8 9:37
 */
public class ProducerResultHolder {
    public static volatile Map<String,Boolean> topic2Suc=new ConcurrentHashMap<>();
    public static void reset(){
        topic2Suc=new ConcurrentHashMap<>();
    }
    public static void record(String topicName,Boolean result){
        topic2Suc.put(topicName,result);
    }
}
