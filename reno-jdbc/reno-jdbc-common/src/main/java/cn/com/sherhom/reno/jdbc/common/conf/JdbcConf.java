package cn.com.sherhom.reno.jdbc.common.conf;

import cn.com.sherhom.reno.common.utils.ConfUtil;

/**
 * @author Sherhom
 * @date 2021/2/7 11:21
 */
public class JdbcConf {
    public static final String JDBC_DRIVER_KEY="jdbc.driver";
    public static final String JDBC_URL_KEY="jdbc.url";
    public static final String JDBC_USERNAME_KEY="jdbc.username";
    public static final String JDBC_PASSWORD_KEY="jdbc.password";
    public static final String JDBC_QUERY_TOTAL_KEY ="jdbc.query.total";
    public static final String JDBC_REPORT_PATH_KEY="jdbc.report.path";
    public static String getJdbcDriver(){
        return ConfUtil.get(JDBC_DRIVER_KEY);
    }
    public static String getJdbcUrl(){
        return ConfUtil.get(JDBC_URL_KEY);
    }
    public static String getJdbcUsername(){
        return ConfUtil.get(JDBC_USERNAME_KEY);
    }
    public static String getJdbcPassword(){
        return ConfUtil.get(JDBC_PASSWORD_KEY);
    }
    public static long getQueryTotal(){
        return ConfUtil.getLong(JDBC_QUERY_TOTAL_KEY,500);
    }


    public static final String DEFAULT_REPORT_PATH="/data/reno/jdbc/simple/report";
    public static String getReportPath(){
        return ConfUtil.get(JDBC_REPORT_PATH_KEY,DEFAULT_REPORT_PATH);
    }
    public static final String DEFAULT_REPORT_FILENAME="reno_jdbc_multi_thread_result";
}
