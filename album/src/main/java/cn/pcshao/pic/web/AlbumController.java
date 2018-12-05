package cn.pcshao.pic.web;

import cn.pcshao.grant.common.base.BaseController;
import cn.pcshao.grant.common.bo.AlbumSource;
import cn.pcshao.grant.common.consts.DtoCodeConsts;
import cn.pcshao.grant.common.dto.ResultDto;
import cn.pcshao.grant.common.entity.AlbumPicPersonal;
import cn.pcshao.grant.common.entity.AlbumPicPublic;
import cn.pcshao.grant.common.util.ResultDtoFactory;
import cn.pcshao.grant.common.util.StringUtils;
import cn.pcshao.pic.ao.ResultFtp;
import cn.pcshao.pic.service.AlbumSourceService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;

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
    @GetMapping("/square")
    public ResultDto showPublic(){
        ResultDto resultDto = ResultDtoFactory.success();

        return ResultDtoFactory.error();
    }

    @ApiOperation("我的相册")
    @PostMapping("/my")
    public ResultDto my(){
        ResultDto resultDto = ResultDtoFactory.success();

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
            //TODO 拆分HttpServletRequest 提取其他参数插库
            if(StringUtils.isNotEmpty(isPrivate)){
                AlbumPicPersonal picPersonal = new AlbumPicPersonal();
                initPicParam(request, picPersonal);
                picPersonal.setIsPrivate(true);
                picPersonal.setPathFtp(resultFtp.getFtpPath());
                picPersonal.setCreatetime(new Date());
                picPersonal.setFilesize(resultFtp.getFilesize());
                picService.getPersonalPicMapper().insertSelective(picPersonal);
            }else{
                AlbumPicPublic picPublic = new AlbumPicPublic();
                initPicParam(request, picPublic);
                picPublic.setDisplay(true);
                picPublic.setPathFtp(resultFtp.getFtpPath());
                picPublic.setCreatetime(new Date());
                picPublic.setFilesize(resultFtp.getFilesize());
                picService.getPublicPicMapper().insertSelective(picPublic);
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
        if(StringUtils.isNotEmpty(userName = multiRequest.getParameter("userName"))){
            albumSource.setUserName(userName);
        }
        if(StringUtils.isNotEmpty(userNickname = multiRequest.getParameter("userNickname"))){
            albumSource.setUserNickname(userNickname);
        }
        //TODO 当前登录用户信息存库
        albumSource.setUserId(null);
        albumSource.setUserName(null);
        albumSource.setUserNickname(null);
    }

    @ApiOperation("删除资源接口")
    @PostMapping("/delete")
    public ResultDto delete(){
        ResultDto resultDto = ResultDtoFactory.success();

        return ResultDtoFactory.error();
    }

    @ApiOperation("更新资源接口")
    @PostMapping("/update")
    public ResultDto update(){
        ResultDto resultDto = ResultDtoFactory.success();

        return ResultDtoFactory.error();
    }

}
