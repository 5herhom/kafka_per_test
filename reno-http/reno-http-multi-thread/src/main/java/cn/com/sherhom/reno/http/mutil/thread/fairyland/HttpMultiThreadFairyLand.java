package cn.com.sherhom.reno.http.mutil.thread.fairyland;

import cn.com.sherhom.reno.boot.annonation.ToExplore;
import cn.com.sherhom.reno.common.utils.DateUtil;
import cn.com.sherhom.reno.http.common.record.Stat;
import cn.com.sherhom.reno.http.common.utils.HttpConfUtil;
import cn.com.sherhom.reno.http.mutil.thread.entity.HttpMultiThreadArgs;
import cn.com.sherhom.reno.http.mutil.thread.runnable.HttpMultiThreadRunner;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author Sherhom
 * @date 2020/10/28 11:17
 */
@Slf4j
public class HttpMultiThreadFairyLand {
    private static final String targetUrl = HttpConfUtil.targetHttpUrl();
    private final String resultPath = HttpConfUtil.reportPath() + "/" + DateUtil.date2String(new Date());
    private final String fileName = "reno_http_multi_thread_result_" + DateUtil.date2String(new Date()) + ".csv";

    @ToExplore
    public void entrance(int threadNum, long lastTimeMs) {
        CountDownLatch fence = new CountDownLatch(threadNum);
        Stat stat = new Stat(threadNum, lastTimeMs);
        List<Thread> threadList=new ArrayList<>();
        HttpMultiThreadArgs args=new HttpMultiThreadArgs(lastTimeMs,stat);
        for (int i = 0; i < threadNum; i++) {
            Thread thread=new HttpMultiThreadRunner(args,i);
            threadList.add(thread);
            thread.start();
        }
        threadList.forEach(e-> {
            try {
                e.join();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        });
        stat.writeFileResult(resultPath, fileName);
    }
}
