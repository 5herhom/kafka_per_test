package cn.com.sherhom.reno.kafka.producer.fairyland;

import cn.com.sherhom.reno.boot.annonation.ToExplore;
import cn.com.sherhom.reno.kafka.common.entity.ProducerRunnerArgs;
import cn.com.sherhom.reno.kafka.common.run.ProducerThread;

import java.util.ArrayList;
import java.util.List;

public class KafkaProducerFairyLand {
    public static final String topicFormat=
            "RENO_TEST_TOPIC_PARTITION_%s_%s";
    @ToExplore
    public void entrance(int topicNum,
                         int partitionNum,
                         int producerNum,
                         int consumerNum,
                         int kbPerMsg,
                         int kbPerInput,
                         int kbPerOutput){
        List<String> topics = new ArrayList<>();
        List<ProducerThread> proThreads=new ArrayList<>();

        for (int i = 0; i < topicNum; i++) {
            topics.add(String.format(topicFormat,partitionNum,i));
        }
        ProducerRunnerArgs proArgs;
        for (int i = 0; i < producerNum; i++) {
            proArgs=new ProducerRunnerArgs();
            proThreads.add(new ProducerThread(proArgs));
        }

    }
}
