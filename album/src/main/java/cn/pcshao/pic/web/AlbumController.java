package cn.pcshao.pic.web;

import cn.pcshao.grant.common.base.BaseController;
import cn.pcshao.grant.common.consts.DtoCodeConsts;
import cn.pcshao.grant.common.dto.ResultDto;
import cn.pcshao.grant.common.util.ResultDtoFactory;
import cn.pcshao.pic.ao.ResultFtp;
import cn.pcshao.pic.service.AlbumSourceService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

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
        ResultFtp resultFtp = picService.upLoadFile(file);
        if(null != resultFtp && resultFtp.isFlag()){
            //TODO 拆分HttpServletRequest 提取其他参数插库
            return resultDto;
        }
        return ResultDtoFactory.error();
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
