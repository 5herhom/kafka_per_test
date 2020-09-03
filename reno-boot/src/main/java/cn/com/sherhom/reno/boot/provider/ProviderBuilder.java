package cn.com.sherhom.reno.boot.provider;

import cn.com.sherhom.reno.boot.function.StepForward;
import cn.com.sherhom.reno.common.exception.RenoException;
import cn.com.sherhom.reno.common.utils.Asset;

import java.util.List;

import static cn.com.sherhom.reno.boot.provider.ProviderBuilder.ProviderType.*;


public class ProviderBuilder<T> {
    public ProviderBuilder(){

    }
    ProviderType providerType;

    T value;

    List<T> cases;

    Long start;
    Long end;
    Long step;
    StepForward<T,Long> stepForward;
    public CaseProvider<T> build(){
        switch (this.providerType){
            case VALUE:
                return new ValueCaseProvider<>(value);
            case LIST:
                return new ListCaseProvider<>(cases);
            case RANGE:
                return this.stepForward==null?new RangeCaseProvider<>(start,end,step):
                        new RangeCaseProvider<>(this.stepForward,start,end,step);
            default:
                throw new RenoException("Please enter current type.");
        }
    }
    public ProviderBuilder<T> fieldType(ProviderType providerType){
        this.providerType = providerType;
        return this;
    }
    public ProviderBuilder<T> value(T value){
        checkType(VALUE);
        this.value=value;
        return this;
    }
    public ProviderBuilder<T> cases(List<T> cases){
        checkType(LIST);
        this.cases=cases;
        return this;
    }
    public ProviderBuilder<T> start(Long start){
        checkType(RANGE);
        this.start=start;
        return this;
    }
    public ProviderBuilder<T> end(Long end){
        checkType(RANGE);
        this.end=end;
        return this;
    }
    public ProviderBuilder<T> step(Long step){
        checkType(RANGE);
        this.step=step;
        return this;
    }
    public ProviderBuilder<T> stepForward(StepForward<T,Long> stepForward){
        checkType(RANGE);
        this.stepForward=stepForward;
        return this;
    }
    private void checkType(ProviderType exp){
        Asset.isEqual(this.providerType,exp,typeErrorMsg(this.providerType,exp));

    }
    private String typeErrorMsg(ProviderType f1, ProviderType f2){
        return String.format("fieldType must be %s,but is %s",f1,f2);
    }
    public enum ProviderType {
        VALUE,LIST,RANGE,FUNCTION
    }
}
