package cn.com.sherhom.reno.jdbc.simple.test;

import cn.com.sherhom.reno.boot.RenoApplication;
import cn.com.sherhom.reno.boot.provider.CaseProvider;
import cn.com.sherhom.reno.boot.provider.ProviderBuilder;
import cn.com.sherhom.reno.jdbc.simple.args.SqlInfo;
import cn.com.sherhom.reno.jdbc.simple.fairyland.JdbcSimpleFairyLand;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Sherhom
 * @date 2021/2/7 16:34
 */
public class SimpleTest {
    @Test
    public void simpleTest01(){
//        List<CaseProvider> providers=new ArrayList<>();
//        List<SqlInfo> sqlInfos=new ArrayList<>();
//        SqlInfo sqlInfo1=new SqlInfo("select *from test_table","testname01",100);
//        SqlInfo sqlInfo2=new SqlInfo("select *from test_table2","testname02",100);
//        sqlInfos.add(sqlInfo1);
//        sqlInfos.add(sqlInfo2);
//        CaseProvider sqlInfoProvide=new ProviderBuilder<SqlInfo>().fieldType(ProviderBuilder.ProviderType.LIST).cases(
//                sqlInfos).build();
//
//        providers.add(sqlInfoProvide);
//
//        CaseProvider threadNumProvide=new ProviderBuilder<Integer>().fieldType(ProviderBuilder.ProviderType.LIST).cases(
//                Stream.of(1,5).collect(Collectors.toList())
//        ).build();
//        providers.add(threadNumProvide);
//
//        new RenoApplication().run(new JdbcSimpleFairyLand(),providers);
    }
}
