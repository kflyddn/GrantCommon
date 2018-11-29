package cn.pcshao.graduaction.web;

import cn.pcshao.graduaction.service.UserService;
import cn.pcshao.grant.common.base.BaseController;
import cn.pcshao.grant.common.consts.DtoCodeConsts;
import cn.pcshao.grant.common.dto.ResultDto;
import cn.pcshao.grant.common.entity.GrantUser;
import cn.pcshao.grant.common.util.ListUtils;
import cn.pcshao.grant.common.util.ResultDtoFactory;
import cn.pcshao.grant.common.util.StringUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

/**
 * 用户相关控制器
 * @author pcshao
 * @date 2018-11-27
 */
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    @Autowired
    @Qualifier("userServiceImpl")
    private UserService userService;


    @ApiOperation("用户登录接口")
    @PostMapping("/login")
    public ResultDto login(@RequestBody GrantUser grantUser){
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

    @ApiOperation("用户注册接口")
    @ApiParam("用户对象与用户角色列表，角色列表默认为2 normal")
    @PostMapping("/register")
    public ResultDto register(@RequestBody GrantUser grantUser, @RequestParam ArrayList<Short> roleIdList){
        ResultDto resultDto = ResultDtoFactory.success();
        if(StringUtils.isNotEmpty(grantUser.getUsername()) && StringUtils.isNotEmpty(grantUser.getPassword())){
            //角色 admin 1 normal 2
            if(ListUtils.isEmptyList(roleIdList)){
                roleIdList.add((short)2);
            }
            if(!userService.findByUserName(grantUser.getUsername())){
                userService.saveUser(grantUser, roleIdList);
                return resultDto;
            }
        }
        return ResultDtoFactory.error();
    }

    @ApiOperation("检查用户名是否已存在")
    @GetMapping("/checkUserName")
    public ResultDto checkUserName(@RequestParam String username){
        ResultDto resultDto = ResultDtoFactory.success();
        if(StringUtils.isNotEmpty(username)){
            if(!userService.findByUserName(username)){
                return resultDto;
            }else{
                return ResultDtoFactory.error(DtoCodeConsts.USER_EXISTS, DtoCodeConsts.USER_EXISTS_MSG);
            }
        }
        return ResultDtoFactory.error();
    }

    @ApiOperation("删除用户接口")
    @PostMapping("/removeUser")
    public ResultDto removeUser(@RequestParam Long userId){
        ResultDto resultDto = ResultDtoFactory.success();
        if(null != userId){
            int deleteNum = userService.delete(userId);
            resultDto.setData(deleteNum);
            return resultDto;
        }
        return ResultDtoFactory.error();
    }

    @ApiOperation("登出接口")
    @ApiParam("token参数留着给以后整合redis等用")
    @RequestMapping("/logout")
    public ResultDto logout(@RequestParam(required = false) String token){
        ResultDto resultDto = ResultDtoFactory.success();
        Subject subject = SecurityUtils.getSubject();
        try{
            subject.logout();
            resultDto.setMsg("登出成功！");
            return resultDto;
        }catch (Exception e){

        }
        return ResultDtoFactory.error();
    }

}
