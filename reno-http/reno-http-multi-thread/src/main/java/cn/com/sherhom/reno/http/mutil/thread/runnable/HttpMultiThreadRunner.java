package cn.com.sherhom.reno.http.mutil.thread.runnable;

import cn.com.sherhom.reno.http.common.record.Stat;
import cn.com.sherhom.reno.http.common.utils.HttpConfUtil;
import cn.com.sherhom.reno.http.common.utils.HttpUtils;
import cn.com.sherhom.reno.http.mutil.thread.entity.HttpMultiThreadArgs;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;

import java.util.concurrent.CountDownLatch;

/**
 * @author Sherhom
 * @date 2020/10/28 13:37
 */
@Slf4j
public class HttpMultiThreadRunner extends Thread {
    HttpMultiThreadArgs args;
    public HttpMultiThreadRunner(HttpMultiThreadArgs args,int i) {
        super("HttpMutilThread-"+i);
        this.args = args;
    }
    public HttpMultiThreadRunner(HttpMultiThreadArgs args) {
        super("HttpMutilThread");
        this.args = args;
    }

    @Override
    public void run() {
        Long lastTimeMs=args.getLastTimeMs();
        Stat stat=args.getStat();
        stat.start();
        Long endTime=System.currentTimeMillis()+lastTimeMs;
        StopWatch sw;
        String targetUrl= HttpConfUtil.targetHttpUrl();
        String param=HttpConfUtil.targetHttpParam();
        while(System.currentTimeMillis()<endTime){
            sw=new StopWatch();
            sw.start();
            //core
            String res=HttpUtils.postJson(targetUrl,param);
            sw.stop();
            log.debug(res);
            stat.recordMs(sw.getTime());
            if(res==null)
                stat.recordFail();
            else
                stat.recordSuccess();
        }
        stat.stop();
    }
}
