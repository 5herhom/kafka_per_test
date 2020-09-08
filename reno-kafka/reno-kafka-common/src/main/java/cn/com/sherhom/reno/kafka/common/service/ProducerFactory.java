package cn.com.sherhom.reno.kafka.common.service;

import cn.com.sherhom.reno.kafka.common.utils.KfkConf;
import org.apache.kafka.clients.producer.KafkaProducer;

import java.util.Properties;

/**
 * @author Sherhom
 * @date 2020/9/7 17:06
 */
public class ProducerFactory<K,V> {
    public KafkaProducer<K,V> newProducer(){
        Properties config=new Properties();
        config.put("bootstrap.servers", KfkConf.kfkIp());
        config.put("acks","1");
        config.put("batch.size",16384);
        config.put("retries",30);
        config.put("max.request.size",6242890);
        config.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");       // 键的序列化
        config.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");  // 值的序列化
        return new KafkaProducer<>(config);
    }
}
