package cn.com.sherhom.reno.boot;

import cn.com.sherhom.reno.boot.annonation.ToExplore;
import cn.com.sherhom.reno.boot.provider.CaseProvider;
import cn.com.sherhom.reno.boot.provider.ProviderBuilder;
import cn.com.sherhom.reno.common.exception.RenoException;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class RenoApplication {
    public static void main(String[] args) {

    }

    public static void run(Object fairyland, List<CaseProvider> providers) {
        if(fairyland==null){
            log.error("Fairyland is null.");
            throw new RenoException("Fairyland is null.");
        }
        Method[] methods=fairyland.getClass().getDeclaredMethods();
        Method methodToExplore=null;
        for (Method method:methods
             ) {
            if(method.getParameterCount()!=providers.size())
                continue;
            Annotation annotation=method.getAnnotation(ToExplore.class);
            if(annotation!=null){
                methodToExplore=method;
                break;
            }
        }
        if(methodToExplore==null){
            log.error("No method to explore");
            throw new RenoException("No method to explore");
        }
        log.info("Method {} to explore.",methodToExplore.getName());
        run(fairyland,methodToExplore,providers);
    }

    public static void run(Object fairyland,Method methodToExplore,List<CaseProvider> providers){
        int deep=0;
        if(deep==providers.size()){
            methodToExplore.setAccessible(true);
            try {
                methodToExplore.invoke(fairyland);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            finally {
                return;
            }
        }
        List args=new ArrayList();
        run(fairyland,methodToExplore,providers,args,deep);
    }
    public static void run(Object fairyland, Method methodToExplore, List<CaseProvider> providers, List<Object> args, int deep){
        if(deep==providers.size()){
            methodToExplore.setAccessible(true);
            try {
                methodToExplore.invoke(fairyland,args);
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    //TODO handle exception
                }
                catch (Exception e1){
                    e.printStackTrace();
                    throw new RenoException(e1);
                }
            }
        }
        CaseProvider provider=providers.get(deep-1);
        while (provider.hasNext()){
            args.add(provider.next());
            run(fairyland,methodToExplore,providers,args,deep);
        }
    }
}
