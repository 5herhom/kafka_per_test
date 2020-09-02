package cn.com.sherhom.reno.kafka.consumer;

import cn.com.sherhom.reno.common.ConfUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RenoConsumerMain {
    public static void main(String[] args) {
        log.info(ConfUtil.get("sherhom.test.port"));
        log.info(ConfUtil.get("sherhom.port"));
    }
}
