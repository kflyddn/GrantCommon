package cn.pcshao.grant.common.aop;

import cn.pcshao.grant.common.dao.GrantLogMapper;
import cn.pcshao.grant.common.dto.ResultDto;
import cn.pcshao.grant.common.entity.GrantLog;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author pcshao.cn
 * @date 2019/1/16
 */
@Aspect
@Component
public class LogAspect {

    private Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Resource
    private GrantLogMapper logMapper;

    //整个表达式可以分为五个部分：
    // 1、execution(): 表达式主体。
    // 2、第一个*号：表示返回类型，*号表示所有的类型。
    // 3、包名：表示需要拦截的包名，后面的两个句点表示当前包和当前包的所有子包，com.sample.service.impl包、子孙包下所有类的方法。
    // 4、第二个*号：表示类名，*号表示所有的类。
    // 5、*(..):最后这个星号表示方法名，*号表示所有的方法，后面括弧里面表示方法的参数，两个句点表示任何参数。
    @Pointcut("@annotation(cn.pcshao.grant.common.aop.LogAnnotation)")
    public void LogPoint(){

    }

    @AfterReturning(returning = "ret", pointcut = "LogPoint() && @annotation(logAnnotation)")
    public void doAfterReturning(ResultDto ret, LogAnnotation logAnnotation){
        String value = logAnnotation.value()+ "-"+ ret.getMsg();
        Subject subject = SecurityUtils.getSubject();
        String currUsername = (String) subject.getPrincipal();
        Session session = subject.getSession();
        GrantLog log = new GrantLog();
        //TODO 日志字段缺省
        log.setUserId(null);
        log.setType(null);
        log.setUserName(currUsername);
        log.setIpAddress(session.getHost());
        log.setTime(new Date());
        log.setTimeOut(session.getTimeout()/1000);
        log.setLastAccessTime(session.getLastAccessTime());
        log.setContent(value);
        logMapper.insert(log);
    }

}
