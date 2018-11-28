package cn.pcshao.graduaction.web;

import cn.pcshao.graduaction.service.UserService;
import cn.pcshao.graduaction.service.impl.UserServiceImpl;
import cn.pcshao.grant.common.base.BaseController;
import cn.pcshao.grant.common.consts.DtoCodeConsts;
import cn.pcshao.grant.common.dto.ResultDto;
import cn.pcshao.grant.common.entity.GrantUser;
import cn.pcshao.grant.common.util.ResultDtoFactory;
import cn.pcshao.grant.common.util.StringUtils;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController extends BaseController {

    @Autowired
    @Qualifier("userServiceImpl")
    private UserService userService;

    @ApiOperation("用户登录接口")
    @RequestMapping("/login")
    public ResultDto login(GrantUser grantUser){
        logger.info("some one ready to login in.");
        ResultDto resultDto = ResultDtoFactory.success();
        if(StringUtils.isNotEmpty(grantUser.getUsername()) && StringUtils.isNotEmpty(grantUser.getPassword())){
            Subject subject = SecurityUtils.getSubject();
            AuthenticationToken token = new UsernamePasswordToken(grantUser.getUsername(), grantUser.getPassword());
            try{
                subject.login(token);
                resultDto.setData(token);
                return resultDto;
            }catch (AuthenticationException e){
                //静默处理登录失败
            }
        }
        return ResultDtoFactory.error(DtoCodeConsts.LOGIN_FAILUR, DtoCodeConsts.LOGIN_FAILUR_MSG);
    }

}
