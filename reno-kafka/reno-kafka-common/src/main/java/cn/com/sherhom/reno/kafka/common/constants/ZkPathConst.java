package cn.com.sherhom.reno.kafka.common.constants;

public class ZkPathConst {
    public static final String ZK_TOPIC_PATH="/brokers/topics";
    public static final String ZK_TOPIC_DETAIL_PATH="/brokers/topics/{0}";
    public static final String ZK_TOPIC_PARTITION_PATH="/brokers/topics/{0}/partitions/{1,number,#}/state";
}
