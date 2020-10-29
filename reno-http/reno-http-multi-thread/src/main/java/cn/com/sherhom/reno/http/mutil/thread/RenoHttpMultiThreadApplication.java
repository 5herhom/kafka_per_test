package cn.com.sherhom.reno.http.mutil.thread;

import cn.com.sherhom.reno.boot.RenoApplication;
import cn.com.sherhom.reno.boot.annonation.ToExplore;
import cn.com.sherhom.reno.boot.provider.CaseProvider;
import cn.com.sherhom.reno.boot.provider.ProviderBuilder;
import cn.com.sherhom.reno.http.mutil.thread.fairyland.HttpMultiThreadFairyLand;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Sherhom
 * @date 2020/10/28 10:32
 */
@Slf4j
public class RenoHttpMultiThreadApplication {
    public static void main(String[] args) {
        List<CaseProvider> providers=new ArrayList<>();
        CaseProvider threadNumProvide=new ProviderBuilder<Integer>().fieldType(ProviderBuilder.ProviderType.LIST).cases(
                Stream.of(100).collect(Collectors.toList())
        ).build();
        providers.add(threadNumProvide);
//        CaseProvider lastTimeMsProvide=new ProviderBuilder<Long>().fieldType(ProviderBuilder.ProviderType.LIST).cases(
//                Stream.of(10000l,900000l).collect(Collectors.toList())
//        ).build();
        CaseProvider lastTimeMsProvide=new ProviderBuilder<Long>().fieldType(ProviderBuilder.ProviderType.LIST).cases(
                Stream.of(10000l).collect(Collectors.toList())
        ).build();
        providers.add(lastTimeMsProvide);

        new RenoApplication().run(new HttpMultiThreadFairyLand(),providers);
    }
}
