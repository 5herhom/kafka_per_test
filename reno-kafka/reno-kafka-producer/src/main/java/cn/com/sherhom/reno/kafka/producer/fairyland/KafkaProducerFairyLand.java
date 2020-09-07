package cn.com.sherhom.reno.kafka.producer.fairyland;

import cn.com.sherhom.reno.boot.annonation.ToExplore;
import cn.com.sherhom.reno.common.utils.Asset;
import cn.com.sherhom.reno.kafka.common.entity.ProducerRunnerArgs;
import cn.com.sherhom.reno.kafka.common.entity.TopicSimpleInfo;
import cn.com.sherhom.reno.kafka.common.run.ProducerThread;
import cn.com.sherhom.reno.kafka.common.service.KfkOperate;
import cn.com.sherhom.reno.kafka.common.utils.KfkConf;

import java.util.ArrayList;
import java.util.List;

public class KafkaProducerFairyLand {
    public static final String topicFormat =
            "RENO_TEST_TOPIC_PARTITION_%s_%s";
    public static final String zkServer = KfkConf.getZkIp();

    @ToExplore
    public void entrance(int topicNum,
                         int partitionNum,
                         int producerNum,
                         int consumerNum,
                         int kbPerMsg,
                         int kbPerInput,
                         int kbPerOutput) {
        List<String> topics = new ArrayList<>();
        List<ProducerThread> proThreads = new ArrayList<>();
        String topic;
        TopicSimpleInfo topicSimpleInfo, param;
        //prepare topic
        for (int i = 0; i < topicNum; i++) {
            topic = String.format(topicFormat, partitionNum, i);
            prepareTopic(topic, partitionNum);
            topics.add(topic);
        }
        ProducerRunnerArgs proArgs;
        for (int i = 0; i < producerNum; i++) {
            proArgs = new ProducerRunnerArgs();
            proThreads.add(new ProducerThread(proArgs));
        }
    }

    public void prepareTopic(String topicName, int partitionNum) {
        TopicSimpleInfo topicSimpleInfo = KfkOperate.getTopicSimpleInfo(zkServer, topicName);
        TopicSimpleInfo param;
        if (topicSimpleInfo == null) {
            param = new TopicSimpleInfo();
            param.setTopicName(topicName);
            param.setPartNum(partitionNum);
            param.setReplicationNum(2);
            param.setZkServers(KfkConf.getZkIp());
            Asset.isTrue(KfkOperate.createTopic(param),
                    String.format("Topic[{}] create error.", topicName));
        } else if (topicSimpleInfo.getPartNum() < partitionNum) {
            Asset.isTrue(KfkOperate.expandPartitions(zkServer, topicName, partitionNum),
                    String.format("Fail to expand partition from {} to {}, for topic[{}]",
                            topicSimpleInfo.getPartNum(), partitionNum, topicName));
        }
    }
}
