package cn.com.sherhom.reno.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class ConfUtil {
    static private volatile Properties properties;
    static private final List<String> CONFIG_DIR_PATH = Stream.of(
            ".","config/"
    ).collect(Collectors.toList());
    static private final String FILE_NAME="application";
    static private final String FILE_SUFFIX=".properties";
    static private final String BASE_PATH=System.getProperty("reno.config.path","/");
    private static final String ACTIVE_PROFILE_KEY="sherhom.profiles.active";
    private static synchronized void load(){
        if(properties!=null)
            return;
        properties=new Properties();
        load(FILE_NAME+FILE_SUFFIX);
        String activeProfile=System.getProperty("sherhom.profiles.active","office");
        load(FILE_NAME+"-"+activeProfile+FILE_SUFFIX);
    }
    private static void load(String fileName){
        String path;
        Properties pros=new Properties();
        for(String dir: CONFIG_DIR_PATH){
            InputStream in= null;
            try {
                path=StringUtils.cleanPath(BASE_PATH+"/"+dir+"/"+fileName);
//                path=StringUtils.cleanPath("/application.properties");
                InputStream inputStream=ConfUtil.class.getResourceAsStream(path);
                if(inputStream==null){
                    log.info("Config file is not existed in [{}]",path);
                    continue;
                }
//                in = new FileInputStream(inputStream);
                pros.load(inputStream);
                log.info("Config is found in {}",path);
                break;
            } catch (FileNotFoundException e) {
                log.warn(e.getMessage());
            } catch (IOException e) {
                log.warn(e.getMessage());
            }
            finally {
                IOUtils.closeQuietly(in);
            }
        }
        if(pros==null){
            log.error("Config file [{}] is found.",fileName);
            SystemUtil.exit();
        }
        if(properties==null){
            properties=new Properties();
        }
        properties.putAll(pros);
    }
    public static String get(String key,String defaultValue){
        if(properties==null){
            load();
        }
        return properties.getProperty(key,defaultValue);
    }
    public static String get(String key){
        return get(key,null);
    }

}
