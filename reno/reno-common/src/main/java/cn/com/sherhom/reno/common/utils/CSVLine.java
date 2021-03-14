package cn.com.sherhom.reno.common.utils;

import com.alibaba.fastjson.JSONObject;

public interface CSVLine {
    String getLine(Object o);
    String getLine(JSONObject o);
    String getHeader();
}
