package cn.com.sherhom.reno.kafka.common.service.scala

import java.util.Properties

import cn.com.sherhom.reno.common.exception.RenoException
import kafka.admin.{AdminUtils, RackAwareMode}
import kafka.server.ConfigType
import kafka.utils.ZkUtils

object KfkOperate4scala {
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
}
