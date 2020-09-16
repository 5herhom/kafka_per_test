package cn.com.sherhom.reno.common.utils;

import javax.xml.crypto.Data;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Sherhom
 * @date 2020/9/9 17:43
 */
public class DateUtil {
    private static volatile Map<String, DateFormat> formatPool = new ConcurrentHashMap<>();

    public static DateFormat getSimpleDateFormat(String str) {
        DateFormat dateFormat = formatPool.get(str);
        if (dateFormat == null) {
            synchronized (formatPool) {
                if (dateFormat == null) {
                    dateFormat = formatPool.get(str);
                    dateFormat = new SimpleDateFormat(str);
                }
            }
        }
        return dateFormat;
    }
    public static String date2String(Date d,String formatStr){
        return getSimpleDateFormat(formatStr).format(d);
    }
    public static String date2String(Date d){
        return date2String(d,"yyyy-MM-dd_HH-mm-ss");
    }
}
