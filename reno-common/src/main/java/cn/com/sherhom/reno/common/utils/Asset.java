package cn.com.sherhom.reno.common.utils;

import cn.com.sherhom.reno.common.exception.RenoException;

public class Asset {
    public static void throwException(String msg){
        throw new RenoException(msg);
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
        if((o1==null||o2==null)||!o1.equals(o2))
            throwException(msg);
    }
}
