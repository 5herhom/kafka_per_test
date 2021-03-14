package cn.com.sherhom.reno.jdbc.common.utils;

import cn.com.sherhom.reno.jdbc.common.conf.JdbcConf;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * @author Sherhom
 * @date 2021/1/11 14:20
 */
public class JdbcUtils {
    private static String driver = null;
    private static String url = null;
    private static String username = null;
    private static String password = null;

    static{
        try{
            driver = JdbcConf.getJdbcDriver();
            url = JdbcConf.getJdbcUrl();
            username = JdbcConf.getJdbcUsername();
            password = JdbcConf.getJdbcPassword();

            Class.forName(driver);

        }catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }


    public static Connection getConnection() throws SQLException {

        return DriverManager.getConnection(url, username,password);
    }

    public static void release(Connection conn, Statement st, ResultSet rs){

        if(rs!=null){
            try{
                rs.close();
            }catch (Exception e) {
                e.printStackTrace();
            }
            rs = null;

        }
        if(st!=null){
            try{
                st.close();
            }catch (Exception e) {
                e.printStackTrace();
            }

        }

        if(conn!=null){
            try{
                conn.close();
            }catch (Exception e) {
                e.printStackTrace();
            }

        }

    }
}
