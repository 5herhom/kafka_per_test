package cn.com.sherhom.reno.common.utils;



import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CollectionUtils {
    public static boolean isEmpty(  Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isEmpty(  Map<?, ?> map) {
        return map == null || map.isEmpty();
    }


}
