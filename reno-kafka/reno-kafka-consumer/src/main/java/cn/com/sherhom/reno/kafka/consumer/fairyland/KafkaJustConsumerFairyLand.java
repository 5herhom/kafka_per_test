package cn.com.sherhom.reno.kafka.consumer.fairyland;

import cn.com.sherhom.reno.boot.annonation.ToExplore;
import cn.com.sherhom.reno.common.exception.RenoException;
import cn.com.sherhom.reno.common.utils.Asset;
import cn.com.sherhom.reno.common.utils.LogUtil;
import cn.com.sherhom.reno.kafka.common.entity.TopicSimpleInfo;
import cn.com.sherhom.reno.kafka.common.service.KfkOperate;
import cn.com.sherhom.reno.kafka.common.utils.KfkConf;

import java.util.Properties;

/**
 * @author Sherhom
 * @date 2020/11/9 16:09
 */
public class KafkaJustConsumerFairyLand {
    public static final String topicFormat =
            "RENO_TEST_TOPIC_PARTITION_CONSUMER_SINGLE_%s_%s";
    public static final String zkServer = KfkConf.zkIp();
    @ToExplore
    void entrance(int topicNum,int partitionNum,int groupPerTopic,int clientPerGroup,long messageSize){

    }
//    public void prepareTopic(String topicName, int partitionNum) {
//        try{
//            TopicSimpleInfo topicSimpleInfo = KfkOperate.getTopicSimpleInfo(zkServer, topicName);
//            TopicSimpleInfo param;
//            if (topicSimpleInfo == null) {
//                param = new TopicSimpleInfo();
//                param.setTopicName(topicName);
//                param.setPartNum(partitionNum);
//                param.setReplicationNum(2);
//                param.setZkServers(KfkConf.zkIp());
//                Asset.isTrue(KfkOperate.createTopic(param),
//                        String.format("Topic[{}] create error.", topicName));
//                topicSimpleInfo = KfkOperate.getTopicSimpleInfo(zkServer, topicName);
//
//            } else if (topicSimpleInfo.getPartNum() < partitionNum) {
//                Asset.isTrue(KfkOperate.expandPartitions(zkServer, topicName, partitionNum),
//                        String.format("Fail to expand partition from {} to {}, for topic[{}]",
//                                topicSimpleInfo.getPartNum(), partitionNum, topicName));
//            }
//            Properties properties=KfkOperate.descConfig(topicSimpleInfo);
//            if(properties==null||!properties.contains("retention.ms")||
//                    Long.valueOf(properties.getProperty("retention.ms"))!=retenMs){
//                Properties paramProperties=new Properties();
//                paramProperties.setProperty("retention.ms",String.valueOf(retenMs));
//                KfkOperate.alterConfig(topicSimpleInfo,paramProperties);
//                log.info("Alter topic {} 's retention ms to {}",topicName,retenMs);
//            }
//        }
//        catch (Exception e){
//            LogUtil.printStackTrace(e);
//            throw new RenoException(e);
//        }
//
//    }
}
