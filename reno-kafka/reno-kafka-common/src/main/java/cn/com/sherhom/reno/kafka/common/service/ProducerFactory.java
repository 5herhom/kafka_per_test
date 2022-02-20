package cn.com.sherhom.reno.kafka.common.service;

import cn.com.sherhom.reno.kafka.common.utils.KfkConf;
import org.apache.kafka.clients.producer.KafkaProducer;

import java.util.Properties;

/**
 * @author Sherhom
 * @date 2020/9/7 17:06
 */
public class ProducerFactory{
    public static <K,V> KafkaProducer<K,V> newProducer(){
        Properties config=getCommonProducer();
        config.put("bootstrap.servers", KfkConf.kfkIp());     // 键的序列化
        config.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");  // 值的序列化
        return new KafkaProducer<>(config);
    }
    public static Properties getCommonProducer(){
        Properties config=new Properties();
        config.put("acks","1");
        config.put("batch.size",32768);
        config.put("retries",30);
        config.put("max.request.size",6242890);
        config.put("request.timeout.ms",120000);
        config.put("linger.ms",3);
        config.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");       // 键的序列化
        config.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");  // 值的序列化
        return config;
    }
}
