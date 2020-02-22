package cn.pcshao.graduaction.web;

import cn.pcshao.grant.common.base.BaseController;
import cn.pcshao.grant.common.dto.ResultDto;
import cn.pcshao.grant.common.util.ResultDtoFactory;
import cn.pcshao.hs.ggzy.bo.OperateBo;
import cn.pcshao.hs.ggzy.service.DBService;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;

/**
 * 资源系统
 * @author pcshao.cn
 * @date 2020-02-22
 */
@RestController
@RequestMapping("/ggzy")
public class GgzyController extends BaseController {

    @Autowired
    @Qualifier("ggzyService")
    private DBService dbService;

    @ApiOperation("公共资源系统-申请流水查询接口")
    @RequiresPermissions("公共资源-管理")
    @PostMapping("/queryPubJour")
    public ResultDto queryPubJour(@RequestParam String MOD_NO){
        ResultDto resultDto = ResultDtoFactory.success();
        OperateBo bo = new OperateBo("" +
                "select * from hs_asset.requestpubjour where mod_no = '"+ MOD_NO+ "'");
        bo.setRequestpubjourList(new ArrayList<>());
        dbService.selectById(bo);
        resultDto.setData(bo);
        return resultDto;
    }
}
