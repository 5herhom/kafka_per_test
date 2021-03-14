package cn.com.sherhom.reno.jdbc.simple;

import cn.com.sherhom.reno.boot.RenoApplication;
import cn.com.sherhom.reno.boot.provider.CaseProvider;
import cn.com.sherhom.reno.boot.provider.ProviderBuilder;
import cn.com.sherhom.reno.jdbc.simple.args.SqlInfo;
import cn.com.sherhom.reno.jdbc.simple.fairyland.JdbcSimpleFairyLand;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Sherhom
 * @date 2021/2/7 10:35
 */
public class JdbcSimpleApplication {
    public static final String SQL1="select count(*) from {0}.test_table";
    public static final String SQL2="select count(*) from {0}.test_table";
    static Map<String,SqlInfo> sqlTemplates=new HashMap<String, SqlInfo>(){
        {
            put("testname01",new SqlInfo(SQL1,"testname01"));
            put("testname02",new SqlInfo(SQL2,"testname02"));
        }
    };
    public static void main(String[] args) {
        List<CaseProvider> providers=new ArrayList<>();

//        sql
        List<SqlInfo> sqlInfos=sqlTemplates.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList());
        CaseProvider sqlInfoProvide=new ProviderBuilder<SqlInfo>().fieldType(ProviderBuilder.ProviderType.LIST).cases(
                sqlInfos).build();

        providers.add(sqlInfoProvide);
//        rowNum
        CaseProvider rowNumProvide=new ProviderBuilder<Long>().fieldType(ProviderBuilder.ProviderType.LIST).cases(
                Stream.of(5l,100l).collect(Collectors.toList())).build();
        providers.add(rowNumProvide);
//      threadNum
        CaseProvider threadNumProvide=new ProviderBuilder<Integer>().fieldType(ProviderBuilder.ProviderType.LIST).cases(
                Stream.of(1,5,10).collect(Collectors.toList())
        ).build();
        providers.add(threadNumProvide);

        new RenoApplication().run(new JdbcSimpleFairyLand(),providers);
    }
}
