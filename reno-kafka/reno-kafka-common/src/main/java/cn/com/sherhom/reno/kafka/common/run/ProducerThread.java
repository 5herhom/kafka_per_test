package cn.com.sherhom.reno.kafka.common.run;

import cn.com.sherhom.reno.kafka.common.entity.ProducerRunnerArgs;

public class ProducerThread extends Thread{
    public ProducerThread(ProducerRunnerArgs args){
        this.args=args;
    }
    ProducerRunnerArgs args;
    @Override
    public void run() {

    }
}
