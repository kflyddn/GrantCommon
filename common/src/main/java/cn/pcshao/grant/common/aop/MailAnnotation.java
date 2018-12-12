package cn.pcshao.grant.common.aop;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author pcshao.cn
 * @date 2018/12/12
 */
@Documented //包含造javadoc
@Retention(RetentionPolicy.RUNTIME) //注解保留策略，class字节码文件中存在，运行时可以通过反射获取到
@Target({ElementType.METHOD}) //作用域
@Inherited //子类可以继承父类中该注解
public @interface MailAnnotation {
    /**
     * 如果注解只有一个属性，那么肯定是赋值给该属性。
     * 如果注解有多个属性，而且前提是这多个属性都有默认值，那么你不写注解名赋值，会赋值给名字为“value”这属性。
     * 如果注解有多个属性，其中有没有设置默认值的属性，那么当你不写属性名进行赋值的时候，是会报错的。
     */
    /*@AliasFor("message")*/
    String value();

    String[] toMailAddress() default "1195350307@qq.com";
}
