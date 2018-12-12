package cn.pcshao.grant.common.aop;

import cn.pcshao.grant.common.dto.ResultDto;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Album新增资源后发送邮件切面
 * @author pcshao.cn
 * @date 2018/12/12
 */
@Aspect
@Component
public class AddSource2MailAspect {

    private Logger logger = LoggerFactory.getLogger(AddSource2MailAspect.class);

    /**
     * 定义一个切点，切在注解上
     */
    @Pointcut("@annotation(cn.pcshao.grant.common.aop.MailAnnotation)")
    public void MailAnnotation(){

    }

   /* @Before("MailAnnotation()")
    public void doBefore(ProceedingJoinPoint joinPoint){
        logger.info("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
    }*/

    @AfterReturning(returning = "ret", pointcut = "MailAnnotation() && @annotation(mailAnnotation)")
    public void doAfterReturning(ResultDto ret, MailAnnotation mailAnnotation) throws Throwable {
        //TODO 发送邮件
        String username = ret.getData().toString().split("-")[0];
        logger.info("方法的返回值 : " + ret);
        logger.info("用户名："+ username);
    }

    /*
    @AfterThrowing("MailAnnotation()")
    public void throwss(JoinPoint jp){
        logger.info("方法异常时执行.....");
    }

    @After("MailAnnotation()")
    public void after(JoinPoint jp){
        logger.info("方法最后执行.....");
    }*/

    /*@Around(value = "MailAnnotation() && @annotation(mailAnnotation)")
    public Object doArround(ProceedingJoinPoint joinPoint, MailAnnotation mailAnnotation){
        logger.info("方法环绕通知开始..");
        try {
            //环绕通知必须执行，否则不进入注解的方法
            Object object = joinPoint.proceed();
            //logger.info("注解上的value为："+ mailAnnotation.value());
            //logger.info("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
            logger.info("环绕通知结束，内容是"+ object);
            return object;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        }
    }*/
}
