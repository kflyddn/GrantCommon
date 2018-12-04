package cn.pcshao.pic.web;

import cn.pcshao.grant.common.base.BaseController;
import cn.pcshao.grant.common.dto.ResultDto;
import cn.pcshao.grant.common.util.ResultDtoFactory;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

/**
 * 相册rest控制器
 * @author pcshao.cn
 * @date 2018-12-4
 */
@RestController
@RequestMapping("/album")
public class PicController extends BaseController {

    @ApiOperation("测试第一个接口")
    @GetMapping("/show")
    public ResultDto test(@RequestParam Integer go){
        ResultDto resultDto = ResultDtoFactory.success();
        if(1 == go){
            resultDto.setData("相册测试接口");
            return resultDto;
        }
        return ResultDtoFactory.error();
    }

}
