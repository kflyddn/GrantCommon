package cn.pcshao.grant.common.exception;

import cn.pcshao.grant.common.consts.DtoCodeConsts;
import cn.pcshao.grant.common.util.ResultDtoFactory;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author pcshao.cn
 * @date 2018-11-24
 * WEB项目全局捕获异常类
 *  可以放置到BaseController中也可以@ControllerAdvice
 *
 */
@ControllerAdvice
public class GlobalException {

    private final Logger logger = LoggerFactory.getLogger(GlobalException.class);

    private CustomException customException;

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Object HandlerError(Exception e){
        logger.error(e.getMessage(), e);
        return getExceptionMsg(e);
    }
    /**
     * 适配抛出的异常
     * @param e
     */
    private Object getExceptionMsg(Exception e){
        if(e instanceof CustomException){
            //将异常转成自定义页面异常作处理
            customException = (CustomException) e;
            return ResultDtoFactory.error(customException.getCode(), customException.getMessage());
        }else if (e instanceof UnauthorizedException){
            //使用shiro注解抛出的验证失败异常
            return ResultDtoFactory.error(DtoCodeConsts.NO_PERMISSION, DtoCodeConsts.NO_PERMISSION_MSG);
        }
        return e.getMessage();
    }
}
