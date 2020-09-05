package cn.com.sherhom.reno.kafka.producer;

import cn.com.sherhom.reno.common.utils.ConfUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RenoProducerMain {
    public static void main(String[] args) {

        log.info(ConfUtil.get("sherhom.test.port"));
    }
}
