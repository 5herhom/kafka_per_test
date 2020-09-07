package cn.com.sherhom.reno.boot.provider;
/**
 * @author Sherhom
 * @date 2020/9/3 20:12
 */
public interface CaseProvider<T> {
    T next();
    boolean hasNext();
    void reset();
}
