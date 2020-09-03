package cn.com.sherhom.reno.boot.function;

@FunctionalInterface
public interface StepForward<R,T> {
    R goForward(T index);
}
