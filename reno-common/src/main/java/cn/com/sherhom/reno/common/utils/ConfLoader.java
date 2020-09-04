package cn.com.sherhom.reno.common.utils;


import cn.com.sherhom.reno.common.loader.URLLoader;
import cn.com.sherhom.reno.common.loader.URLLoaderFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static cn.com.sherhom.reno.common.contants.ConfKey.*;
import static cn.com.sherhom.reno.common.contants.ProtocolType.CLASSPATH_PROTOCOL;
/**
 * @author Sherhom
 * @date 2020/9/2 20:12
 */
@Slf4j
public class ConfLoader {
    static private final List<String> CONFIG_DIR_PATH = Stream.of(
            ".","config/"
    ).collect(Collectors.toList());
    private static final String FILE_NAME="application";
    private static final String FILE_SUFFIX=".properties";
    private static final String BASE_PATH=System.getProperty(BASE_PATH_KEY,"/");
    private static final String CONFIG_FILE_PROTOCOL=System.getProperty(FILE_PROTOCOL_KEY,CLASSPATH_PROTOCOL);

    public static void load(){
        if(ConfUtil.properties!=null)
            return;
        synchronized (ConfUtil.class){
            if(ConfUtil.properties!=null)
                return;
            ConfUtil.properties=new Properties();
            ConfUtil.properties.putAll(System.getProperties());
            load(FILE_NAME+FILE_SUFFIX);
            String activeProfile=System.getProperty(ACTIVE_PROFILE_KEY,"office");
            load(FILE_NAME+"-"+activeProfile+FILE_SUFFIX);
        }
    }
    static void load(String fileName){
        String path;
        URLLoader urlLoader;
        Properties pros=new Properties();
        for(String dir: CONFIG_DIR_PATH){
            InputStream in= null;
            try {
                path=StringUtils.cleanPath(
                        StringUtils.dirPathEnd(BASE_PATH)+
                                StringUtils.dirPathEnd(dir)+
                                fileName);
                urlLoader= URLLoaderFactory.getInstance(CONFIG_FILE_PROTOCOL,path);
                InputStream inputStream=urlLoader.getInStream();
                if(inputStream==null){
                    log.info("Config file is not existed in [{}]",urlLoader.getUrl());
                    continue;
                }
//                in = new FileInputStream(inputStream);
                pros.load(inputStream);
                log.info("Config is found in {}",urlLoader.getUrl());
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
            log.warn("Config file [{}] is found.",fileName);
            return;
        }
        if(ConfUtil.properties==null){
            ConfUtil.properties=new Properties();
        }
        ConfUtil.properties.putAll(pros);
    }
}
