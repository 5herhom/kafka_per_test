package cn.com.sherhom.reno.kafka.consumer;

import cn.com.sherhom.reno.common.utils.DateUtil;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.record.Record;
import org.junit.Test;

import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author Sherhom
 * @Date 2022/2/20 15:07
 */
public class ConsumerTest {
    @Test
    public void consumerTest01(){
        List<String> topics= Stream.of("shehrom-test").collect(Collectors.toList());
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "119.29.54.129:9092,47.96.65.208:9092");
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "group"+new Date().toString());
        properties.put("max.poll.records", 1);
        properties.put("request.timeout.ms", 15000);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String,String> consumer=new KafkaConsumer(properties);
        consumer.subscribe(topics);
        while ((true)){
            ConsumerRecords<String,String> records=consumer.poll(1000);
            records.forEach(record-> System.out.println(getRecordsString(record)));

        }
    }

    public static <K,V> String getRecordsString(ConsumerRecord<K,V> record){
        return
        "ConsumerRecord(topic = " + record.topic() + ", partition = " + record.partition() + ", offset = " + record.offset()
                + ", " + record.timestampType() + " = " + DateUtil.date2String(new Date(record.timestamp()),"yyyy-MM-dd HH-mm-ss")
                + ", headers = " + record.headers()
                + ", key = " + record.key() + ", value = " + record.value() + ")";
    }
}
