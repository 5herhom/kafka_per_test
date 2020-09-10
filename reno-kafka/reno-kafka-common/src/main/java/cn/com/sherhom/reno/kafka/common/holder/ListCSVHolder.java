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
    ).collect(Collectors.toList()),"\t");


    public static final ListCSVLine resultCsvLine=new ListCSVLine(Stream.of(
            new Pair<>("topicNum", "topicNum"),
            new Pair<>("partitionNum", "partitionNum"),
            new Pair<>("producerNum(per topic)", "producerNum"),
            new Pair<>("consumerNum(per topic)", "consumerNum"),
            new Pair<>("bytePerMsg", "bytePerMsg"),
            new Pair<>("expectInput(byte/sec)", "bytePerSecInput"),
            new Pair<>("expectOutput(byte/sec)", "bytePerSecOutput"),
            new Pair<>("actualInput(byte/sec)", "actualInput"),
            new Pair<>("inputDiff(byte/sec)", "inputDiff"),
            new Pair<>("inputDiffPercent(%)", "inputDiffPercent"),
            new Pair<>("actualOutput(byte/sec)", "actualOutput"),
            new Pair<>("outputDiff(byte/sec)", "outputDiff"),
            new Pair<>("outputDiffPercent(%)", "outputDiffPercent"),
            new Pair<>("success", "success"),
            new Pair<>("detailLogPath", "detailLogPath")
    ).collect(Collectors.toList()),"\t");
}
