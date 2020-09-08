package cn.com.sherhom.reno.kafka.consumer.listener;

import cn.com.sherhom.reno.kafka.common.utils.KfkConf;
import cn.com.sherhom.reno.kafka.consumer.runner.NetHandleThread;

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
            while(true){
                try{
                    new NetHandleThread(serversocket.accept());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
