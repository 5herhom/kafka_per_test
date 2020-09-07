package cn.com.sherhom.reno.boot.provider;

import cn.com.sherhom.reno.boot.function.StepForward;
/**
 * @author Sherhom
 * @date 2020/9/3 20:12
 */
public class RangeCaseProvider<T> extends AbstractCaseProvider<T> implements CaseProvider<T> {
    Long start;
    Long end;
    Long step;
    long cur=0;

    public RangeCaseProvider(Long start, Long end, Long step) {
        this.start = start;
        this.end = end;
        this.step = step;
        this.stepForward=(i)->(T)(Long.valueOf(i+this.step));
    }

    public RangeCaseProvider(StepForward<T,Long> stepForward, Long start, Long end, Long step) {
        super(stepForward);
        this.start = start;
        this.end = end;
        this.step = step;
    }

    @Override
    public T next() {
        T e=stepForward.goForward(cur);
        cur+=this.step;
        return e;
    }

    @Override
    public boolean hasNext() {
        return cur<=end;
    }

    @Override
    public void reset() {
        this.cur=start;
    }
}
