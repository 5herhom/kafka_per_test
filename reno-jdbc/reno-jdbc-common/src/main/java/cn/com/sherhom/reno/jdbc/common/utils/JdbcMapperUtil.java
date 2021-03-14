package cn.com.sherhom.reno.jdbc.common.utils;

import cn.com.sherhom.reno.common.entity.Pair;

import java.util.ArrayList;
import java.util.List;

import static cn.com.sherhom.reno.common.mapper.CsvMapper.DEFAULT_TIME_COST_CSV_MAPPER;
import static cn.com.sherhom.reno.jdbc.common.constants.ReportHeads.*;

/**
 * @author Sherhom
 * @date 2021/2/7 15:37
 */
public class JdbcMapperUtil {
    public static List<Pair<String, String>> getJdbcTimeCostMapper() {
        List<Pair<String, String>> jdbcTimeCostMapper = new ArrayList<>();
        jdbcTimeCostMapper.add(new Pair<>(SQL_NAME, SQL_NAME));
        jdbcTimeCostMapper.add(new Pair<>(SQL, SQL));
        jdbcTimeCostMapper.add(new Pair<>("total_row_num", ROW_NUM_IN_MAIN_TABLE));
        jdbcTimeCostMapper.addAll(DEFAULT_TIME_COST_CSV_MAPPER);
        return jdbcTimeCostMapper;
    }
}
