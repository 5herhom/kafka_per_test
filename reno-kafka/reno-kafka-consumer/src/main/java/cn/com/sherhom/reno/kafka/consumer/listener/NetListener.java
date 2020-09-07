package cn.com.sherhom.reno.kafka.consumer.listener;

import cn.com.sherhom.reno.kafka.common.utils.KfkConf;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * @author Sherhom
 * @date 2020/9/7 14:52
 */
public class NetListener extends Thread{
    @Override
    public void run(){
        try {
            ServerSocket serversocket = new ServerSocket(KfkConf.consumerServerPort());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
