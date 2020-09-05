package cn.com.sherhom.reno.kafka.common;

import cn.com.sherhom.reno.kafka.common.service.KfkOperate;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author Sherhom
 * @date 2020/9/4 14:50
 */
@Slf4j
public class CommonTest {
    @Test
    public void topicSimpleInfoTest(){
        String zkServers="47.96.65.208:2181,150.158.119.99:2181";
        log.info(KfkOperate.getTopicSimpleInfo(zkServers,
                "HZY_TEST").toString());
    }
    @Test
    public void addPartitionTest(){
        String zkServers="47.96.65.208:2181,150.158.119.99:2181";
        String topicName="HZY_TEST";
        log.info("Result:{}",KfkOperate.expandPartitions(zkServers,topicName,7));
    }
}
