package cn.com.sherhom.reno.kafka.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.requests.DescribeLogDirsResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Sherhom
 * @date 2020/11/9 18:09
 */
@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BrokerLogDirInfo {
    protected Integer broker;
    protected List<LogDirEntity> logDirs;

    static public List<BrokerLogDirInfo> map2list(Map<Integer, Map<String, DescribeLogDirsResponse.LogDirInfo>> map) {
        return
                map.entrySet().stream().map(entry -> {
                    int broker = entry.getKey();
                    Map<String, DescribeLogDirsResponse.LogDirInfo> logDirInfos = entry.getValue();
                    BrokerLogDirInfo brokerLogDirInfo = new BrokerLogDirInfo();
                    brokerLogDirInfo.setBroker(broker);
                    List<LogDirEntity> logDirs = logDirInfos.entrySet().stream().map(
                            logDirInfo -> {
                                LogDirEntity logDirEntity = new LogDirEntity();
                                logDirEntity.setLogDir(logDirInfo.getKey());
                                logDirEntity.setError(logDirInfo.getValue().error.message());
                                List<PartitionLogDirInfo> partitions = logDirInfo.getValue().replicaInfos.entrySet().stream().map(
                                        replicaInfo -> new PartitionLogDirInfo(replicaInfo.getKey().topic() + "-" + replicaInfo.getKey().partition(),
                                                replicaInfo.getValue().size,
                                                replicaInfo.getValue().offsetLag,
                                                replicaInfo.getValue().isFuture
                                        )
                                ).collect(Collectors.toList());
                                logDirEntity.setPartitions(partitions);
                                return logDirEntity;
                            }
                    ).collect(Collectors.toList());
                    brokerLogDirInfo.setLogDirs(logDirs);
                    return brokerLogDirInfo;
                }).collect(Collectors.toList());
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PartitionLogDirInfo {
        protected String partition;
        protected long size;
        protected long offsetLag;
        protected boolean isFuture;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LogDirEntity {
        protected String logDir;
        protected String error;
        protected List<PartitionLogDirInfo> partitions;
    }
}
