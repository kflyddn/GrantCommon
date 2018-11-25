package cn.pcshao.graduaction.web;

import cn.pcshao.graduaction.service.UserService;
import cn.pcshao.graduaction.service.impl.UserServiceImpl;
import cn.pcshao.grant.common.base.BaseController;
import cn.pcshao.grant.common.consts.DtoCodeConsts;
import cn.pcshao.grant.common.dto.ResultDto;
import cn.pcshao.grant.common.entity.GrantUser;
import cn.pcshao.grant.common.util.ResultDtoFactory;
import io.swagger.annotations.ApiOperation;
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
    @RequestMapping
    public ResultDto login(GrantUser grantUser){
        ResultDto resultDto = ResultDtoFactory.success();
        String username = grantUser.getUsername();
        String password = grantUser.getPassword();
        int ret = userService.doAuth(username, password);
        if(ret == 0){
            resultDto = ResultDtoFactory.error(DtoCodeConsts.LOGIN_FAILUR, DtoCodeConsts.LOGIN_FAILUR_MSG);
        }
        return resultDto;
    }

}
