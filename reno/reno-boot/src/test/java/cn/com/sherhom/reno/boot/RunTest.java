package cn.com.sherhom.reno.boot;

import cn.com.sherhom.reno.boot.annonation.ToExplore;
import cn.com.sherhom.reno.boot.provider.CaseProvider;
import cn.com.sherhom.reno.boot.provider.ProviderBuilder;
import cn.com.sherhom.reno.common.exception.RenoException;
import cn.com.sherhom.reno.common.utils.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
/**
 * @author Sherhom
 * @date 2020/9/3 20:12
 */
@Slf4j
public class RunTest {
    @Test
    public void runTest(){
        List<CaseProvider> providers=new ArrayList<>();
        providers.add(new ProviderBuilder<String>().fieldType(ProviderBuilder.ProviderType.RANGE).
                start(10l).end(20l).step(2l).stepForward((i)->"TOPIC-"+String.valueOf(i)).build());
        providers.add(new ProviderBuilder<Long>().fieldType(ProviderBuilder.ProviderType.LIST).cases(
                Stream.of(2l,3l,7l,13l,45l).collect(Collectors.toList())).build());
        providers.add(new ProviderBuilder<Integer>().fieldType(ProviderBuilder.ProviderType.VALUE).value(99).build());

        providers.add(new ProviderBuilder<String>().fieldType(ProviderBuilder.ProviderType.RANGE).
                start(23l).end(55l).step(10l).build());
        new RenoApplication().run(new Excute(),providers);
    }

    private class Excute{
        @ToExplore
        public void println(String s,Long l,Integer i,Long s2){
            log.info("result:{},{},{},{}",s,l,i,s2);
        }
    }

    @Test
    public void logTest(){
//        log.info("startlog");
        try{

            throw new RenoException("aaaa");
        } catch (Exception e) {
//            LogUtil.printStackTrace(new RenoException("bbb"));
            LogUtil.printStackTrace(log,new RenoException("bbb"));
        }
    }
}
