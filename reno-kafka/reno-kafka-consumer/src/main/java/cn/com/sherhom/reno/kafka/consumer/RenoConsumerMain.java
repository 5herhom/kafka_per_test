package cn.com.sherhom.reno.kafka.consumer;

import cn.com.sherhom.reno.common.utils.ConfUtil;
import lombok.extern.slf4j.Slf4j;

import static cn.com.sherhom.reno.common.contants.ConfKey.ACTIVE_PROFILE_KEY;

@Slf4j
public class RenoConsumerMain {
    public static void main(String[] args) {
        log.info(ConfUtil.get("sherhom.test.port"));
        log.info(ConfUtil.get("sherhom.port"));
        log.info(ConfUtil.get(ACTIVE_PROFILE_KEY));
    }
}
