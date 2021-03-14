package cn.com.sherhom.reno.kafka.common.service.scala

import java.util.Properties

import cn.com.sherhom.reno.common.exception.RenoException
import kafka.admin.{AdminUtils, RackAwareMode}
import kafka.server.ConfigType
import kafka.utils.ZkUtils
import org.slf4j.LoggerFactory

object KfkOperate4scala {
  val logger=LoggerFactory.getLogger(KfkOperate4scala.getClass);
  def addPartition(topicName: String, zkUtils: ZkUtils, nPartitions: Int): Boolean = {
    val configs = AdminUtils.fetchEntityConfig(zkUtils, ConfigType.Topic, topicName)
    val existingAssignment = zkUtils.getReplicaAssignmentForTopics(List(topicName)).map {
      case (topicPartition, replicas) => topicPartition.partition -> replicas
    }
    if (existingAssignment.isEmpty)
      throw new RenoException(s"Topic [$topicName] does not exist.")
    if (existingAssignment.size >= nPartitions)
      return false
    val allBrokers = AdminUtils.getBrokerMetadatas(zkUtils)
    AdminUtils.addPartitions(zkUtils, topicName, existingAssignment, allBrokers, nPartitions)
    true
  }
  def addTopic(topic:String,zkUtils:ZkUtils,partitions:Int,configs:Properties):Boolean={
    val rackAwareMode=RackAwareMode.Disabled
    AdminUtils.createTopic(zkUtils,topic,partitions,2,configs,rackAwareMode)
    true
  }
  def addTopic(topic:String,zkUtils:ZkUtils,partitions:Int,replicas:Int,configs:Properties):Boolean={
    val rackAwareMode=RackAwareMode.Disabled
    AdminUtils.createTopic(zkUtils,topic,partitions,replicas,configs,rackAwareMode)
    true
  }
  def alterConfig(topic:String,zkUtils: ZkUtils,configsTobeAdded:Properties):String={
    val configs=AdminUtils.fetchEntityConfig(zkUtils,ConfigType.Topic,topic);
    val it=configsTobeAdded.keySet().iterator();
    while(it.hasNext){
      val key= it.next()
      configs.put(key,configsTobeAdded.get(key));
    }
    AdminUtils.changeTopicConfig(zkUtils,topic,configs)
    logger.info("Updated config for topic \"%s\".".format(topic));
    return "true"
  }
  def descConfig(topic:String,zkUtils: ZkUtils):Properties={
    val configs=AdminUtils.fetchEntityConfig(zkUtils,ConfigType.Topic,topic)
    configs
  }
}
