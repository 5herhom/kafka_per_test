package cn.com.sherhom.reno.kafka.common.net;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Sherhom
 * @date 2020/9/7 15:33
 */
@Getter
@AllArgsConstructor
public class JSONRequestMethod {
    Class clazz;
    Object obj;
    Method method;
    Class paramClass;

    public Object doMethod(String jsonStr) {
        try {
            return method.invoke(JSON.parseObject(jsonStr,paramClass));
        } catch (IllegalAccessException e) {
            e.printStackTrace();

        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

}
