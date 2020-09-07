package cn.com.sherhom.reno.kafka.common.holder;

import cn.com.sherhom.reno.common.utils.LogUtil;
import cn.com.sherhom.reno.kafka.common.utils.KfkConf;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.RetryUntilElapsed;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Sherhom
 * @date 2020/9/4 13:58
 */
@Slf4j
public class ZkCuratorHolder {
    private static final ConcurrentHashMap<String, CuratorFramework> cluster2zk=new ConcurrentHashMap<>();
    public static CuratorFramework getZkCli(String zkServers){
        if(cluster2zk.contains(zkServers)){
            return cluster2zk.get(zkServers);
        }
        else{
            return newZkCli(zkServers);
        }
    }
    private static CuratorFramework newZkCli(String zkServers){
        synchronized (cluster2zk){
            try {
                if(cluster2zk.get(zkServers)!=null)
                    return cluster2zk.get(zkServers);
                CuratorFramework curator= CuratorFrameworkFactory.builder().connectString(zkServers)
                        .retryPolicy(new RetryUntilElapsed(KfkConf.zkMaxWaitTime(),KfkConf.zkMaxRetry()))
                        .connectionTimeoutMs(KfkConf.zkMaxWaitTime()).build();
                curator.getConnectionStateListenable().addListener((client, state) -> {
                    String currentConnectionString=client.getZookeeperClient().getCurrentConnectionString();
                    if(state==ConnectionState.LOST){
                        log.info("Lost session with zookeeper:{}",currentConnectionString);
                    }
                    else if(state==ConnectionState.CONNECTED){
                        log.info("Connect with zookeeper:{}",currentConnectionString);
                    }
                    else if(state == ConnectionState.RECONNECTED){
                        log.info("Reconnected with zookeeper:{}",currentConnectionString);
                    }
                });
                curator.start();
                cluster2zk.put(zkServers,curator);
                return curator;
            } catch (Exception e) {
                LogUtil.printStackTrace(e);
                return null;
            }
        }
    }
}
