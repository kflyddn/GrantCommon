package cn.pcshao.graduaction.web;

import cn.pcshao.graduaction.bo.SysPageBo;
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
import cn.pcshao.grant.common.util.MD5Utils;
import cn.pcshao.grant.common.util.ResultDtoFactory;
import cn.pcshao.grant.common.util.StringUtils;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
            //MD5解密
            grantUser.setPassword(MD5Utils.transMD5Code(grantUser.getPassword()));
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
    public ResultDto register(@RequestBody GrantUser grantUser, @RequestParam(required = false) List<Short> roleIdList){
        ResultDto resultDto = ResultDtoFactory.success();
        if(StringUtils.isNotEmpty(grantUser.getUsername()) && StringUtils.isNotEmpty(grantUser.getPassword())){
            if(ListUtils.isEmptyList(userService.listUsersByUserName(grantUser.getUsername()))){
                //MD5加密
                grantUser.setPassword(MD5Utils.transMD5Code(grantUser.getPassword()));
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
            if(ListUtils.isEmptyList(userService.listUsersByUserName(username))){
                return resultDto;
            }else{
                return ResultDtoFactory.error(DtoCodeConsts.USER_EXISTS, DtoCodeConsts.USER_EXISTS_MSG);
            }
        }
        return ResultDtoFactory.error();
    }

    @ApiOperation("删除用户接口")
    @PostMapping("/removeUser")
    public ResultDto removeUser(@RequestParam Long[] userIdList){
        ResultDto resultDto = ResultDtoFactory.success();
        //检查角色权限
        Subject subject = SecurityUtils.getSubject();
        try{
            subject.checkRole("admin");
        }catch (UnauthorizedException e){
            return ResultDtoFactory.error(DtoCodeConsts.NO_PERMISSION, DtoCodeConsts.NO_PERMISSION_MSG);
        }
        if(null != userIdList){
            int deleteNum = 0;
            for(Long l : userIdList) {
                deleteNum += userService.delete(l);
            }
            resultDto.setData(deleteNum);
            return resultDto;
        }
        return ResultDtoFactory.error();
    }

    @ApiOperation("查询用户接口（条件可选）")
    @ApiParam("查用户接口，只支持角色名和权限名参数查询")
    @PostMapping("/queryUser")
    public ResultDto queryUser(@RequestBody(required = false) SysPageBo sysPageBo){
        ResultDto resultDto = ResultDtoFactory.success();
        //检查角色权限
        Subject subject = SecurityUtils.getSubject();
        try{
            subject.checkRole("admin");
        }catch (UnauthorizedException e){
            return ResultDtoFactory.error(DtoCodeConsts.NO_PERMISSION, DtoCodeConsts.NO_PERMISSION_MSG);
        }
        int pageNum = 1;
        int pageSize = 8;
        if(null != sysPageBo && sysPageBo.checkSelf()){
            pageNum = sysPageBo.getPageNum();
            pageSize = sysPageBo.getPageSize();
        }
        PageHelper.startPage(pageNum, pageSize);
        GrantUser grantUser = sysPageBo.getGrantUser();
        String roleName = sysPageBo.getGrantRole() != null ? sysPageBo.getGrantRole().getRoleName() : "";
        String permissionName = sysPageBo.getGrantPermission() != null ? sysPageBo.getGrantPermission().getPermissionName() : "";
        List<GrantUser> grantUsers = userService.listUsers(grantUser, roleName, permissionName);
        if(null != grantUsers){
            resultDto.setData(grantUsers);
            if(grantUsers.size() == 0){
                resultDto.setMsg("无数据");
                return resultDto;
            }
            resultDto.setMsg("查询成功！"+grantUsers.size());
            return resultDto;
        }
        return ResultDtoFactory.error();
    }

    @ApiOperation("登出接口")
    @ApiParam("token参数留着给以后整合redis等用")
    @GetMapping("/logout")
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
    public ResultDto addRole(@RequestBody GrantRole grantRole, @RequestParam(required = false) List<Long> userIdList){
        ResultDto resultDto = ResultDtoFactory.success();
        //检查角色权限
        Subject subject = SecurityUtils.getSubject();
        try{
            subject.checkRole("admin");
        }catch (UnauthorizedException e){
            return ResultDtoFactory.error(DtoCodeConsts.NO_PERMISSION, DtoCodeConsts.NO_PERMISSION_MSG);
        }
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

    @ApiOperation("查询角色列表（条件可选）")
    @ApiParam("单一查角色接口")
    @PostMapping("/queryRole")
    public ResultDto queryRole(@RequestBody(required = false) SysPageBo sysPageBo){
        ResultDto resultDto = ResultDtoFactory.success();
        //检查角色权限
        Subject subject = SecurityUtils.getSubject();
        try{
            subject.checkRole("admin");
        }catch (UnauthorizedException e){
            return ResultDtoFactory.error(DtoCodeConsts.NO_PERMISSION, DtoCodeConsts.NO_PERMISSION_MSG);
        }
        int pageNum = 1;
        int pageSize = 8;
        if(null != sysPageBo && sysPageBo.checkSelf()){
            pageNum = sysPageBo.getPageNum();
            pageSize = sysPageBo.getPageSize();
        }
        PageHelper.startPage(pageNum, pageSize);
        GrantRole grantRole = sysPageBo.getGrantRole();
        List<GrantRole> grantRoles = roleService.listRoles(grantRole);
        if(null != grantRoles){
            resultDto.setData(grantRoles);
            if(grantRoles.size() == 0){
                resultDto.setMsg("无对应角色记录");
                return resultDto;
            }
            resultDto.setMsg("查询成功！"+grantRoles.size());
            return resultDto;
        }
        return ResultDtoFactory.error();
    }

    @ApiOperation("删除角色接口（成功则返回删除条数）")
    @PostMapping("/removeRole")
    public ResultDto deleteRole(@RequestParam Short[] roleIdList){
        ResultDto resultDto = ResultDtoFactory.success();
        //检查角色权限
        Subject subject = SecurityUtils.getSubject();
        try{
            subject.checkRole("admin");
        }catch (UnauthorizedException e){
            return ResultDtoFactory.error(DtoCodeConsts.NO_PERMISSION, DtoCodeConsts.NO_PERMISSION_MSG);
        }
        if(null != roleIdList){
            int deleteNum = 0;
            for(Short s : roleIdList) {
                //校验该角色是否对应有用户绑定
                if(ListUtils.isNotEmptyList(userService.listUserByRoleId(s))){
                    resultDto.setMsg("未全部删除，请检查！");
                }else{
                    deleteNum += roleService.delete(s);
                }
            }
            resultDto.setData(deleteNum);
            return resultDto;
        }
        return ResultDtoFactory.error();
    }

    @Autowired
    @Qualifier("permissionServiceImpl")
    private PermissionService permissionService;

    @ApiOperation("新增权限接口（可选带角色）")
    @PostMapping("/addPermission")
    public ResultDto addPermission(@RequestBody GrantPermission grantPermission, @RequestParam(required = false) List<Short> userRoleList){
        ResultDto resultDto = ResultDtoFactory.success();
        //检查角色权限
        Subject subject = SecurityUtils.getSubject();
        try{
            subject.checkRole("admin");
        }catch (UnauthorizedException e){
            return ResultDtoFactory.error(DtoCodeConsts.NO_PERMISSION, DtoCodeConsts.NO_PERMISSION_MSG);
        }
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

    @ApiOperation("查询权限列表（条件可选）")
    @ApiParam("单一查权限接口")
    @PostMapping("/queryPermission")
    public ResultDto queryPermission(@RequestBody(required = false) SysPageBo sysPageBo){
        ResultDto resultDto = ResultDtoFactory.success();
        //检查角色权限
        Subject subject = SecurityUtils.getSubject();
        try{
            subject.checkRole("admin");
        }catch (UnauthorizedException e){
            return ResultDtoFactory.error(DtoCodeConsts.NO_PERMISSION, DtoCodeConsts.NO_PERMISSION_MSG);
        }
        int pageNum = 1;
        int pageSize = 8;
        if(null != sysPageBo && sysPageBo.checkSelf()){
            pageNum = sysPageBo.getPageNum();
            pageSize = sysPageBo.getPageSize();
        }
        PageHelper.startPage(pageNum, pageSize);
        GrantPermission grantPermission = sysPageBo.getGrantPermission();
        List<GrantPermission> grantPermissions = permissionService.listPermissions(grantPermission);
        if(null != grantPermissions){
            resultDto.setData(grantPermissions);
            if(grantPermissions.size() == 0){
                resultDto.setMsg("无对应权限记录");
                return resultDto;
            }
            resultDto.setMsg("查询成功！"+grantPermissions.size());
            return resultDto;
        }
        return ResultDtoFactory.error();
    }

    @ApiOperation("删除权限接口，（成功则返回删除条数）")
    @PostMapping("/removePermission")
    public ResultDto deletePermission(@RequestParam Long[] permissionIdList){
        ResultDto resultDto = ResultDtoFactory.success();
        //检查角色权限
        Subject subject = SecurityUtils.getSubject();
        try{
            subject.checkRole("admin");
        }catch (UnauthorizedException e){
            return ResultDtoFactory.error(DtoCodeConsts.NO_PERMISSION, DtoCodeConsts.NO_PERMISSION_MSG);
        }
        if(null != permissionIdList){
            int deleteNum = 0;
            for(Long l : permissionIdList) {
                //校验该角色是否对应有用户绑定
                if(ListUtils.isNotEmptyList(roleService.listRolesByPermissionId(l))){
                    resultDto.setMsg("未全部删除，请检查！");
                }else {
                    deleteNum += permissionService.delete(l);
                }
            }
            resultDto.setData(deleteNum);
            return resultDto;
        }
        return ResultDtoFactory.error();
    }

    @ApiOperation("授权接口 给单个用户多个角色")
    @PostMapping("bindUserRoles")
    public ResultDto bindUserRoles(@RequestParam Long userId, @RequestParam List<Short> roleIdList){
        ResultDto resultDto = ResultDtoFactory.success();
        //检查角色权限
        Subject subject = SecurityUtils.getSubject();
        try{
            subject.checkRole("admin");
        }catch (UnauthorizedException e){
            return ResultDtoFactory.error(DtoCodeConsts.NO_PERMISSION, DtoCodeConsts.NO_PERMISSION_MSG);
        }
        if(null != userId && ListUtils.isNotEmptyList(roleIdList)){
            userService.bindUserRoles(userId, roleIdList);
            return resultDto;
        }
        return ResultDtoFactory.error();
    }

    @ApiOperation("授权接口 给单个角色多个用户")
    @PostMapping("bindRoleUsers")
    public ResultDto bindRoleUsers(@RequestParam Short roleId, @RequestParam List<Long> userIdList){
        ResultDto resultDto = ResultDtoFactory.success();
        //检查角色权限
        Subject subject = SecurityUtils.getSubject();
        try{
            subject.checkRole("admin");
        }catch (UnauthorizedException e){
            return ResultDtoFactory.error(DtoCodeConsts.NO_PERMISSION, DtoCodeConsts.NO_PERMISSION_MSG);
        }
        if(null != roleId && ListUtils.isNotEmptyList(userIdList)){
            roleService.bindRoleUsers(roleId, userIdList);
            return resultDto;
        }
        return ResultDtoFactory.error();
    }

    @ApiOperation("授权接口 给单个角色多个权限")
    @PostMapping("bindRolePermissions")
    public ResultDto bindRolePermissions(@RequestParam Short roleId, @RequestParam List<Long> permissionIdList){
        ResultDto resultDto = ResultDtoFactory.success();
        //检查角色权限
        Subject subject = SecurityUtils.getSubject();
        try{
            subject.checkRole("admin");
        }catch (UnauthorizedException e){
            return ResultDtoFactory.error(DtoCodeConsts.NO_PERMISSION, DtoCodeConsts.NO_PERMISSION_MSG);
        }
        if(null != roleId && ListUtils.isNotEmptyList(permissionIdList)) {
            roleService.bindRolePermissions(roleId, permissionIdList);
            return resultDto;
        }
        return ResultDtoFactory.error();
    }

    @ApiOperation("授权接口 给单个权限多个用户")
    @PostMapping("bindPermissionRoles")
    public ResultDto bindPermissionRoles(@RequestParam Long permissionId, @RequestParam List<Short> roleIdList){
        ResultDto resultDto = ResultDtoFactory.success();
        //检查角色权限
        Subject subject = SecurityUtils.getSubject();
        try{
            subject.checkRole("admin");
        }catch (UnauthorizedException e){
            return ResultDtoFactory.error(DtoCodeConsts.NO_PERMISSION, DtoCodeConsts.NO_PERMISSION_MSG);
        }
        if(null != permissionId && ListUtils.isNotEmptyList(roleIdList)) {
            permissionService.bindPermissionRoles(permissionId, roleIdList);
            return resultDto;
        }
        return ResultDtoFactory.error();
    }

}
