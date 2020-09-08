package cn.com.sherhom.reno.kafka.producer.opt;

import cn.com.sherhom.reno.kafka.common.callback.ProducerCallback;
import cn.com.sherhom.reno.kafka.common.record.Stat;
import cn.com.sherhom.reno.kafka.common.utils.MsgUtil;
import cn.com.sherhom.reno.kafka.common.utils.ThroughputThrottler;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

/**
 * @author Sherhom
 * @date 2020/9/8 13:46
 */
public class ProducerOpt {
    public static boolean sendMsg(Producer producer, String topicName, int size, int num, int throughput, Stat stat){
        byte[] msg= MsgUtil.getBytes(size);
        long startMs = System.currentTimeMillis();

        ThroughputThrottler throttler = new ThroughputThrottler(throughput, startMs);
        stat.start();
        for(int i=0;i<num;i++){
            if(stat.isFailed()){
                break;
            }
            ProducerRecord<String,byte[]> record=new ProducerRecord<>(topicName,null,msg);
            long sendStartMs = System.currentTimeMillis();
            producer.send(record,new ProducerCallback(i,sendStartMs,stat));
            if (throttler.shouldThrottle(i, sendStartMs)) {
                throttler.throttle();
            }
        }
        producer.close();
        return !stat.isFailed();
    }
}
