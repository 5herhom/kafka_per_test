package cn.com.sherhom.reno.kafka.common.holder;

import cn.com.sherhom.reno.common.entity.Pair;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Sherhom
 * @date 2020/9/8 20:10
 */
public class ListCSVHolder {
    public static final ListCSVLine metricCsvLine=new ListCSVLine(Stream.of(
            new Pair<>("total_Bytes(byte)", "totalBytes"),
            new Pair<>("byte_per_sec(byte/sec)", "bytePerSec"),
            new Pair<>("total_msg", "totalCount"),
            new Pair<>("msg_per_sec", "countPerSec"),
            new Pair<>("total_latency", "totalLatency"),
            new Pair<>("max_latency", "maxLatency")
    ).collect(Collectors.toList())," ");
}
