package cn.pcshao.graduaction.web;

import cn.pcshao.graduaction.service.PermissionService;
import cn.pcshao.graduaction.service.RoleService;
import cn.pcshao.graduaction.service.UserService;
import cn.pcshao.grant.common.base.BaseController;
import cn.pcshao.grant.common.consts.DtoCodeConsts;
import cn.pcshao.grant.common.dto.ResultDto;
import cn.pcshao.grant.common.entity.GrantPermission;
import cn.pcshao.grant.common.entity.GrantRole;
import cn.pcshao.grant.common.entity.GrantUser;
import cn.pcshao.grant.common.util.ListUtils;
import cn.pcshao.grant.common.util.ResultDtoFactory;
import cn.pcshao.grant.common.util.StringUtils;
import io.swagger.annotations.Api;
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

    @ApiOperation("用户注册接口 用户新增接口（带角色）")
    @ApiParam("用户对象与用户角色列表，角色列表默认为2 normal")
    @PostMapping("/register")
    public ResultDto register(@RequestBody GrantUser grantUser, @RequestParam(required = false) ArrayList<Short> roleIdList){
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
    public ResultDto removeUser(@RequestParam Long[] userId){
        ResultDto resultDto = ResultDtoFactory.success();
        if(null != userId){
            int deleteNum = 0;
            for(Long l : userId) {
                deleteNum += userService.delete(l);
            }
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

    @Autowired
    @Qualifier("roleServiceImpl")
    private RoleService roleService;

    @ApiOperation("新增角色接口（可选带用户）")
    @PostMapping("/addRole")
    public ResultDto addRole(@RequestBody GrantRole grantRole, @RequestParam(required = false) ArrayList<Long> userIdList){
        ResultDto resultDto = ResultDtoFactory.success();
        if(StringUtils.isNotEmpty(grantRole.getRoleName()) && StringUtils.isNotEmpty(grantRole.getRoleRemark())){
            if(!roleService.findRoleByName(grantRole.getRoleName())){
                roleService.saveRole(grantRole, userIdList);
                return resultDto;
            }else{
                return ResultDtoFactory.error(DtoCodeConsts.ROLE_EXISTS, DtoCodeConsts.ROLE_EXISTS_MSG);
            }
        }
        return ResultDtoFactory.error();
    }

    public ResultDto queryRole(){
        ResultDto resultDto = ResultDtoFactory.success();
        return ResultDtoFactory.error();
    }

    public ResultDto deleteRole(@RequestBody GrantRole[] grantRole){
        ResultDto resultDto = ResultDtoFactory.success();
        return ResultDtoFactory.error();
    }

    @Autowired
    @Qualifier("permissionServiceImpl")
    private PermissionService permissionService;

    @ApiOperation("授权接口 给单个用户多个角色")
    @PostMapping("bindUserRoles")
    public ResultDto bindUserRoles(@RequestParam Long userId, @RequestParam ArrayList<Short> roleIdList){
        ResultDto resultDto = ResultDtoFactory.success();
        if(null != userId && ListUtils.isNotEmptyList(roleIdList)){
            userService.bindUserRoles(userId, roleIdList);
        }
        return ResultDtoFactory.error();
    }

    @ApiOperation("授权接口 给单个角色多个用户")
    @PostMapping("bindRoleUsers")
    public ResultDto bindRoleUsers(@RequestParam Short roleId, @RequestParam ArrayList<Long> userIdList){
        ResultDto resultDto = ResultDtoFactory.success();
        if(null != roleId && ListUtils.isNotEmptyList(userIdList)){
            roleService.bindRoleUsers(roleId, userIdList);
            return resultDto;
        }
        return ResultDtoFactory.error();
    }

    @ApiOperation("新增权限接口（可选带角色）")
    @PostMapping("/addPermission")
    public ResultDto addPermission(@RequestBody GrantPermission grantPermission, @RequestParam(required = false) ArrayList<Short> userRoleList){
        ResultDto resultDto = ResultDtoFactory.success();
        if(StringUtils.isNotEmpty(grantPermission.getPermissionName())){
            if(!permissionService.findPermissionByName(grantPermission.getPermissionName())){
                permissionService.savePermission(grantPermission, userRoleList);
                return resultDto;
            }else{
                return ResultDtoFactory.error(DtoCodeConsts.ROLE_EXISTS, DtoCodeConsts.ROLE_EXISTS_MSG);
            }
        }
        return ResultDtoFactory.error();
    }

    public ResultDto queryPermission(){
        ResultDto resultDto = ResultDtoFactory.success();
        return ResultDtoFactory.error();
    }

    public ResultDto deletePermission(@RequestBody GrantPermission[] grantPermission){
        ResultDto resultDto = ResultDtoFactory.success();
        return ResultDtoFactory.error();
    }

    @ApiOperation("授权接口 给单个角色多个权限")
    @PostMapping("bindRolePermissions")
    public ResultDto bindRolePermissions(@RequestParam Short roleId, @RequestParam ArrayList<Long> permissionIdList){
        ResultDto resultDto = ResultDtoFactory.success();
        if(null != roleId && ListUtils.isNotEmptyList(permissionIdList)) {
            roleService.bindRolePermissions(roleId, permissionIdList);
        }
        return ResultDtoFactory.error();
    }

    @ApiOperation("授权接口 给单个权限多个用户")
    @PostMapping("bindPermissionRoles")
    public ResultDto bindPermissionRoles(@RequestParam Long permissionId, @RequestParam ArrayList<Short> roleIdList){
        ResultDto resultDto = ResultDtoFactory.success();
        if(null != permissionId && ListUtils.isNotEmptyList(roleIdList)) {
            permissionService.bindPermissionRoles(permissionId, roleIdList);
        }
        return ResultDtoFactory.error();
    }

}
