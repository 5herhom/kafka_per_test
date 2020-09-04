package cn.com.sherhom.reno.common.utils;

import cn.com.sherhom.reno.common.exception.RenoException;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;

/**
 * @author Sherhom
 * @date 2020/9/2 20:12
 */
public class Asset {
    public static void notNull(Object o,String msg) {
        if(o==null)
            throwException(msg);
    }
    public static void notEmpty(Collection<?> collection , String msg) {
        if(CollectionUtils.isEmpty(collection))
            throwException(msg);
    }
    public static void isNotBlank(String s,String msg) {
        if(!StringUtils.isNotBlank(s))
            throwException(msg);
    }
    public static void isTrue(boolean flag,String msg){
        if(!flag)
            throwException(msg);
    }
    public static void isFalse(boolean flag,String msg){
        if(flag)
            throwException(msg);
    }
    public static void isEqual(Object o1,Object o2,String msg){
        if(o1==null&&o2==null)
            return;
        if((o1==null)||!o1.equals(o2))
            throwException(msg);
    }
    public static void throwException(String msg){
        throw new RenoException(msg);
    }
}
