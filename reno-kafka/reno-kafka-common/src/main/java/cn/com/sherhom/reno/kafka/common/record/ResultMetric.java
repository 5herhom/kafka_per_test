package cn.com.sherhom.reno.kafka.common.record;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Sherhom
 * @date 2020/9/9 19:19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultMetric {
    Integer topicNum;
    Integer partitionNum;
    Integer producerNum;
    Integer consumerNum;
    Integer bytePerMsg;
    Integer bytePerSecInput;
    Integer bytePerSecOutput;
    Integer actualInput;
    Integer inputDiff;
    Integer actualOutput;
    Integer outputDiff;
    Boolean success;
}
