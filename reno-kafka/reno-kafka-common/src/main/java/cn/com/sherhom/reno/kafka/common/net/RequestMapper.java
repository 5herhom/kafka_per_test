package cn.com.sherhom.reno.kafka.common.net;

public interface RequestMapper {
    JSONRequestMethod mapping(String methodStr);
}
