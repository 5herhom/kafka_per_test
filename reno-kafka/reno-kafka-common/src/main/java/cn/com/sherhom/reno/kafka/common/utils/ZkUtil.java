package cn.com.sherhom.reno.kafka.common.utils;

import cn.com.sherhom.reno.common.utils.Asset;
import cn.com.sherhom.reno.common.utils.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;

import java.util.*;
import java.util.function.Function;

/**
 * @author Sherhom
 * @date 2020/9/4 10:34
 */
@Slf4j
public class ZkUtil {
    private static final String DEFAULT_CODE="utf-8";
    private static String getZkConnStr(CuratorFramework curatorFramework){
        try{
            return curatorFramework.getZookeeperClient().getCurrentConnectionString();
        } catch (Exception e) {
            LogUtil.printStackTrace(e);
            return null;
        }
    }

    public static String getData(CuratorFramework curator,String path){
        String emptyMsg=noPathMsg(path);
        try {
            Asset.notNull(curator.checkExists().forPath(path),emptyMsg);
            byte[] bytes=curator.getData().forPath(path);
            String value=new String(bytes,DEFAULT_CODE);
            Asset.isNotBlank(value,emptyMsg);
            return value;
        } catch (Exception e) {
            log.error("Occur when zk path is [{}]",path);
            LogUtil.printStackTrace(e);
            return null;
        }
    }
    public static<R> Map<String,R> getChildrenData(CuratorFramework curator, String path, Function<String,R> handler){
        Map<String,R> path2value=new HashMap<>();
        String emptyMsg=noPathMsg(path);
        String noChildMsg=noChildMsg(path);
        try {
            Asset.notNull(curator.checkExists().forPath(path),emptyMsg);
            List<String> children=curator.getChildren().forPath(path);
            Asset.notEmpty(children,noChildMsg);
            String basePath=path;
            for(String child : children){
                String pathTmp = basePath+"/"+child;
                byte[] bytes=curator.getData().forPath(pathTmp);
                String value = new String(bytes,DEFAULT_CODE);
                if(StringUtils.isNotBlank(value)){
                    path2value.put(child,handler.apply(value));
                }
            }
        } catch (Exception e) {
            LogUtil.printStackTrace(e);
            return  new HashMap<>();
        }
        return path2value;

    }
    private static String noPathMsg(String path){
        return String.format("path[%s] is not existed at zk.",path);
    }
    private static String noChildMsg(String path){
        return String.format("path[%s] has no children at zk.",path);
    }

}
