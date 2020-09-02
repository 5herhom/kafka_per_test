package cn.com.sherhom.reno.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.URL;
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
    static private final String FILE_NAME="application.properties";
    static private final String BASE_PATH=System.getProperty("reno.config.path","");
    private static synchronized void load(){
        if(properties!=null)
            return;
        String path;
        for(String dir: CONFIG_DIR_PATH){
            InputStream in= null;
            try {
                path=BASE_PATH+dir+"/"+FILE_NAME;
                URL url=ConfUtil.class.getClassLoader().getResource(path);
                if(url==null){
                    log.info("Config file is not existed in [{}]",path);
                    continue;
                }
                in = new FileInputStream(url.getFile());
                Properties pros=new Properties();
                pros.load(in);
                properties=pros;
                log.info("Config is found in {}",path);
                return;
            } catch (FileNotFoundException e) {
                log.warn(e.getMessage());
            } catch (IOException e) {
                log.warn(e.getMessage());
            }
            finally {
                IOUtils.closeQuietly(in);
            }
        }
        if(properties==null){
            log.error("Not config file is found.");
            SystemUtil.exit();
        }
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
