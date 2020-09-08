package cn.com.sherhom.reno.kafka.common.record;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.StampedLock;

/**
 * @author Sherhom
 * @date 2020/9/8 10:14
 */
@Slf4j
public class Stat {

    private final StampedLock stampedLock = new StampedLock();
    private volatile boolean failed;
    private long startTime;
    private volatile boolean started=false;
    private AtomicLong totalBytes;
    private AtomicLong totalLatency;
    private AtomicInteger msg;
    private AtomicLong maxLatency;

    private long windowStart;
    private AtomicInteger windowCount;
    private long windowMaxLatency;
    private AtomicLong windowTotalLatency;
    private AtomicLong windowBytes;
    private long reportingInterval;
    private ConcurrentLinkedDeque<Integer> indexList;

    private CountDownLatch countDownLatch;
    public Stat(long totalBytes, int msg, long reportingInterval, int threadNum) {
        this.startTime =System.currentTimeMillis();
        this.failed =false;
        this.totalBytes =new AtomicLong(totalBytes);
        this.reportingInterval=reportingInterval;
        this.totalLatency =new AtomicLong(0);
        this.msg =new AtomicInteger(msg);

        this.maxLatency =new AtomicLong(0);
        this.indexList=new ConcurrentLinkedDeque();
        this.newWindows();
        countDownLatch=new CountDownLatch(threadNum);
    }
    public void start(){
        countDownLatch.countDown();
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(!started){
            synchronized (countDownLatch){
                if(!started){
                    this.startTime =System.currentTimeMillis();
                    started=true;
                }
            }
        }
        log.info("Thread:{} started stat",Thread.currentThread().getName());
    }
    public boolean isFailed() {
        return failed;
    }
    public void setFailed() {
        this.failed = true;
    }
    public void record(int index,long bytes,int msg,long latency){
        long stamp=stampedLock.readLock();
        try{
            this.totalBytes.addAndGet(bytes);
            this.msg.addAndGet(msg);
            this.totalLatency.addAndGet(latency);
            this.maxLatency.set(Math.max(latency,this.maxLatency.longValue()));
            this.indexList.add(index);

            this.windowCount.incrementAndGet();
            this.windowMaxLatency=Math.max(latency,windowMaxLatency);
            this.windowTotalLatency.addAndGet(latency);
            this.windowBytes.addAndGet(bytes);
        } finally {
            stampedLock.unlockRead(stamp);
        }
        if(this.reportingInterval>0&&System.currentTimeMillis()-this.windowStart>this.reportingInterval){
            printAndNewWindow();
        }
    }

    public AtomicLong getTotalBytes() {
        return totalBytes;
    }

    public AtomicLong getTotalLatency() {
        return totalLatency;
    }

    public AtomicInteger getMsg() {
        return msg;
    }

    public AtomicInteger getWindowCount() {
        return windowCount;
    }

    public void newWindows(){
        this.windowStart = System.currentTimeMillis();
        this.windowCount = new AtomicInteger(0);
        this.windowMaxLatency = 0;
        this.windowTotalLatency =  new AtomicLong(0);
        this.windowBytes = new AtomicLong(0);
    }

    private void printAndNewWindow(){
        long stamp=stampedLock.writeLock();
        try {
            printWindows();
            newWindows();
        } finally {
            stampedLock.unlockWrite(stamp);
        }
    }
    private static final String winFormat=
            "win_max_latency(ms):%s " +
            "win_total_byte:%s " +
            "win_byte/s:%s " +
            "win_total_msg:%s " +
            "win_msg/s:%s " +
            "win_total_latency(ms):%s";
    public void printWindows(){;
        long ellapsed = System.currentTimeMillis() - windowStart;
        log.info(windowsMgs());
    }
    public String windowsMgs(){
        Metric metric = windowsMetric();
        return String.format(winFormat,
                metric.getMaxLatency(),
                metric.getTotalBytes(),
                metric.getBytePerSec(),
                metric.getTotalCount(),
                metric.getCountPerSec(),
                metric.getTotalLatency());
    }
    public Metric windowsMetric(){
        long ellapsed = System.currentTimeMillis() - windowStart;
        return new Metric(windowMaxLatency,
                windowBytes.longValue(),
                windowBytes.longValue()/(ellapsed/1000.0),
                windowCount.intValue(),
                windowCount.intValue()/(ellapsed/1000.0),
                windowTotalLatency.longValue());
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class Metric{
        long maxLatency;
        long totalBytes;
        double bytePerSec;
        long totalCount;
        double countPerSec;
        long totalLatency;
    }
    private static final String sFormat=
            "maxLatency(ms):%s " +
            "total_byte:%s " +
            "byte/s:%s " +
            "total_msg:%s " +
            "msg/s:%s " +
            "total_latency(ms):%s";
    public void printTotal(){
       log.info(totalMsg());
    }
    public String totalMsg(){
        long stamp=stampedLock.writeLock();
        long ellapsed = System.currentTimeMillis() - startTime;
        String res;
        try {
            //windowCount:%s latency(ms):%s maxLatency(ms):%s byte/s:%s msg/s:%s
            res=String.format(sFormat,
                    maxLatency.longValue(),
                    totalBytes.longValue(),
                    totalBytes.longValue()/(ellapsed/1000.0),
                    msg.intValue(),
                    msg.intValue()/(ellapsed/1000.0),
                    totalLatency);
        }finally {
            stampedLock.unlockWrite(stamp);
        }
        return res;
    }
    public Metric totalMetric(){
        long stamp=stampedLock.writeLock();
        long ellapsed = System.currentTimeMillis() - startTime;
        Metric res;
        try {
            //windowCount:%s latency(ms):%s maxLatency(ms):%s byte/s:%s msg/s:%s
            res=new Metric(
                    maxLatency.longValue(),
                    totalBytes.longValue(),
                    totalBytes.longValue()/(ellapsed/1000.0),
                    msg.intValue(),
                    msg.intValue()/(ellapsed/1000.0),
                    totalLatency.longValue());
        }finally {
            stampedLock.unlockWrite(stamp);
        }
        return res;
    }
}
