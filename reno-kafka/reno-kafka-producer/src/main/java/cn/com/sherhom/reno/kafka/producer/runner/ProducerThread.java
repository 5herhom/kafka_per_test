package cn.com.sherhom.reno.kafka.producer.runner;

import cn.com.sherhom.reno.kafka.common.service.ProducerFactory;
import cn.com.sherhom.reno.kafka.producer.entity.ProducerRunnerArgs;
import cn.com.sherhom.reno.kafka.common.service.ProducerOpt;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Producer;

@Slf4j
public class ProducerThread extends Thread{
    public ProducerThread(ProducerRunnerArgs args){
        this.args=args;
    }
    ProducerRunnerArgs args;
    @Override
    public void run() {
        Producer producer= ProducerFactory.newProducer();
        ProducerOpt.sendMsg(producer,args.getTopicName(),args.getRecordSize(),args.getNum(),args.getThroughput(),args.getStat());
        log.info("start to close.");
        producer.close();
    }
}
