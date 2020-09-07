package cn.com.sherhom.reno.kafka.common.net;

import cn.com.sherhom.reno.kafka.common.net.constants.NetConstants;

/**
 * @author Sherhom
 * @date 2020/9/7 15:10
 */
public class RenoRequestHandler implements RequestHandler {
    RequestMapper mapper;

    public RenoRequestHandler(RequestMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Object handle(String msg) {
        int methodIndex = msg.indexOf(NetConstants.REQUEST_SEPARATOR);
        String methodStr = msg.substring(0, methodIndex);
        int paramStart = methodIndex + 1;
        String paramStr = msg.substring(paramStart);
        JSONRequestMethod method = mapper.mapping(methodStr);
        return method.doMethod(paramStr);
    }
}
