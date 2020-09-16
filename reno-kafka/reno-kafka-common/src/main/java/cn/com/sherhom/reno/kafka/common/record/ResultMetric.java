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
    Long bytePerSecInput;
    Long bytePerSecOutput;
    Long actualInput;
    Long inputDiff;
    Double inputDiffPercent;
    Long actualOutput;
    Long outputDiff;
    Double outputDiffPercent;
    Long executeTime;
    Boolean success;
    String detailLogPath;
}
