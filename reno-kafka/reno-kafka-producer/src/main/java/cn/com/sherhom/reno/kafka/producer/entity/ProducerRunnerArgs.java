package cn.com.sherhom.reno.kafka.producer.entity;

import cn.com.sherhom.reno.kafka.common.record.Stat;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProducerRunnerArgs {
    private String topicName;
    private int recordSize;
    private long num;
    //msg/sec
    private int throughput;
    private Stat stat;
}
