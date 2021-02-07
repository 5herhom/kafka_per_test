package cn.com.sherhom.reno.jdbc.simple.runnable;

import cn.com.sherhom.reno.boot.record.TimeCostStat;
import cn.com.sherhom.reno.common.utils.LogUtil;
import cn.com.sherhom.reno.jdbc.common.conf.JdbcConf;
import cn.com.sherhom.reno.jdbc.common.utils.JdbcUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.time.StopWatch;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Sherhom
 * @date 2021/2/7 10:47
 */
@Slf4j
public class JdbcSimpleThread extends Thread {
    public static final String THREAD_NAME_TEMPLATE = "JdbcSimpleThread";
    private static final AtomicInteger threadIdGetter = new AtomicInteger(0);
    private long totalQueryCount;
    private String sql;
    private TimeCostStat stat;

    public JdbcSimpleThread() {
        super(THREAD_NAME_TEMPLATE + "-" + threadIdGetter.getAndIncrement());
    }

    public JdbcSimpleThread(TimeCostStat stat, String sql, long queryCount) {
        this();
        this.stat = stat;
        this.sql = sql;
        this.totalQueryCount = queryCount;
    }

    @Override
    public void run() {
        Connection con = null;
        PreparedStatement state = null;
        ResultSet resultSet = null;
        try {
            con = JdbcUtils.getConnection();
            state = con.prepareStatement("select 1");
            resultSet = state.executeQuery();
        } catch (SQLException e) {
            LogUtil.printStackTrace(e);
            System.exit(-1);
        } finally {
        }

        String sql = this.sql;
        int i = 0;
        long start,end;
        String column;
        stat.start();
        while (i < totalQueryCount) {
            try {
                start=System.currentTimeMillis();
                state = con.prepareStatement(sql);
                resultSet=state.executeQuery();
                while (resultSet.next()){
                    column=resultSet.getString(1);
                    log.debug(column);
                }
                end=System.currentTimeMillis();
                stat.recordMs(end-start);
                stat.recordSuccess();
            } catch (SQLException e) {
                LogUtil.printStackTrace(e);
                stat.recordFail();
            }
            finally {
                i++;
            }
        }
        stat.stop();
    }

}
