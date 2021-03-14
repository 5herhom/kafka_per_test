package cn.com.sherhom.reno.jdbc.simple.args;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Sherhom
 * @date 2021/2/7 16:38
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SqlInfo {
    String sqlTemplate;
    String sqlName;
    @Override
    public String toString(){
        return JSONObject.toJSONString(this);
    }
}
