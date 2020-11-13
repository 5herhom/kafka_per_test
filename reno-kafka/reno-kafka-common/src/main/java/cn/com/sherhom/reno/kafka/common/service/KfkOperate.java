package cn.com.sherhom.reno.kafka.common.service;
import static cn.com.sherhom.reno.kafka.common.entity.BrokerLogDirInfo.*;
import cn.com.sherhom.reno.common.utils.Asset;
import cn.com.sherhom.reno.common.utils.CollectionUtils;
import cn.com.sherhom.reno.common.utils.LogUtil;
import cn.com.sherhom.reno.kafka.common.callback.ProducerCallback;
import cn.com.sherhom.reno.kafka.common.constants.ZkPathConst;
import cn.com.sherhom.reno.kafka.common.entity.BrokerLogDirInfo;
import cn.com.sherhom.reno.kafka.common.entity.TopicPartitionSize;
import cn.com.sherhom.reno.kafka.common.entity.TopicSimpleInfo;
import cn.com.sherhom.reno.kafka.common.holder.ZkCuratorHolder;
import cn.com.sherhom.reno.kafka.common.service.scala.KfkOperate4scala;
import cn.com.sherhom.reno.kafka.common.utils.MsgUtil;
import cn.com.sherhom.reno.kafka.common.utils.ThroughputThrottler;
import cn.com.sherhom.reno.kafka.common.utils.ZkUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import kafka.utils.ZkUtils;
import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.curator.framework.CuratorFramework;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.DescribeLogDirsResult;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.requests.DescribeLogDirsResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author Sherhom
 * @date 2020/9/4 10:12
 */
@Slf4j
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
    public static TopicPartitionSize getTopicPartitionSize(String zkServers,String topicName){
        List<TopicPartitionSize> list=getTopicPartitionSize(zkServers,e->e.getKey().equals(topicName));
        if(CollectionUtils.isEmpty(list))
                return null;
        return list.get(0);
    }
    public static List<TopicPartitionSize> getTopicPartitionSize(String zkServers){
        return getTopicPartitionSize(zkServers,e->true);
    }
    public static List<TopicPartitionSize> getTopicPartitionSize(String zkServers, Predicate<HashMap.Entry<String,Map<Integer,Long>>> predicate){
        CuratorFramework zkCli = ZkCuratorHolder.getZkCli(zkServers);
        String path=ZkPathConst.ZK_BROKER_PATH;
        Map<String, String> path2hostport = ZkUtil.getChildrenData(zkCli, path, value -> {
            JSONObject tmp = JSONObject.parseObject(value);
            return tmp.getString("host") + ":" + tmp.getString("port");
        });
        if(path2hostport==null)
            return null;
        List<Integer> brokerIdList=path2hostport.entrySet().stream().map(e->Integer.parseInt(e.getKey())).collect(Collectors.toList());
        List<String>kfkBrokers=path2hostport.entrySet().stream().map(e->e.getValue()).collect(Collectors.toList());
        List<BrokerLogDirInfo> brokerLogDirInfos=getBrokerLogDirInfo(String.join(",",kfkBrokers),brokerIdList);
        if(brokerLogDirInfos==null)
            return null;
        Map<String,Map<Integer,Long>>topicPartition2Size=new HashMap<>();
        Map<Integer,Long> partition2Size;
        long tmp;
        List<LogDirEntity> logDirEntityList;
        List<PartitionLogDirInfo> partitionLogDirInfoList;
        int partition;
        String topic;
        for (BrokerLogDirInfo brokerLogDirInfo:brokerLogDirInfos) {
            logDirEntityList=brokerLogDirInfo.getLogDirs();
            for(LogDirEntity logDirEntity:logDirEntityList){
                partitionLogDirInfoList=logDirEntity.getPartitions();
                for(PartitionLogDirInfo partitionLogDirInfo:partitionLogDirInfoList){
                    String topicPartition=partitionLogDirInfo.getPartition();
                    int separateIndex=topicPartition.lastIndexOf("-");
                    topic=topicPartition.substring(0,separateIndex);
                    partition=Integer.parseInt(topicPartition.substring(separateIndex+1));
                    partition2Size=topicPartition2Size.get(topic);
                    if(partition2Size==null){
                        partition2Size=new HashMap<>();
                        topicPartition2Size.put(topic,partition2Size);
                    }
                    tmp=partition2Size.getOrDefault(partition,0l);
                    partition2Size.put(partition,tmp+partitionLogDirInfo.getSize());
                }
            }
        }
        return topicPartition2Size.entrySet().stream().filter(predicate).map(entry->new TopicPartitionSize(entry.getKey(),entry.getValue())).collect(Collectors.toList());
    }
    public static List<BrokerLogDirInfo> getBrokerLogDirInfo(String brokerListStr, List<Integer> brokerIdList) {
        Properties props=new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG,brokerListStr);
        props.put(AdminClientConfig.CLIENT_ID_CONFIG,"getTopicPartitionSize-tool");
        AdminClient adminClient=AdminClient.create(props);
        DescribeLogDirsResult describeLogDirsResult=adminClient.describeLogDirs(brokerIdList);
        try {
            Map<Integer, Map<String, DescribeLogDirsResponse.LogDirInfo>> integerMapMap = describeLogDirsResult.all().get();
            return BrokerLogDirInfo.map2list(integerMapMap);
        } catch (InterruptedException e) {
            LogUtil.printStackTrace(e);
            return null;
        } catch (ExecutionException e) {
            LogUtil.printStackTrace(e);
            return null;
        }
        finally {
            adminClient.close();
        }
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

    public static boolean clearTopicData(String zkServers, String topicName){
        log.info("Start to clean.");
        StopWatch sw=new StopWatch();
        sw.start();
        TopicSimpleInfo topicSimpleInfo = KfkOperate.getTopicSimpleInfo(zkServers, topicName);
        if(topicSimpleInfo==null)
            return false;
        Properties properties=KfkOperate.descConfig(topicSimpleInfo);
        long retenMs=-1l;
        if(properties!=null||!properties.contains("retention.ms")){
            retenMs= Long.valueOf(properties.getProperty("retention.ms","-1"));
        }
        //清空topic数据
        Properties paramProperties=new Properties();
        paramProperties.setProperty("retention.ms","0");
        KfkOperate.alterConfig(topicSimpleInfo,paramProperties);
        TopicPartitionSize topicPartitionSize=getTopicPartitionSize(zkServers,topicName);
        long startTime=System.currentTimeMillis();
        long overTime=10000;
        int maxRetry=10000;
        int n=0;
        //检测是否清理
        while(!topicPartitionSize.isTopicEmpty()&&n<maxRetry){
            if(overTime<=System.currentTimeMillis()-startTime){
                paramProperties.setProperty("retention.ms","0");
                KfkOperate.alterConfig(topicSimpleInfo,paramProperties);
                KfkOperate.alterConfig(topicSimpleInfo,paramProperties);
                startTime=System.currentTimeMillis();
                try {
                    Thread.sleep(5000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                n++;
            }
            topicPartitionSize=getTopicPartitionSize(zkServers,topicName);
        }
        sw.stop();
        log.info("Clean cost:{} ms",sw.getTime());
        //设置回来
        paramProperties.setProperty("retention.ms",String.valueOf(retenMs));
        if(retenMs!=-1)
            KfkOperate.alterConfig(topicSimpleInfo,paramProperties);
        return topicPartitionSize.isTopicEmpty();
    }
}
