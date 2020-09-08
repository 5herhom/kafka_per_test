package cn.com.sherhom.reno.kafka.common.callback;

import cn.com.sherhom.reno.common.utils.LogUtil;
import cn.com.sherhom.reno.kafka.common.record.Stat;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;

/**
 * @author Sherhom
 * @date 2020/9/7 19:39
 */
@Slf4j
public class ProducerCallback implements Callback {
    private final Integer index;
    private final Stat stat;
    private final long start;
    public ProducerCallback(int index,long start,Stat stat){
        this.index=index;
        this.start=start;
        this.stat =stat;
    }
    @Override
    public void onCompletion(RecordMetadata recordMetadata, Exception e) {
        if(e!=null){
            LogUtil.printStackTrace(e);
            stat.setFailed();
        }
        else{
            long latency=System.currentTimeMillis()-this.start;
            stat.record(this.index,recordMetadata.serializedValueSize(),1,latency);
        }

    }
}
