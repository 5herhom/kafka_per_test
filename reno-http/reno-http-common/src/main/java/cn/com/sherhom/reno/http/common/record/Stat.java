package cn.com.sherhom.reno.http.common.record;

import cn.com.sherhom.reno.common.entity.Pair;
import cn.com.sherhom.reno.common.exception.RenoException;
import cn.com.sherhom.reno.common.utils.CsvWriter;
import cn.com.sherhom.reno.common.utils.FileUtil;
import cn.com.sherhom.reno.common.utils.ListCSVLine;
import cn.com.sherhom.reno.common.utils.LogUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Sherhom
 * @date 2020/10/28 13:53
 */
@Slf4j
public class Stat {
    int threadNum;
    Long lastTimeMs;
    CountDownLatch startFence;
    CountDownLatch endFence;
    boolean started;
    ThreadLocal<List<Long>> localCostTimeList=new ThreadLocal<>();
    int requestTotal;
    Long avgMs;
    Long maxMs;
    Long minMs;
    AtomicLong successNum=new AtomicLong();
    AtomicLong failNum=new AtomicLong();
    List<PnMetric> PnList=new ArrayList<>();
    List<Long> finalCostTimeList=new ArrayList<>();
    public Stat(int threadNum, Long lastTimeMs) {
        this.threadNum = threadNum;
        this.lastTimeMs = lastTimeMs;
        this.startFence = new CountDownLatch(threadNum);
        this.endFence = new CountDownLatch(threadNum);
    }

    public void start(){
        startFence.countDown();
        try {
            startFence.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(!started){
            synchronized (startFence){
                if(!started){
                    started=true;
                    log.info("Thread:{} started stat",Thread.currentThread().getName());
                }
            }
        }
    }
    private List<Long> getLocalCostTimeList(){
        List<Long> list=localCostTimeList.get();
        if(list==null){
            list=new ArrayList<>();
            localCostTimeList.set(list);
        }
        return list;
    }

    public void stop(){
        endFence.countDown();
        try {
            endFence.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized (endFence){
            if(started){
                started=false;
                log.info("Thread:{} ended stat",Thread.currentThread().getName());
            }
            finalCostTimeList.addAll(this.getLocalCostTimeList());
        }
    }
    public void recordMs(Long ms){
        getLocalCostTimeList().add(ms);
    }
    public void recordSuccess(){
        successNum.getAndIncrement();
    }
    public void recordFail(){
        failNum.getAndIncrement();
    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PnMetric{
        int quartile ;
        long ms;
    }
    private final static List<Integer> percentList= Stream.of(95,90,85,80,75,70,60,30,20).collect(Collectors.toList());

    public void calculateResult(){
        finalCostTimeList.sort(Long::compareTo);
        this.requestTotal=finalCostTimeList.size();
        this.avgMs=Math.round(finalCostTimeList.stream().mapToLong(e->e).average().getAsDouble());
        this.maxMs=finalCostTimeList.stream().mapToLong(e->e).max().getAsLong();
        this.minMs=finalCostTimeList.stream().mapToLong(e->e).min().getAsLong();
        percentList.forEach(quartile->{
            int index=quartile*finalCostTimeList.size()/100;
            this.PnList.add(new PnMetric(quartile,finalCostTimeList.get(index)));
        });
    }
    private final static List<Pair<String,String>> ResultMapper=Stream.of(
            new Pair<>("threadNum","threadNum"),
            new Pair<>("requestTotal","requestTotal"),
            new Pair<>("successNum","successNum"),
            new Pair<>("failNum","failNum"),
            new Pair<>("avgMs","avgMs"),
            new Pair<>("maxMs","maxMs"),
            new Pair<>("minMs","minMs")
    ).collect(Collectors.toList());
    static {
        percentList.forEach(quartile->
                ResultMapper.add(new Pair<>(quartileLineName(quartile),quartileLineName(quartile))));
    }
    private final static ListCSVLine resultLine=new ListCSVLine(ResultMapper,"\t");

    public JSONObject getResult(){
        if(started)
            throw new RenoException("The stat should get result when stopped.");
        if(this.requestTotal==0)
            calculateResult();
        JSONObject resultJson=new JSONObject();
        resultJson.put("threadNum",this.threadNum);
        resultJson.put("requestTotal",this.requestTotal);
        resultJson.put("successNum",this.successNum.get());
        resultJson.put("failNum",this.failNum.get());
        resultJson.put("avgMs",this.avgMs);
        resultJson.put("maxMs",this.maxMs);
        resultJson.put("minMs",this.minMs);
        if(percentList!=null)
            PnList.forEach(pnMetric->{
                resultJson.put(quartileLineName(pnMetric.quartile),pnMetric.ms);
            });
        return resultJson;
    }
    public static String quartileLineName(int quartile){
        return MessageFormat.format("P{0}",quartile);
    }
    public void writeFileResult(String path,String fileName){
        CsvWriter resultWriter = new CsvWriter(FileUtil.getPathAndFile(path, fileName), resultLine);
        try {
            resultWriter.open();
            if (resultWriter.isShouldCreate())
                resultWriter.writeHeader();
            resultWriter.writeLine(getResult());
        } catch (Exception e) {
            LogUtil.printStackTrace(e);
            resultWriter.close();
        }
    }
}
