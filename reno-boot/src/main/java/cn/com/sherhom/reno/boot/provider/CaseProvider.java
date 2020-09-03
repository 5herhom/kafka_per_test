package cn.com.sherhom.reno.boot.provider;

public interface CaseProvider<T> {
    T next();
    boolean hasNext();
}
