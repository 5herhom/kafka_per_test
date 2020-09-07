package cn.com.sherhom.reno.kafka.consumer.bean;

import cn.com.sherhom.reno.kafka.common.net.RenoRequestHandler;
import cn.com.sherhom.reno.kafka.common.net.RequestHandler;
import cn.com.sherhom.reno.kafka.common.net.RequestMapper;
import cn.com.sherhom.reno.kafka.consumer.mapper.ConsumerRequestMapper;

/**
 * @author Sherhom
 * @date 2020/9/7 15:54
 */
public class MapperHolder {
    public static final RequestMapper requestMapper=new ConsumerRequestMapper();
    public static final RequestHandler requestHandler=new RenoRequestHandler(requestMapper);
}
