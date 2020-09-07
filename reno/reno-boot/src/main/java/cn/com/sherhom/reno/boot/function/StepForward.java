package cn.com.sherhom.reno.boot.function;
/**
 * @author Sherhom
 * @date 2020/9/3 20:12
 */
@FunctionalInterface
public interface StepForward<R,T> {
    R goForward(T index);
}
