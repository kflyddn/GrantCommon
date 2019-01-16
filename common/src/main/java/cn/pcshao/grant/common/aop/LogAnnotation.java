package cn.pcshao.grant.common.aop;

import java.lang.annotation.*;

/**
 * @author pcshao.cn
 * @date 2019/1/16
 */
@Documented //包含造javadoc
@Retention(RetentionPolicy.RUNTIME) //注解保留策略，class字节码文件中存在，运行时可以通过反射获取到
@Target({ElementType.TYPE, ElementType.METHOD}) //作用域
@Inherited //子类可以继承父类中该注解
public @interface LogAnnotation {

    String value() default "";

}
