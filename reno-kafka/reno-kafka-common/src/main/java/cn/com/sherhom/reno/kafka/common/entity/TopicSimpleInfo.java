package cn.com.sherhom.reno.kafka.common.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Sherhom
 * @date 2020/9/4 10:23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopicSimpleInfo {
    protected String kfkCluster;
    protected String zkServers;
    protected String topicName;
    protected int partNum;
    protected int replicationNum;

    @Override
    public String toString(){
        return JSONObject.toJSONString(this);
    }
}
