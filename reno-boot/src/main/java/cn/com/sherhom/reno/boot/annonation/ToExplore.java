package cn.com.sherhom.reno.boot.annonation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * @author Sherhom
 * @date 2020/9/3 20:12
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ToExplore {
}
