package cn.com.sherhom.reno.http.common.utils;

import cn.com.sherhom.reno.common.utils.ConfUtil;

/**
 * @author Sherhom
 * @date 2020/10/28 11:20
 */
public class HttpConfUtil {
    public static final String TARGET_HTTP_URL_KEY ="reno.http.target.url";
    public static final String TARGET_HTTP_PARAM_KEY="reno.http.json.param";
    public static final String REPORT_PATH_KEY="reno.http.report.path";
    public static String targetHttpUrl(){
        return ConfUtil.get(TARGET_HTTP_URL_KEY);
    }
    public static String targetHttpParam(){
        return ConfUtil.get(TARGET_HTTP_PARAM_KEY);
    }
    public static String reportPath(){
        return ConfUtil.get(REPORT_PATH_KEY,"/data/reno/http/multiThread/report");
    }
}
