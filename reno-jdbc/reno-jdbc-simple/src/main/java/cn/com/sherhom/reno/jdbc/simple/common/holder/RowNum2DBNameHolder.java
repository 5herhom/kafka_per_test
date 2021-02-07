package cn.com.sherhom.reno.jdbc.simple.common.holder;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sherhom
 * @date 2021/2/7 17:39
 */
public class RowNum2DBNameHolder {
    static Map<Long,String> rowNumAndDBNameMapper =new HashMap<Long, String>(){
        {
            put(5l,"test5");
            put(100l,"test100");
        }
    };
    public static String getSqlFromRowNum(String sqlTemplate,Long dataSize){
        return MessageFormat.format(sqlTemplate,rowNumAndDBNameMapper.get(dataSize));
    }
}
