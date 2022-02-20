package cn.com.sherhom.reno.kafka.producer;

import cn.com.sherhom.reno.common.entity.Pair;
import cn.com.sherhom.reno.common.utils.DateUtil;
import cn.com.sherhom.reno.kafka.common.service.ProducerFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.TopicPartition;
import org.junit.Test;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author Sherhom
 * @Date 2022/2/20 15:42
 */
@Slf4j
public class TransactionTest {
    @Test
    public void normalTest() {
        String topic = "shehrom-test";
        Properties config = ProducerFactory.getCommonProducer();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "47.96.65.208:9092,119.29.54.129:9092");
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(config);
        runTest(kafkaProducer, topic, () -> {
            return new Pair<>(String.valueOf(RandomUtils.nextInt(0, 10)),
                    String.valueOf(RandomUtils.nextInt(0, 10)));
        });
    }

    @Test
    public void idempotentTest() {
        Properties config = ProducerFactory.getCommonProducer();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "47.96.65.208:9092,119.29.54.129:9092");

        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(config);

    }

    String srcTopic = "batchTransactionProduceTopic";
    String targetTopic = "sherhom-tgt";
    String broker = "47.96.65.208:9092,119.29.54.129:9092";

    @Test
    public void transactionProduceAndConsume() {
        String transactionId = "test-transactional";
        Properties producerProp = ProducerFactory.getCommonProducer();
        producerProp.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, broker);

        producerProp.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, transactionId);
        producerProp.put(ProducerConfig.ACKS_CONFIG,"all");

        KafkaProducer<String, String> producer = new KafkaProducer<>(producerProp);

        Properties consumerProp = getConsumerProp();
        String groupId = "group-" + new Date().getTime();
        consumerProp.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        consumerProp.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed"); // 设置隔离级别
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerProp);

        // 订阅主题
        consumer.subscribe(Arrays.asList(srcTopic));
        producer.initTransactions();
        for (; ; ) {
            // 开启事务
            producer.beginTransaction();
            // 接受消息
            ConsumerRecords<String, String> records = consumer.poll(500);
            // 处理逻辑
            for (ConsumerRecord record : records) {

                try {
                    Map<TopicPartition, OffsetAndMetadata> commits = new HashMap<>();
                    // 处理消息
                    System.out.printf("offset = %d, key = %s, value = %s\n", record.offset(), record.key(), record.value());
                    // 记录提交的偏移量
                    commits.put(new TopicPartition(record.topic(), record.partition()), new OffsetAndMetadata(record.offset()));
                    // 产生新消息
                    Future<RecordMetadata> metadataFuture = producer.send(new ProducerRecord<>(targetTopic, record.value() + "-send"));
                    // 提交偏移量
                    producer.sendOffsetsToTransaction(commits, groupId);
                    if(record.value().equals("0")){
                        throw new RuntimeException();
                    }
                    // 事务提交
                    producer.commitTransaction();

                } catch (Exception e) {
                    e.printStackTrace();
                    producer.abortTransaction();
                }
            }

        }
    }

    @Test
    public void transactionProduce() {
        String transactionId = "test-transactional";
        String topic="batchTransactionProduceTopic";
        Properties producerProp = ProducerFactory.getCommonProducer();
        producerProp.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, broker);

        producerProp.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, transactionId);
        producerProp.put(ProducerConfig.ACKS_CONFIG,"all");

        KafkaProducer<String, String> producer = new KafkaProducer<>(producerProp);

        // 订阅主题
        producer.initTransactions();

        // 开启事务
        producer.beginTransaction();
        // 接受消息
        // 处理逻辑
        for(int i=0;i<10;i++){
            Future<RecordMetadata> metadataFuture = producer.send(new ProducerRecord<>(topic, String.valueOf(i)));

        }
        producer.commitTransaction();
        producer.send(new ProducerRecord<>(topic, String.valueOf(11)));
    }
    public Properties getConsumerProp() {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "119.29.54.129:9092,47.96.65.208:9092");
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "group-" + new Date().getTime());
        properties.put("max.poll.records", 1);
        properties.put("request.timeout.ms", 15000);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        return properties;
    }

    @Test
    public void consumerTest01() {
//        List<String> topics = Stream.of("sherhom-test").collect(Collectors.toList());
        List<String> topics = Stream.of("sherhom-tgt").collect(Collectors.toList());
        Properties consumerProp = getConsumerProp();
        consumerProp.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed"); // 设置隔离级别
        consumerProp.put("max.poll.records", 1000000);
        KafkaConsumer<String, String> consumer = new KafkaConsumer(consumerProp);
        consumer.subscribe(topics);
        while ((true)) {
            ConsumerRecords<String, String> records = consumer.poll(5000);
            Map<TopicPartition, List<ConsumerRecord<String, String>>>
                    tp2Records = records.partitions().stream().collect(Collectors.toMap(p -> p, p -> records.records(p)));
            Map<TopicPartition, List<ConsumerRecord<String, String>>> tp2SortedRecords = tp2Records.entrySet().stream().collect(Collectors.toMap(e -> e.getKey(),
                    e -> e.getValue().stream().sorted((r1, r2) -> (r1.offset() - r2.offset()) < 0 ? -1 : (r1.offset() - r2.offset() == 0 ? 0 : 1)).collect(Collectors.toList())));
            List<Pair<ConsumerRecord<String, String>, ConsumerRecord<String, String>>> missedRecords = new ArrayList<>();
            tp2SortedRecords.forEach((k, v) -> {
                System.out.println("==============topic:" + k.topic() + ";partition:" + k.partition() + "==================");
                Optional<ConsumerRecord<String, String>> lastRecord = Optional.empty();
                v.forEach(e -> {
                    long curOffset = e.offset();
                    System.out.println("offset:" + curOffset + ";value:" + e.value());
                    if (lastRecord.isPresent()
                            && (curOffset - lastRecord.get().offset() != 1)) {
                        missedRecords.add(new Pair<>(lastRecord.get(), e));
                        System.out.println("Missed record between:" + printPairRecord(missedRecords.get(missedRecords.size() - 1)));
                    }
                });
            });
            missedRecords.forEach(r -> System.out.println(printPairRecord(r)));
        }
    }

    @Test
    public void consumerTestUnCommit() {
//        List<String> topics = Stream.of("sherhom-test").collect(Collectors.toList());
        List<String> topics = Stream.of("batchTransactionProduceTopic").collect(Collectors.toList());
        Properties consumerProp = getConsumerProp();
//        consumerProp.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed"); // 设置隔离级别
        consumerProp.put("max.poll.records", 1000000);
        KafkaConsumer<String, String> consumer = new KafkaConsumer(consumerProp);
        consumer.subscribe(topics);
        while ((true)) {
            ConsumerRecords<String, String> records = consumer.poll(5000);
            Map<TopicPartition, List<ConsumerRecord<String, String>>>
                    tp2Records = records.partitions().stream().collect(Collectors.toMap(p -> p, p -> records.records(p)));
            Map<TopicPartition, List<ConsumerRecord<String, String>>> tp2SortedRecords = tp2Records.entrySet().stream().collect(Collectors.toMap(e -> e.getKey(),
                    e -> e.getValue().stream().sorted((r1, r2) -> (r1.offset() - r2.offset()) < 0 ? -1 : (r1.offset() - r2.offset() == 0 ? 0 : 1)).collect(Collectors.toList())));
            List<Pair<ConsumerRecord<String, String>, ConsumerRecord<String, String>>> missedRecords = new ArrayList<>();
            tp2SortedRecords.forEach((k, v) -> {
                System.out.println("==============topic:" + k.topic() + ";partition:" + k.partition() + "==================");
                Optional<ConsumerRecord<String, String>> lastRecord = Optional.empty();
                v.forEach(e -> {
                    long curOffset = e.offset();
                    System.out.println("offset:" + curOffset + ";value:" + e.value());
                    if (lastRecord.isPresent()
                            && (curOffset - lastRecord.get().offset() != 1)) {
                        missedRecords.add(new Pair<>(lastRecord.get(), e));
                        System.out.println("Missed record between:" + printPairRecord(missedRecords.get(missedRecords.size() - 1)));
                    }
                });
            });
            missedRecords.forEach(r -> System.out.println(printPairRecord(r)));
        }
    }

    public static String printPairRecord(Pair<ConsumerRecord<String, String>, ConsumerRecord<String, String>> recordPair) {
        return Stream.of(recordPair.getKey(), recordPair.getValue())
                .map(r -> "[p=" + r.partition() + ";offset=" + r.offset() + "v=" + r.value() + "]").collect(Collectors.joining("---"));
    }

    public static <K, V> String getRecordsString(ConsumerRecord<K, V> record) {
        return
                "ConsumerRecord(topic = " + record.topic() + ", partition = " + record.partition() + ", offset = " + record.offset()
                        + ", " + record.timestampType() + " = " + DateUtil.date2String(new Date(record.timestamp()), "yyyy-MM-dd HH-mm-ss")
                        + ", key = " + record.key() + ", value = " + record.value() + ")";
    }

    public static <K, V> void runTest(KafkaProducer<K, V> kafkaProducer, String topic, Supplier<Pair<K, V>> supplier) {
        runTest(kafkaProducer, topic, supplier, 1000);
    }

    public static <K, V> void runTest(KafkaProducer<K, V> kafkaProducer, String topic, Supplier<Pair<K, V>> supplier, long sleepMill) {
        String msg;
        while (true) {
            Pair<K, V> pair = supplier.get();
            System.out.println("========================================");
            System.out.println(pair.toString());
            ProducerRecord<K, V> record = new ProducerRecord<>(topic, pair.getKey(), pair.getValue());
            kafkaProducer.send(record, new Callback() {
                @Override
                public void onCompletion(RecordMetadata metadata, Exception exception) {
                    System.out.println(metadata.toString() + (exception == null ? ":Success" : ":Fail:" + exception.getMessage()));
                }
            });
            try {
                TimeUnit.MILLISECONDS.sleep(sleepMill);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
