package cn.com.sherhom.reno.kafka.common.service;

import cn.com.sherhom.reno.common.utils.Asset;
import cn.com.sherhom.reno.kafka.common.callback.ProducerCallback;
import cn.com.sherhom.reno.kafka.common.constants.ZkPathConst;
import cn.com.sherhom.reno.kafka.common.entity.TopicSimpleInfo;
import cn.com.sherhom.reno.kafka.common.holder.ZkCuratorHolder;
import cn.com.sherhom.reno.kafka.common.service.scala.KfkOperate4scala;
import cn.com.sherhom.reno.kafka.common.utils.MsgUtil;
import cn.com.sherhom.reno.kafka.common.utils.ThroughputThrottler;
import cn.com.sherhom.reno.kafka.common.utils.ZkUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import kafka.utils.ZkUtils;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;
import org.apache.curator.framework.CuratorFramework;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;

/**
 * @author Sherhom
 * @date 2020/9/4 10:12
 */
public class KfkOperate {
    public static TopicSimpleInfo getTopicSimpleInfo(String zkServers, String topicName) {
        CuratorFramework zkCli = ZkCuratorHolder.getZkCli(zkServers);
        String path = ZkPathConst.getZkTopicDetailPath(topicName);
        String topic2partStr = ZkUtil.getData(zkCli, path);
        if(topic2partStr==null)
            return null;
        JSONObject t2p = JSON.parseObject(topic2partStr);
        TopicSimpleInfo topicSimpleInfo = new TopicSimpleInfo();
        topicSimpleInfo.setTopicName(topicName);
        topicSimpleInfo.setZkServers(zkServers);
        topicSimpleInfo.setPartNum(t2p.getJSONObject("partitions").size());
        topicSimpleInfo.setReplicationNum(t2p.getJSONObject("partitions").getJSONArray("0").size());
        return topicSimpleInfo;
    }

    public static boolean expandPartitions(String zkServers, String topicName, int newNum) {
        ZkClient zkClient = ZkUtils.createZkClient(zkServers, 10 * 1000, 8 * 1000);
        ZkUtils zkUtils = new ZkUtils(zkClient, new ZkConnection(zkServers), false);
        return KfkOperate4scala.addPartition(topicName,zkUtils,newNum);
    }

    public static boolean createTopic(TopicSimpleInfo simpleInfo, Properties config){
        ZkClient zkClient=ZkUtils.createZkClient(simpleInfo.getZkServers(),10*1000,8*1000);
        ZkUtils zkUtils=new ZkUtils(zkClient,new ZkConnection(simpleInfo.getZkServers()),false);
        KfkOperate4scala.addTopic(simpleInfo.getTopicName(),zkUtils,simpleInfo.getPartNum(),
                simpleInfo.getReplicationNum(),config);
        zkUtils.close();
        return true;
    }
    public static boolean createTopic(TopicSimpleInfo simpleInfo){
        return createTopic(simpleInfo,new Properties());
    }
    public static boolean alterConfig(TopicSimpleInfo simpleInfo,Properties config){
        ZkClient zkClient=ZkUtils.createZkClient(simpleInfo.getZkServers(),10*1000,8*1000);
        ZkUtils zkUtils=new ZkUtils(zkClient,new ZkConnection(simpleInfo.getZkServers()),false);
        KfkOperate4scala.alterConfig(simpleInfo.getTopicName(),zkUtils,config);
        zkUtils.close();
        return true;
    }
    public static Properties descConfig(TopicSimpleInfo simpleInfo){
        ZkClient zkClient=ZkUtils.createZkClient(simpleInfo.getZkServers(),10*1000,8*1000);
        ZkUtils zkUtils=new ZkUtils(zkClient,new ZkConnection(simpleInfo.getZkServers()),false);
        Properties config=KfkOperate4scala.descConfig(simpleInfo.getTopicName(),zkUtils);
        return config;
    }
}
