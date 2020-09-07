package cn.com.sherhom.reno.boot.provider;

import java.util.Iterator;
import java.util.List;
/**
 * @author Sherhom
 * @date 2020/9/3 20:12
 */
public class ListCaseProvider<T> implements CaseProvider<T> {
    List<T> cases;
    Iterator<T> it;
    public ListCaseProvider(List cases) {
        this.cases = cases;
        it=cases.iterator();
    }

    @Override
    public T next() {
        return it.next();
    }

    @Override
    public boolean hasNext() {
        return it.hasNext();
    }

    @Override
    public void reset() {
        this.it=cases.iterator();
    }
}
