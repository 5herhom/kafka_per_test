package cn.com.sherhom.reno.boot.provider;

public class ValueCaseProvider<T> extends AbstractCaseProvider<T> implements CaseProvider<T> {

    T value;

    public ValueCaseProvider(T value) {
        this.value = value;
    }

    @Override
    public T next() {
        return value;
    }

    @Override
    public boolean hasNext() {
        return false;
    }
}
