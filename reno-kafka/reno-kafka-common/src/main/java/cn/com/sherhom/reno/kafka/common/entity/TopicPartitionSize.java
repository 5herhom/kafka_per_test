package cn.com.sherhom.reno.kafka.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Sherhom
 * @date 2020/11/9 17:34
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopicPartitionSize {
    protected String topicName;
    protected Map<Integer,Long> partition2Size=new HashMap<>();
}
