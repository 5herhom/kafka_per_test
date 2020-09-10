package cn.com.sherhom.reno.kafka.producer.fairyland;

import cn.com.sherhom.reno.boot.annonation.ToExplore;
import cn.com.sherhom.reno.common.exception.RenoException;
import cn.com.sherhom.reno.common.utils.*;
import cn.com.sherhom.reno.kafka.common.holder.ListCSVHolder;
import cn.com.sherhom.reno.kafka.common.record.ResultMetric;
import cn.com.sherhom.reno.kafka.producer.config.ProConf;
import cn.com.sherhom.reno.kafka.producer.entity.ProducerRunnerArgs;
import cn.com.sherhom.reno.kafka.common.entity.TopicSimpleInfo;
import cn.com.sherhom.reno.kafka.common.record.Stat;
import cn.com.sherhom.reno.kafka.producer.runner.ProducerThread;
import cn.com.sherhom.reno.kafka.common.service.KfkOperate;
import cn.com.sherhom.reno.kafka.common.utils.KfkConf;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
public class KafkaProducerFairyLand {
    public static final String topicFormat =
            "RENO_TEST_TOPIC_PARTITION_%s_%s";
    public static final String zkServer = KfkConf.zkIp();

    private final String resultPath = ProConf.reportPath()+"/"+DateUtil.date2String(new Date());
    private final String fileName = "reno_kfk_producer_result_" + DateUtil.date2String(new Date()) + ".csv";

    @ToExplore
    public void entrance(int topicNum,
                         int partitionNum,
                         int producerNum,
                         int consumerNum,
                         int bytePerMsg,
                         int bytePerSecInput,//b/s
                         int bytePerSecOutput) {
        List<String> topics = new ArrayList<>();
        List<ProducerThread> proThreads = new ArrayList<>();
        List conThreads = new ArrayList<>();
        String topic;
        TopicSimpleInfo topicSimpleInfo, param;
        //1.check and prepare topic
        for (int i = 0; i < topicNum; i++) {
            topic = String.format(topicFormat, partitionNum, i);
            prepareTopic(topic, partitionNum);
            topics.add(topic);
        }
        //2.create threads
//        ProducerRunnerArgs proArgs;
        Stat stat = new Stat(0, 0, ProConf.reportInterval(), producerNum, ProConf.isReportToFile(), resultPath);
        try {
            long numOfMsg = ProConf.allKbSize() * 1024 / bytePerMsg;
            int throughput = bytePerSecInput / bytePerMsg;

            long singleBatchSize = numOfMsg / producerNum;
            int singleThroughput = throughput / producerNum;
            topics.forEach((t) -> {
                        for (int i = 0; i < producerNum; i++) {
                            ProducerRunnerArgs proArgs = new ProducerRunnerArgs();
                            proArgs.setTopicName(t);
                            proArgs.setRecordSize(bytePerMsg);
                            proArgs.setNum(singleBatchSize);
                            proArgs.setThroughput(singleThroughput);
                            proArgs.setStat(stat);
                            proThreads.add(new ProducerThread(proArgs));
                        }
                    }
            );
            log.info("Expect:  total_byte:{},bytePerMsg:{},byte/s:{},total_msg:{}, msg/s:{}",
                    ProConf.allKbSize() * 1024, bytePerMsg, bytePerSecInput, numOfMsg, throughput);
            for (int i = 0; i < consumerNum; i++) {
                //todo
            }
            proThreads.forEach(e -> e.start());
            stat.writeHeaderToFile();
            proThreads.forEach(e -> {
                try {
                    e.join();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            });
            stat.stop();
            stat.printTotal();
            ResultMetric resultMetric = new ResultMetric();
            resultMetric.setTopicNum(topicNum);
            resultMetric.setPartitionNum(partitionNum);
            resultMetric.setProducerNum(producerNum);
            resultMetric.setConsumerNum(consumerNum);
            resultMetric.setBytePerMsg(bytePerMsg);
            resultMetric.setBytePerSecInput(bytePerSecInput);
            resultMetric.setBytePerSecOutput(bytePerSecOutput);
            resultMetric.setActualInput(stat.getBytePerSec());
            resultMetric.setInputDiff(stat.getBytePerSec()-bytePerSecInput );
            resultMetric.setInputDiffPercent((stat.getBytePerSec()-bytePerSecInput)/(double)bytePerSecInput );
            resultMetric.setActualOutput(0);
            resultMetric.setOutputDiff(0);
            resultMetric.setOutputDiffPercent(0.0);
            resultMetric.setSuccess(!stat.isFailed());
            resultMetric.setDetailLogPath(stat.getFilePath());
            if (stat.isFailed())
                log.error("Case failed!");
            CsvWriter resultWriter = new CsvWriter(FileUtil.getPathAndFile(resultPath, fileName), ListCSVHolder.resultCsvLine);
            try{
                resultWriter.open();
                if(resultWriter.isShouldCreate())
                    resultWriter.writeHeader();
                resultWriter.writeLine(resultMetric);
            } catch (Exception e) {
                LogUtil.printStackTrace(e);
                resultWriter.close();
            }


        } catch (Exception e) {
            e.printStackTrace();
            throw new RenoException(e);
        } finally {
            if (stat != null)
                stat.closeFile();
        }

    }

    public void prepareTopic(String topicName, int partitionNum) {
        TopicSimpleInfo topicSimpleInfo = KfkOperate.getTopicSimpleInfo(zkServer, topicName);
        TopicSimpleInfo param;
        if (topicSimpleInfo == null) {
            param = new TopicSimpleInfo();
            param.setTopicName(topicName);
            param.setPartNum(partitionNum);
            param.setReplicationNum(2);
            param.setZkServers(KfkConf.zkIp());
            Asset.isTrue(KfkOperate.createTopic(param),
                    String.format("Topic[{}] create error.", topicName));
        } else if (topicSimpleInfo.getPartNum() < partitionNum) {
            Asset.isTrue(KfkOperate.expandPartitions(zkServer, topicName, partitionNum),
                    String.format("Fail to expand partition from {} to {}, for topic[{}]",
                            topicSimpleInfo.getPartNum(), partitionNum, topicName));
        }
    }

}
