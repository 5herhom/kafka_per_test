package cn.com.sherhom.reno.jdbc.simple.fairyland;

import cn.com.sherhom.reno.boot.annonation.ToExplore;
import cn.com.sherhom.reno.boot.record.TimeCostStat;
import cn.com.sherhom.reno.common.utils.DateUtil;
import cn.com.sherhom.reno.common.utils.LogUtil;
import cn.com.sherhom.reno.jdbc.common.conf.JdbcConf;
import cn.com.sherhom.reno.jdbc.common.utils.JdbcMapperUtil;
import cn.com.sherhom.reno.jdbc.simple.args.SqlInfo;
import cn.com.sherhom.reno.jdbc.simple.common.holder.RowNum2DBNameHolder;
import cn.com.sherhom.reno.jdbc.simple.runnable.JdbcSimpleThread;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static cn.com.sherhom.reno.jdbc.common.constants.ReportHeads.*;

/**
 * @author Sherhom
 * @date 2021/2/7 13:53
 */
@Slf4j
public class JdbcSimpleFairyLand {
    private final String resultPath = JdbcConf.getReportPath() + "/" + DateUtil.date2String(new Date());
    private final String fileName = JdbcConf.DEFAULT_REPORT_FILENAME+"_" + DateUtil.date2String(new Date()) + ".csv";

    @ToExplore
    public void run(SqlInfo sqlInfo,long rowNum, int concurrentNum){
        String sql= RowNum2DBNameHolder.getSqlFromRowNum(sqlInfo.getSqlTemplate(),rowNum);

        log.info("Do query in {} thread for {} rows in table:{} ",concurrentNum,rowNum,sql);
        String sqlName=sqlInfo.getSqlName();
        long queryTotal= JdbcConf.getQueryTotal();
        List<Thread> threads=new ArrayList<>();

        TimeCostStat stat=new TimeCostStat(concurrentNum, JdbcMapperUtil.getJdbcTimeCostMapper(), Stream.of(50,70,90,99).collect(Collectors.toList()));
        for(int i=0;i<concurrentNum;i++){
            threads.add(new JdbcSimpleThread(stat,sql,queryTotal));
        }
        threads.forEach(e->e.start());
        threads.forEach(e-> {
            try {
                e.join();
            } catch (InterruptedException ex) {
                LogUtil.printStackTrace(ex);
            }
        });
        Map<String,Object> extReport=new HashMap<>();
        extReport.put(SQL_NAME,sqlName);
        extReport.put(SQL,sql);
        extReport.put(ROW_NUM_IN_MAIN_TABLE,rowNum);
        stat.writeFileResult(resultPath, fileName,extReport);
        log.info("result:{}",stat.getResult(extReport));
    }
}
