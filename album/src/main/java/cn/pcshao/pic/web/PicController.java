package cn.pcshao.pic.web;

import cn.pcshao.grant.common.base.BaseController;
import cn.pcshao.grant.common.dto.ResultDto;
import cn.pcshao.grant.common.util.ResultDtoFactory;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 相册rest控制器
 * @author pcshao.cn
 * @date 2018-12-4
 */
@RestController
@RequestMapping("/album")
public class PicController extends BaseController {

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
    public ResultDto add(){
        ResultDto resultDto = ResultDtoFactory.success();

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
