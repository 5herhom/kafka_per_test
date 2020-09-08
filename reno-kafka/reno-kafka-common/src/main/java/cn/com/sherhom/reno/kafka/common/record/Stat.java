package cn.com.sherhom.reno.kafka.common.record;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentLinkedDeque;
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
    private AtomicLong bytes;
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
    public Stat(long bytes,int msg,long reportingInterval) {
        this.failed =false;
        this.bytes =new AtomicLong(bytes);
        this.reportingInterval=reportingInterval;
        this.totalLatency =new AtomicLong(0);
        this.msg =new AtomicInteger(msg);

        this.maxLatency =new AtomicLong(0);
        this.indexList=new ConcurrentLinkedDeque();
        this.newWindows();
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
            this.bytes.addAndGet(bytes);
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

    public AtomicLong getBytes() {
        return bytes;
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
    public void printWindows(){
        log.info(String.format(winFormat,
                windowMaxLatency,
                windowBytes.longValue(),
                windowBytes.longValue()/(windowTotalLatency.longValue()/1000.0),
                windowCount.intValue(),
                windowCount.intValue()/(windowTotalLatency.longValue()/1000.0),
                windowTotalLatency));
    }

    private static final String sFormat=
            "maxLatency(ms):%s " +
            "total_byte:%s " +
            "byte/s:%s " +
            "total_msg:%s " +
            "msg/s:%s " +
            "total_latency(ms):%s";
    public synchronized void printTotal(){
        long stamp=stampedLock.writeLock();
        try {
            //windowCount:%s latency(ms):%s maxLatency(ms):%s byte/s:%s msg/s:%s
            log.info(String.format(sFormat,
                    maxLatency.longValue(),
                    bytes.longValue(),
                    bytes.longValue()/(totalLatency.longValue()/1000.0),
                    msg.intValue(),
                    msg.intValue()/(totalLatency.longValue()/1000.0),
                    totalLatency));
        }finally {
           stampedLock.unlockWrite(stamp);
        }
    }
}
