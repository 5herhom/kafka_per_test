package cn.com.sherhom.reno.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;
/**
 * @author Sherhom
 * @date 2020/9/2 20:12
 */
@Slf4j
public class ConfUtil {

    static volatile Properties properties;
    public static String get(String key,String defaultValue){
        if(properties==null){
            ConfLoader.load();
        }
        return properties.getProperty(key,defaultValue);
    }
    public static String get(String key){
        return get(key,null);
    }

}
