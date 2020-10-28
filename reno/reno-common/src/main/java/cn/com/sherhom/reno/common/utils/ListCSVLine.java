package cn.com.sherhom.reno.common.utils;

import cn.com.sherhom.reno.common.entity.Pair;
import cn.com.sherhom.reno.common.utils.AbstractCSVLine;
import cn.com.sherhom.reno.common.utils.CSVLine;
import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Sherhom
 * @date 2020/9/8 18:10
 */
public class ListCSVLine extends AbstractCSVLine implements CSVLine {
    public final List<Pair<String, String>> mapper;

    public ListCSVLine(String separator) {
        super(separator);
        this.mapper=Stream.of(
                new Pair<>("total_Bytes(byte)", "totalBytes"),
                new Pair<>("byte_per_sec(byte/sec)", "bytePerSec"),
                new Pair<>("total_msg", "totalCount"),
                new Pair<>("msg_per_sec", "countPerSec"),
                new Pair<>("total_latency", "totalLatency"),
                new Pair<>("max_latency", "maxLatency")
        ).collect(Collectors.toList());
    }
    public ListCSVLine( List<Pair<String, String>> mapper, String separator) {
        super(separator);
        this.mapper=mapper;
    }
    @Override
    public String getLine(Object o) {
        Class clazz = o.getClass();
        StringBuilder sb = new StringBuilder();
        mapper.forEach(pair -> {
            Field field;
            try {
                field = clazz.getDeclaredField(pair.getValue());
                field.setAccessible(true);
                Object value = field.get(o);
                separateLink(sb, value);
            } catch (NoSuchFieldException | IllegalAccessException e) {
            }
        });
        return sb.length()>2?sb.substring(0,sb.length()-1):sb.toString();
    }
    @Override
    public String getLine(JSONObject o) {
        StringBuilder sb = new StringBuilder();
        mapper.forEach(pair -> {
            Object value = o.getString(pair.getValue());
            separateLink(sb, value);
        });
        return sb.length()>2?sb.substring(0,sb.length()-1):sb.toString();
    }
    @Override
    public String getHeader() {
        StringBuilder sb=new StringBuilder();
        mapper.forEach(
                pair->separateLink(sb,pair.getKey())
        );
        return sb.length()>2?sb.substring(0,sb.length()-1):sb.toString();
    }

}
