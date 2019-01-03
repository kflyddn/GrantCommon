package cn.pcshao.graduaction.web;

import cn.pcshao.graduaction.service.HUserService;
import cn.pcshao.grant.common.dto.ResultDto;
import cn.pcshao.grant.common.entity.GrantHuser;
import cn.pcshao.grant.common.util.ResultDtoFactory;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 毕业设计(HUser based on Hadoop)控制器
 * @author pcshao.cn
 * @date 2019/1/3
 */
@RestController
@RequestMapping("/huser")
public class HUserController {

    @Autowired
    @Qualifier("hUserServiceImpl")
    private HUserService hUserService;

    @ApiOperation("获取HUser档案")
    @PostMapping("/getHUserFile")
    public ResultDto getHUserFile(@RequestParam Long hUserId){
        ResultDto resultDto = ResultDtoFactory.success();
        if(null != hUserId){
            GrantHuser hUser = hUserService.getHUserByUserId(hUserId);
            if(null != hUser){
                resultDto.setData(hUser);
                return resultDto;
            }
        }
        return ResultDtoFactory.error();
    }

}
