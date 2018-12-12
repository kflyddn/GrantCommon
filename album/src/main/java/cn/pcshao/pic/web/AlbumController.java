package cn.pcshao.pic.web;

import cn.pcshao.grant.common.base.BaseController;
import cn.pcshao.grant.common.bo.AlbumSource;
import cn.pcshao.grant.common.consts.DtoCodeConsts;
import cn.pcshao.grant.common.dto.ResultDto;
import cn.pcshao.grant.common.entity.AlbumPicPersonal;
import cn.pcshao.grant.common.entity.AlbumPicPublic;
import cn.pcshao.grant.common.util.ListUtils;
import cn.pcshao.grant.common.util.ResultDtoFactory;
import cn.pcshao.grant.common.util.StringUtils;
import cn.pcshao.pic.ao.ResultFtp;
import cn.pcshao.pic.bo.AlbumPageBo;
import cn.pcshao.pic.service.AlbumSourceService;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * 相册rest控制器
 * @author pcshao.cn
 * @date 2018-12-4
 */
@RestController
@RequestMapping("/album")
public class AlbumController extends BaseController {

    @Autowired
    @Qualifier("picService")
    private AlbumSourceService picService;

    @ApiOperation("测试第一个接口")
    @PostMapping("/show")
    public ResultDto test(){
        ResultDto resultDto = ResultDtoFactory.success();
        if(1 == 1){
            resultDto.setData("相册测试接口");
            return resultDto;
        }
        return ResultDtoFactory.error();
    }

    @ApiOperation("公共相册接口")
    @PostMapping("/square")
    public ResultDto showPublic(@RequestBody(required = false) AlbumPageBo albumPageBo){
        ResultDto resultDto = ResultDtoFactory.success();
        int pageNum = 1;
        int pageSize = 8;
        if(null != albumPageBo && albumPageBo.checkSelf()){
            pageNum = albumPageBo.getPageNum();
            pageSize = albumPageBo.getPageSize();
        }else{
            albumPageBo = new AlbumPageBo();
        }
        //组合查询参数
        AlbumPicPublic condition = new AlbumPicPublic();
        condition.setUserName(albumPageBo.getUsername());
        condition.setUserNickname(albumPageBo.getUserNickname());
        condition.setName(albumPageBo.getName());
        condition.setDescrib(albumPageBo.getDescribe());
        condition.setDisplay(true);
        PageHelper.startPage(pageNum, pageSize);
        List<AlbumPicPublic> picPublic = picService.getPicPublic(condition);
        if(null != picPublic){
            if(0 == picPublic.size()){
                resultDto.setMsg("无相册记录！");
            }
            resultDto.setData(picPublic);
            return resultDto;
        }
        return ResultDtoFactory.error();
    }

    @ApiOperation("我的相册")
    @PostMapping("/my")
    public ResultDto my(@RequestBody(required = false) AlbumPageBo albumPageBo){
        ResultDto resultDto = ResultDtoFactory.success();
        //获取当前登录用户
        Subject subject = SecurityUtils.getSubject();
        String username = (String) subject.getPrincipal();
        int pageNum = 1;
        int pageSize = 8;
        if(null != albumPageBo && albumPageBo.checkSelf()){
            pageNum = albumPageBo.getPageNum();
            pageSize = albumPageBo.getPageSize();
        }
        PageHelper.startPage(pageNum, pageSize);
        List<AlbumPicPersonal> picPersonal = picService.getPicPersonal(username);
        if(null != picPersonal){
            if(0 == picPersonal.size()){
                resultDto.setMsg("无个人相册记录！");
            }
            resultDto.setData(picPersonal);
            return resultDto;
        }
        return ResultDtoFactory.error();
    }

