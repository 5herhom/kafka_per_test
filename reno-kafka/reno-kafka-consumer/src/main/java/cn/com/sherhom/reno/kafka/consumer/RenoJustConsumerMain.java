package cn.com.sherhom.reno.kafka.consumer;

import cn.com.sherhom.reno.common.utils.ConfUtil;
import lombok.extern.slf4j.Slf4j;

import static cn.com.sherhom.reno.common.contants.ConfKey.ACTIVE_PROFILE_KEY;
/*
* 样例维度：1.topic数量；2.partition大小；3.一个topic不同consumer的数量；4.一个consumer和其partition的匹配度；4.message大小；
* 结果：1.总数量；2.总byte；3.总耗时；4.条数速度；5.大小速度
* 1.修改topic数据的保存时间为0ms
* 2.检测topic大小直到0，再把保存时间设置回去
* 3.发送数据到对应topic，注意限流，保证数据都发进去，如果有失败，就从头再来
* 4.启动消费
* */
@Slf4j
public class RenoJustConsumerMain {
    public static void main(String[] args) {
        log.info(ConfUtil.get("sherhom.test.port"));
        log.info(ConfUtil.get("sherhom.port"));
        log.info(ConfUtil.get(ACTIVE_PROFILE_KEY));
    }
}
