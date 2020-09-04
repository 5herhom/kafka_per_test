package cn.com.sherhom.reno.common.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author Sherhom
 * @date 2020/9/4 11:34
 */
public class ExceptionUtil {
    public static String toStackTraceString(Throwable e){
        if(e==null)
            return "";
        StringWriter stringWriter=new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }
}
