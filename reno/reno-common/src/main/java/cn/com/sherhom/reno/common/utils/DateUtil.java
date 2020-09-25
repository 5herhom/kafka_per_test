package cn.com.sherhom.reno.common.utils;

import javax.xml.crypto.Data;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Sherhom
 * @date 2020/9/9 17:43
 */
public class DateUtil {
    private static final ThreadLocal<Map<String, DateFormat>> formatPool = new ThreadLocal<>();

    public static DateFormat getSimpleDateFormat(String formatStr) {
        if(formatPool.get()==null)
            formatPool.set(new HashMap<>());
        if (formatPool.get().containsKey(formatStr)) {
            formatPool.get().put(formatStr,new SimpleDateFormat(formatStr));
        }
        return formatPool.get().get(formatStr);
    }
    public static String date2String(Date d,String formatStr){
        return getSimpleDateFormat(formatStr).format(d);
    }
    public static String date2String(Date d){
        return date2String(d,"yyyy-MM-dd_HH-mm-ss");
    }
}
