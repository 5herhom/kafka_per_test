package cn.com.sherhom.reno.kafka.consumer.runner;

import cn.com.sherhom.reno.kafka.common.net.RequestHandler;
import cn.com.sherhom.reno.kafka.consumer.bean.MapperHolder;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * @author Sherhom
 * @date 2020/9/7 15:49
 */
public class NetHandleThread extends Thread {
    Socket socket;
    String param;
    Object result;
    RequestHandler requestHandler = MapperHolder.requestHandler;
    public NetHandleThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    socket.getInputStream(),"UTF-8"));
            StringBuilder sb=new StringBuilder();
            String line;
            while((line=br.readLine())!=null)
            {
                sb.append(line);
            }
            br.close();
            param=sb.toString();
            this.result=requestHandler.handle(param);
            try {
                socket.getOutputStream().write(JSONObject.toJSONString(result).getBytes("UTF-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Object getResult() {
        return result;
    }
}
