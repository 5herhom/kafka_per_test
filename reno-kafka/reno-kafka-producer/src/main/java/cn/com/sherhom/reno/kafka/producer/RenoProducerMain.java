package cn.com.sherhom.reno.kafka.producer;

import cn.com.sherhom.reno.boot.RenoApplication;
import cn.com.sherhom.reno.boot.provider.CaseProvider;
import cn.com.sherhom.reno.boot.provider.ProviderBuilder;
import cn.com.sherhom.reno.common.utils.ConfUtil;
import cn.com.sherhom.reno.kafka.producer.fairyland.KafkaProducerFairyLand;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class RenoProducerMain {
    public static void main(String[] args) {
        List<CaseProvider> providers=new ArrayList<>();
        CaseProvider topicNumProvider=new ProviderBuilder<Integer>().fieldType(ProviderBuilder.ProviderType.LIST).cases(
                Stream.of(1,5,50,100,300).collect(Collectors.toList())).build();
        CaseProvider partitionNumProvider=new ProviderBuilder<Integer>().fieldType(ProviderBuilder.ProviderType.LIST).cases(
                Stream.of(3).collect(Collectors.toList())
        ).build();
        CaseProvider producerNumProvider=new ProviderBuilder<Integer>().fieldType(ProviderBuilder.ProviderType.LIST).cases(
                Stream.of(1,10,100,500).collect(Collectors.toList())
        ).build();
        CaseProvider consumerNumProvider=new ProviderBuilder<Integer>().fieldType(ProviderBuilder.ProviderType.LIST).cases(
                Stream.of(0).collect(Collectors.toList())
        ).build();
//        CaseProvider bytePerMsgProvider=new ProviderBuilder<Integer>().fieldType(ProviderBuilder.ProviderType.LIST).cases(
//                Stream.of(512,20480,102400,512000,1024000).collect(Collectors.toList())
//        ).build();
        CaseProvider bytePerMsgProvider=new ProviderBuilder<Integer>().fieldType(ProviderBuilder.ProviderType.LIST).cases(
                Stream.of(512).collect(Collectors.toList())
        ).build();
        CaseProvider bytePerSecInputProvider=new ProviderBuilder<Integer>().fieldType(ProviderBuilder.ProviderType.LIST).cases(
                Stream.of(102400,204800,512000,1024000).collect(Collectors.toList())
        ).build();
        CaseProvider bytePerSecOutputProvider=new ProviderBuilder<Integer>().fieldType(ProviderBuilder.ProviderType.LIST).cases(
                Stream.of(0).collect(Collectors.toList())
        ).build();
        providers.add(topicNumProvider);
        providers.add(partitionNumProvider);
        providers.add(producerNumProvider);
        providers.add(consumerNumProvider);
        providers.add(bytePerMsgProvider);
        providers.add(bytePerSecInputProvider);
        providers.add(bytePerSecOutputProvider);
        new RenoApplication().run(new KafkaProducerFairyLand(),providers);
    }
}
