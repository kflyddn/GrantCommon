package cn.pcshao.graduaction.web;

import cn.pcshao.graduaction.bo.SysPageBo;
import cn.pcshao.graduaction.service.PermissionService;
import cn.pcshao.graduaction.service.RoleService;
import cn.pcshao.graduaction.service.UserService;
import cn.pcshao.grant.common.aop.LogAnnotation;
import cn.pcshao.grant.common.base.BaseController;
import cn.pcshao.grant.common.consts.DtoCodeConsts;
import cn.pcshao.grant.common.dto.ResultDto;
import cn.pcshao.grant.common.entity.GrantPermission;
import cn.pcshao.grant.common.entity.GrantRole;
import cn.pcshao.grant.common.entity.GrantUser;
import cn.pcshao.grant.common.exception.CustomException;
import cn.pcshao.grant.common.util.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
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
    @LogAnnotation("用户登录")
    public ResultDto login(HttpServletRequest request, @RequestBody GrantUser grantUser){
        ResultDto resultDto = ResultDtoFactory.success();
        if(StringUtils.isNotEmpty(grantUser.getUsername()) && StringUtils.isNotEmpty(grantUser.getPassword())){
            //MD5解密
            grantUser.setPassword(MD5Utils.transMD5Code(grantUser.getPassword()));
            Subject subject = SecurityUtils.getSubject();
            AuthenticationToken token = new UsernamePasswordToken(grantUser.getUsername(), grantUser.getPassword());
            try{
                subject.login(token);
                String url = "/";
                if(null != WebUtils.getSavedRequest(request)){
                    url = WebUtils.getSavedRequest(request).getRequestUrl();
                }
                resultDto.setData(url);
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
    @LogAnnotation("用户注册")
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
        return ResultDtoFactory.error(DtoCodeConsts.USER_EXISTS, DtoCodeConsts.USER_EXISTS_MSG);
    }

    @ApiOperation("获取上传文件模板URL")
    @GetMapping("/importUsersTemplate")
    public ResultDto importUsersTemplate(){
        ResultDto resultDto = ResultDtoFactory.success();
        String url = PropertiesUtil.getBusinessConfig("importUsersTemplate.url");
        resultDto.setData(url);
        return resultDto;
    }

    @ApiOperation("上传excel导入用户")
    @PostMapping("/importUsers")
    public ResultDto importUsers(@RequestParam MultipartFile file){
        ResultDto resultDto = ResultDtoFactory.success();
        //文件校验
        try {
            if (null == file || file.isEmpty() || !file.getContentType().contains("excel")) {
                throw new CustomException(DtoCodeConsts.EXCEL_NO, DtoCodeConsts.EXCEL_NO_MSG);
            }
        } catch (Exception e) {
            logger.error(e.toString());
            e.printStackTrace();
            throw new CustomException(DtoCodeConsts.VIEW_ERROR, DtoCodeConsts.VIEW_ERROR_MSG);
        }
        List<List> excels = null;
        try {
            excels = ExcelUtil.TransExcelToList(file.getInputStream());
        }catch (Exception e){
            logger.info("转换失败");
        }
        if(ListUtils.isNotEmptyList(excels)) {
            List<GrantUser> usersFromList = null;
            usersFromList = userService.getUsersFromList(excels);
            // TODO 插库 是否插库前展示确认一下 逐个校验的话不用批处理插入 速度慢 huser已经做成逐个校验
            Long time = System.currentTimeMillis();
            if(ListUtils.isNotEmptyList(usersFromList)) {
                userService.insertBatch(usersFromList);
            }
            resultDto.setMsg("插库时间："+ (System.currentTimeMillis()-time));
            return resultDto;
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
                throw new CustomException(DtoCodeConsts.USER_EXISTS, DtoCodeConsts.USER_EXISTS_MSG);
            }
        }
        return ResultDtoFactory.error();
    }

    @ApiOperation("删除用户接口")
    @RequiresPermissions("用户管理")
    @PostMapping("/removeUser")
    @LogAnnotation("删除用户")
    public ResultDto removeUser(@RequestParam Long[] userIdList){
        ResultDto resultDto = ResultDtoFactory.success();
        //检查角色权限
        Subject subject = SecurityUtils.getSubject();
        try{
            subject.checkRole("admin");
        }catch (UnauthorizedException e){
            throw new CustomException(DtoCodeConsts.NO_PERMISSION, DtoCodeConsts.NO_PERMISSION_MSG);
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

    @ApiOperation("用户对象编辑接口")
    @RequiresPermissions("用户管理")
    @PostMapping("/editUser")
    @LogAnnotation("编辑用户")
    public ResultDto saveUser(@RequestBody GrantUser grantUser){
        ResultDto resultDto = ResultDtoFactory.success();
        if(StringUtils.isNotEmpty(grantUser.getUsername())) {
            grantUser.setPassword(MD5Utils.transMD5Code(grantUser.getPassword()));
            userService.update(grantUser);
            return resultDto;
        }
        return ResultDtoFactory.error();
    }

    @ApiOperation("查询用户接口（条件可选）")
    @RequiresPermissions({"用户查询"})
    @PostMapping("/queryUser")
    public ResultDto queryUser(@RequestBody(required = false) SysPageBo sysPageBo){
        ResultDto resultDto = ResultDtoFactory.success();
        int pageNum = 1;
        int pageSize = 8;
        if(null != sysPageBo && sysPageBo.checkSelf()){
            pageNum = sysPageBo.getPageNum();
            pageSize = sysPageBo.getPageSize();
        }else{
            sysPageBo = new SysPageBo();
        }
        PageHelper.startPage(pageNum, pageSize);
        GrantUser grantUser = sysPageBo.getGrantUser();
        String roleName = sysPageBo.getGrantRole() != null ? sysPageBo.getGrantRole().getRoleName() : "";
        String permissionName = sysPageBo.getGrantPermission() != null ? sysPageBo.getGrantPermission().getPermissionName() : "";
        List<GrantUser> grantUsers = userService.listUsers(grantUser, roleName, permissionName);
        if(null != grantUsers){
            PageInfo page = new PageInfo(grantUsers);
            resultDto.setData(page);
            if(grantUsers.size() == 0){
                resultDto.setMsg("无数据");
                return resultDto;
            }
            resultDto.setMsg("查询成功！"+grantUsers.size());
            return resultDto;
        }
        return ResultDtoFactory.error();
    }

    @ApiOperation("获取当前登陆用户信息")
    @RequiresAuthentication
    @PostMapping("/currUser")
    public ResultDto currUser(){
        ResultDto resultDto = ResultDtoFactory.success();
        Subject subject = SecurityUtils.getSubject();
        String currUsername = (String) subject.getPrincipal();
        GrantUser user = userService.listUsersByUserName(currUsername).get(0);
        resultDto.setData(user);
        return resultDto;
    }

    @ApiOperation("登出接口")
    @GetMapping("/logout")
    @LogAnnotation("用户登出")
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
    @RequiresPermissions("用户管理")
    @PostMapping("/addRole")
    @LogAnnotation("新增角色")
    public ResultDto addRole(@RequestBody GrantRole grantRole, @RequestParam(required = false) List<Long> userIdList){
        ResultDto resultDto = ResultDtoFactory.success();
        if(StringUtils.isNotEmpty(grantRole.getRoleName()) && StringUtils.isNotEmpty(grantRole.getRoleRemark())){
            if(!roleService.findRoleByName(grantRole.getRoleName())){
                roleService.saveRole(grantRole, userIdList);
                return resultDto;
            }else{
                throw new CustomException(DtoCodeConsts.ROLE_EXISTS, DtoCodeConsts.ROLE_EXISTS_MSG);
            }
        }
        return ResultDtoFactory.error();
    }

    @ApiOperation("查询角色列表（条件可选）")
    @RequiresPermissions({"角色查询"})
    @PostMapping("/queryRole")
    public ResultDto queryRole(@RequestBody(required = false) SysPageBo sysPageBo){
        ResultDto resultDto = ResultDtoFactory.success();
        int pageNum = 1;
        int pageSize = 8;
        if(null != sysPageBo && sysPageBo.checkSelf()){
            pageNum = sysPageBo.getPageNum();
            pageSize = sysPageBo.getPageSize();
        }else{
            sysPageBo = new SysPageBo();
        }
        PageHelper.startPage(pageNum, pageSize);
        GrantRole grantRole = sysPageBo.getGrantRole();
        List<GrantRole> grantRoles = null;
        if(null != sysPageBo.getGrantUser()){
            Long userId;
            if(null != (userId = sysPageBo.getGrantUser().getUserId())) {
                grantRoles = roleService.listRolesByUserId(userId);
            }
        }else {
            grantRoles = roleService.listRoles(grantRole);
        }
        if(null != grantRoles){
            PageInfo page = new PageInfo(grantRoles);
            resultDto.setData(page);
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
    @RequiresPermissions("用户管理")
    @PostMapping("/removeRole")
    @LogAnnotation("删除角色")
    public ResultDto deleteRole(@RequestParam Short[] roleIdList){
        ResultDto resultDto = ResultDtoFactory.success();
        if(null != roleIdList){
            int deleteNum = 0;
            for(Short s : roleIdList) {
                //校验该角色是否对应有用户绑定
                if(ListUtils.isNotEmptyList(userService.listUserByRoleId(s))){
                    throw new CustomException(DtoCodeConsts.CASCADE_DATA, DtoCodeConsts.CASCADE_DATA_MSG);
                }else{
                    deleteNum += roleService.delete(s);
                }
            }
            resultDto.setData(deleteNum);
            return resultDto;
        }
        return ResultDtoFactory.error();
    }

    @ApiOperation("编辑角色接口")
    @RequiresPermissions("用户管理")
    @PostMapping("/editRole")
    @LogAnnotation("编辑角色")
    public ResultDto editRole(@RequestBody GrantRole grantRole){
        ResultDto resultDto = ResultDtoFactory.success();
        if(StringUtils.isNotEmpty(grantRole.getRoleName())){
            roleService.update(grantRole);
            return resultDto;
        }
        return ResultDtoFactory.error();
    }

    @Autowired
    @Qualifier("permissionServiceImpl")
    private PermissionService permissionService;

    @ApiOperation("新增权限接口（可选带角色）")
    @RequiresPermissions("用户管理")
    @PostMapping("/addPermission")
    @LogAnnotation("新增权限")
    public ResultDto addPermission(@RequestBody GrantPermission grantPermission, @RequestParam(required = false) List<Short> userRoleList){
        ResultDto resultDto = ResultDtoFactory.success();
        //检查角色权限
        Subject subject = SecurityUtils.getSubject();
        try{
            subject.checkRole("admin");
        }catch (UnauthorizedException e){
            throw new CustomException(DtoCodeConsts.NO_PERMISSION, DtoCodeConsts.NO_PERMISSION_MSG);
        }
        if(StringUtils.isNotEmpty(grantPermission.getPermissionName())){
            if(!permissionService.findPermissionByName(grantPermission.getPermissionName())){
                permissionService.savePermission(grantPermission, userRoleList);
                return resultDto;
            }else{
                throw new CustomException(DtoCodeConsts.ROLE_EXISTS, DtoCodeConsts.ROLE_EXISTS_MSG);
            }
        }
        return ResultDtoFactory.error();
    }

    @ApiOperation("查询权限列表（条件可选）")
    @RequiresPermissions("权限查询")
    @PostMapping("/queryPermission")
    public ResultDto queryPermission(@RequestBody(required = false) SysPageBo sysPageBo){
        ResultDto resultDto = ResultDtoFactory.success();
        int pageNum = 1;
        int pageSize = 8;
        if(null != sysPageBo && sysPageBo.checkSelf()){
            pageNum = sysPageBo.getPageNum();
            pageSize = sysPageBo.getPageSize();
        }else{
            sysPageBo = new SysPageBo();
        }
        PageHelper.startPage(pageNum, pageSize);
        GrantPermission grantPermission = sysPageBo.getGrantPermission();
        List<GrantPermission> grantPermissions = null;
        if(null != sysPageBo.getGrantRole()){
            Short roleId;
            if(null != (roleId = sysPageBo.getGrantRole().getRoleId())) {
                grantPermissions = permissionService.listPermissionsByRoleId(roleId);
            }
        }else {
            grantPermissions = permissionService.listPermissions(grantPermission);
        }
        if(null != grantPermissions){
            PageInfo page = new PageInfo(grantPermissions);
            resultDto.setData(page);
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
    @RequiresPermissions("用户管理")
    @PostMapping("/removePermission")
    @LogAnnotation("删除权限")
    public ResultDto deletePermission(@RequestParam Long[] permissionIdList){
        ResultDto resultDto = ResultDtoFactory.success();
        if(null != permissionIdList){
            int deleteNum = 0;
            for(Long l : permissionIdList) {
                //校验该角色是否对应有用户绑定
                if(ListUtils.isNotEmptyList(roleService.listRolesByPermissionId(l))){
                    throw new CustomException(DtoCodeConsts.CASCADE_DATA, DtoCodeConsts.CASCADE_DATA_MSG);
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
    @RequiresPermissions("授权")
    @PostMapping("bindUserRoles")
    @LogAnnotation("授权U2R")
    public ResultDto bindUserRoles(@RequestParam Long userId, @RequestBody List<Short> roleIdList){
        ResultDto resultDto = ResultDtoFactory.success();
        if(null != userId && ListUtils.isNotEmptyList(roleIdList)){
            userService.bindUserRoles(userId, roleIdList);
            return resultDto;
        }
        return ResultDtoFactory.error();
    }

    @ApiOperation("授权接口 给单个角色多个用户")
    @RequiresPermissions("授权")
    @PostMapping("bindRoleUsers")
    @LogAnnotation("授权R2U")
    public ResultDto bindRoleUsers(@RequestParam Short roleId, @RequestBody List<Long> userIdList){
        ResultDto resultDto = ResultDtoFactory.success();
        if(null != roleId && ListUtils.isNotEmptyList(userIdList)){
            roleService.bindRoleUsers(roleId, userIdList);
            return resultDto;
        }
        return ResultDtoFactory.error();
    }

    @ApiOperation("授权接口 给单个角色多个权限")
    @RequiresPermissions("授权")
    @PostMapping("bindRolePermissions")
    @LogAnnotation("授权R2P")
    public ResultDto bindRolePermissions(@RequestParam Short roleId, @RequestBody List<Long> permissionIdList){
        ResultDto resultDto = ResultDtoFactory.success();
        if(null != roleId && ListUtils.isNotEmptyList(permissionIdList)) {
            roleService.bindRolePermissions(roleId, permissionIdList);
            return resultDto;
        }
        return ResultDtoFactory.error();
    }

    @ApiOperation("授权接口 给单个权限多个用户")
    @RequiresPermissions("授权")
    @PostMapping("bindPermissionRoles")
    @LogAnnotation("授权P2U")
    public ResultDto bindPermissionRoles(@RequestParam Long permissionId, @RequestBody List<Short> roleIdList){
        ResultDto resultDto = ResultDtoFactory.success();
        if(null != permissionId && ListUtils.isNotEmptyList(roleIdList)) {
            permissionService.bindPermissionRoles(permissionId, roleIdList);
            return resultDto;
        }
        return ResultDtoFactory.error();
    }

}
