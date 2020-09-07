package cn.com.sherhom.reno.kafka.common.net;

/**
 * @author Sherhom
 * @date 2020/9/7 15:07
 */
public interface RequestHandler {
    Object handle(String msg);
}
