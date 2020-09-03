package cn.com.sherhom.reno.boot.provider;

import cn.com.sherhom.reno.boot.function.StepForward;

public class RangeCaseProvider<T> extends AbstractCaseProvider<T> implements CaseProvider<T> {
    Long start;
    Long end;
    Long step;
    long cur=0;

    public RangeCaseProvider(Long start, Long end, Long step) {
        this.start = start;
        this.end = end;
        this.step = step;
        this.stepForward=()->(T)(Long.valueOf(this.cur+this.step));
    }

    public RangeCaseProvider(StepForward<T> stepForward, Long start, Long end, Long step) {
        super(stepForward);
        this.start = start;
        this.end = end;
        this.step = step;
    }

    @Override
    public T next() {
        return stepForward.goForward();
    }

    @Override
    public boolean hasNext() {
        return cur<=end;
    }
}
