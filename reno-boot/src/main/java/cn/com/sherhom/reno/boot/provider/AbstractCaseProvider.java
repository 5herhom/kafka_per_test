package cn.com.sherhom.reno.boot.provider;

import cn.com.sherhom.reno.boot.function.StepForward;

public abstract class AbstractCaseProvider<T> implements CaseProvider<T> {
    protected StepForward<T> stepForward;

    public AbstractCaseProvider() {
    }

    public AbstractCaseProvider(StepForward stepForward) {
        this.stepForward = stepForward;
    }
}
