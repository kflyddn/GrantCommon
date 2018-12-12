package cn.pcshao.grant.common.aop;

import cn.pcshao.grant.common.consts.DtoCodeConsts;
import cn.pcshao.grant.common.dto.ResultDto;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Album新增资源后发送邮件切面
 * @author pcshao.cn
 * @date 2018/12/12
 */
@Aspect
@Component
@PropertySource({"classpath:mail-config.properties"})
public class AddSource2MailAspect {

    private Logger logger = LoggerFactory.getLogger(AddSource2MailAspect.class);

    @Value("${mail.fromAddress}")
    private String mail_from_address;

    @Value("${mail.subject}")
    private String mail_subject;

    @Value("${mail.text}")
    private String mail_text;

    @Resource
    private JavaMailSender mailSender;

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
    public void doAfterReturning(ResultDto ret, MailAnnotation mailAnnotation){
        //TODO 后期建议改成消息队列，积累到一定量的资源上传再向管理员发送邮件
        if(ret.getCode() != DtoCodeConsts.VIEW_SUCCESS){
            return;
        }
        String[] data = ret.getData().toString().split("-");
        logger.info("开始邮件发送服务！");
        logger.info("当前影响操作的用户名："+ data[0]);

        //建立邮件消息
        SimpleMailMessage mainMessage = new SimpleMailMessage();
        mainMessage.setFrom(mail_from_address);
        mainMessage.setTo(mailAnnotation.toMailAddress());
        //发送的标题
        mainMessage.setSubject(mail_subject);
        //发送的内容
        mainMessage.setText(mail_text+ "用户名："+ data[0]+ data[1]);
        logger.info("邮件："+ " from:"+ mail_from_address+ " to:"+ mailAnnotation.toMailAddress()+ " subject:"+ mail_subject);
        try {
            mailSender.send(mainMessage);
        }catch (Exception e){
            e.printStackTrace();
            logger.info("邮件发送异常！");
        }
        logger.info("邮件发送服务结束！");
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
