package cn.com.sherhom.reno.common.mapper;

import cn.com.sherhom.reno.common.entity.Pair;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Sherhom
 * @date 2021/2/7 14:59
 */
public class CsvMapper {
    public static final List<Pair<String, String>> DEFAULT_TIME_COST_CSV_MAPPER = Stream.of(
            new Pair<>("threadNum", "threadNum"),
            new Pair<>("requestTotal", "requestTotal"),
            new Pair<>("successNum", "successNum"),
            new Pair<>("failNum", "failNum"),
            new Pair<>("avgMs", "avgMs"),
            new Pair<>("maxMs", "maxMs"),
            new Pair<>("minMs", "minMs")
    ).collect(Collectors.toList());
}