    @ApiOperation("新增资源接口")
    @PostMapping("/add")
    public ResultDto add(HttpServletRequest request, @RequestParam MultipartFile file){
        ResultDto resultDto = ResultDtoFactory.success();
        //文件校验
        try {
            if (null == file || file.isEmpty()) {
                return ResultDtoFactory.error(DtoCodeConsts.ALBUM_PIC_EMPTY, DtoCodeConsts.ALBUM_PIC_EMPTY_MSG);
            }
            if (!file.getContentType().contains("image")) {
                return ResultDtoFactory.error(DtoCodeConsts.ALBUM_PIC_NO, DtoCodeConsts.ALBUM_PIC_NO_MSG);
            }
        } catch (Exception e) {
            logger.error(e.toString());
            e.printStackTrace();
            return ResultDtoFactory.error();
        }
        //上传文件，根据request中是否私有来确定物理上传位置
        ResultFtp resultFtp = null;
        String isPrivate = "";
        try {
            if(null != request.getParameter("isPrivate")) {
                //目录private物理隔离
                isPrivate = "/auth";
            }
            resultFtp = picService.upLoadFile(file, isPrivate);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //上传成功后的插库等操作
        if(null != resultFtp && resultFtp.isFlag()){
            AlbumSource pic = new AlbumSource();
            initPicParam(request, pic);
            pic.setPathLocal(resultFtp.getLocalPath());
            pic.setPathFtp(resultFtp.getFtpPath());
            pic.setCreatetime(new Date());
            pic.setFilesize(resultFtp.getFilesize());
            if(StringUtils.isNotEmpty(isPrivate)){
                pic.setPrivate(true);
                picService.getPersonalPicMapper().insertSelective(pic);
            }else{
                pic.setDisplay(true);
                picService.getPublicPicMapper().insertSelective(pic);
            }
            resultDto.setData("成功上传！");
            return resultDto;
        }
        return ResultDtoFactory.error(DtoCodeConsts.FTP_FAILUER, DtoCodeConsts.FTP_FAILUER_MSG);
    }

    /**
     * Request --> AlbumSource
     * @param request
     * @param albumSource
     */
    private void initPicParam(HttpServletRequest request, AlbumSource albumSource) {
        MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
        String type1,type2,type3,name,describ;
        String userId;
        String userName,userNickname;
        if(StringUtils.isNotEmpty(type1 = multiRequest.getParameter("type1"))){
            albumSource.setType1(type1);
        }
        if(StringUtils.isNotEmpty(type2 = multiRequest.getParameter("type2"))){
            albumSource.setType2(type2);
        }
        if(StringUtils.isNotEmpty(type3 = multiRequest.getParameter("type3"))){
            albumSource.setType3(type3);
        }
        if(StringUtils.isNotEmpty(name = multiRequest.getParameter("name"))){
            albumSource.setName(name);
        }
        if(StringUtils.isNotEmpty(describ = multiRequest.getParameter("describ"))){
            albumSource.setDescrib(describ);
        }
        if(StringUtils.isNotEmpty(userId = multiRequest.getParameter("userId"))){
            albumSource.setUserId(Long.parseLong(userId));
        }
        Subject subject = SecurityUtils.getSubject();
        String currUsername = subject.getPrincipals().toString();
            albumSource.setUserName(currUsername);
        if(StringUtils.isNotEmpty(userNickname = multiRequest.getParameter("userNickname"))){
            albumSource.setUserNickname(userNickname);
        }

    }

    @ApiOperation("删除资源接口")
    @PostMapping("/delete")
    public ResultDto delete(@RequestParam Long[] idList, @RequestParam String sourceType){
        ResultDto resultDto = ResultDtoFactory.success();
        //检查角色权限
        Subject subject = SecurityUtils.getSubject();
        try{
            subject.checkRole("admin");
        }catch (UnauthorizedException e){
            return ResultDtoFactory.error(DtoCodeConsts.NO_PERMISSION, DtoCodeConsts.NO_PERMISSION_MSG);
        }
        if(null != idList && StringUtils.isNotEmpty(sourceType)){
            int deleteNum = 0;
            if(sourceType.equals("picPublic")){
                for(Long id : idList) {
                    AlbumPicPublic condition = new AlbumPicPublic();
                    condition.setId(id);
                    condition.setName("TO_DELETE");
                    deleteNum += picService.getPublicPicMapper().updateByPrimaryKeySelective(condition);
                }
            }else if(sourceType.equals("picPersonal")){
                for(Long id : idList) {
                    AlbumPicPublic condition = new AlbumPicPublic();
                    condition.setId(id);
                    condition.setName("TO_DELETE");
                    deleteNum += picService.getPersonalPicMapper().updateByPrimaryKeySelective(condition);
                }
            }
            resultDto.setData(deleteNum);
            return resultDto;
        }
        return ResultDtoFactory.error();
    }

    @ApiOperation("更新资源接口")
    @PostMapping("/update")
    public ResultDto update(){
        ResultDto resultDto = ResultDtoFactory.success();

        return ResultDtoFactory.error();
    }

}
