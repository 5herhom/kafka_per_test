package cn.com.sherhom.reno.boot.provider;

import cn.com.sherhom.reno.boot.function.StepForward;
/**
 * @author Sherhom
 * @date 2020/9/3 20:12
 */
public abstract class AbstractCaseProvider<T> implements CaseProvider<T> {
    protected StepForward<T,Long> stepForward;

    public AbstractCaseProvider() {
    }

    public AbstractCaseProvider(StepForward stepForward) {
        this.stepForward = stepForward;
    }
}
