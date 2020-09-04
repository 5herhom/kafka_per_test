package cn.com.sherhom.reno.kafka.common.utils;

import cn.com.sherhom.reno.common.utils.Asset;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Sherhom
 * @date 2020/9/4 10:34
 */
@Slf4j
public class ZkUtils {
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
    public static List<String> getChildrenData(CuratorFramework curator,String path){
        List<String> ret= new ArrayList<>();
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
                    ret.add(value);
                }
            }
        } catch (Exception e) {
            LogUtil.printStackTrace(e);
            return new ArrayList<>();
        }
        return ret;

    }

    private static String noPathMsg(String path){
        return String.format("path[{}] is not existed at zk.",path);
    }
    private static String noChildMsg(String path){
        return String.format("path[{}] has no children at zk.",path);
    }
}
