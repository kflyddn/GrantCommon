package cn.pcshao.graduaction.web;

import cn.pcshao.graduaction.service.GrantTempService;
import cn.pcshao.graduaction.service.HUserService;
import cn.pcshao.graduaction.service.UserService;
import cn.pcshao.grant.common.base.BaseController;
import cn.pcshao.grant.common.consts.DtoCodeConsts;
import cn.pcshao.grant.common.dto.ResultDto;
import cn.pcshao.grant.common.entity.GrantHuser;
import cn.pcshao.grant.common.entity.GrantTemp;
import cn.pcshao.grant.common.entity.GrantUser;
import cn.pcshao.grant.common.exception.CustomException;
import cn.pcshao.grant.common.util.ExcelUtil;
import cn.pcshao.grant.common.util.ListUtils;
import cn.pcshao.grant.common.util.PropertiesUtil;
import cn.pcshao.grant.common.util.ResultDtoFactory;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * 毕业设计(HUser based on Hadoop)控制器
 * @author pcshao.cn
 * @date 2019/1/3
 */
@RestController
@RequestMapping("/huser")
public class HUserController extends BaseController {

    @Autowired
    @Qualifier("hUserServiceImpl")
    private HUserService hUserService;
    @Autowired
    @Qualifier("userServiceImpl")
    private UserService userService;
    @Autowired
    @Qualifier("gTempService")
    private GrantTempService gTempService;

    @ApiOperation("获取HUser档案")
    @PostMapping("/getHUserFile")
    public ResultDto getHUserFile(@RequestParam Long hUserId){
        ResultDto resultDto = ResultDtoFactory.success();
        if(null != hUserId){
            List<GrantHuser> huserList = hUserService.getHUsersByUserId(hUserId);
            if(ListUtils.isNotEmptyList(huserList)){
                resultDto.setData(huserList.get(0));
                return resultDto;
            }
        }
        return ResultDtoFactory.error();
    }

    @ApiOperation("挂载档案接口")
    @PostMapping("/addFile")
    public ResultDto addFile (@RequestBody GrantHuser huser){
        ResultDto resultDto = ResultDtoFactory.success();
        if(null != huser) {
            if(null != huser.getUserId()) {
                if(ListUtils.isEmptyList(hUserService.getHUsersByUserId(huser.getUserId()))){
                    if(null != userService.selectById(huser.getUserId())) {
                        hUserService.insert(huser);
                        return resultDto;
                    }
                }
            }else {
                GrantUser grantUser = new GrantUser();
                grantUser.setUsername(huser.getIdCard());
                grantUser.setNickname(huser.getName());
                grantUser.setSex(huser.getSex());
                grantUser.setTel(huser.getTelephone());
                grantUser.setEmail(huser.getEmail());
                grantUser.setIsUse(false);
                if(ListUtils.isEmptyList(userService.listUsersByUserName(grantUser.getUsername()))) {
                    List<Short> roleList = new ArrayList<>();
                    roleList.add((short)3);
                    Long id = userService.saveUser(grantUser, roleList);
                    huser.setUserId(id);
                    hUserService.insert(huser);
                    return resultDto;
                }
            }
        }
        return ResultDtoFactory.error();
    }

    @ApiOperation("编辑档案接口，一次修改机会")
    @PostMapping("/editFile")
    public ResultDto editFile (@RequestBody GrantHuser huser){
        ResultDto resultDto = ResultDtoFactory.success();
        hUserService.editHUserFile(huser);
        return ResultDtoFactory.error();
    }

    @ApiOperation("获取上传文件模板URL")
    @GetMapping("/importFilesTemplate")
    public ResultDto importFilesTemplate(){
        ResultDto resultDto = ResultDtoFactory.success();
        String url = PropertiesUtil.getBusinessConfig("importHUsersTemplate.url");
        resultDto.setData(url);
        return resultDto;
    }

    @ApiOperation("批量导入档案接口，校验哪些用户名已经被添加过了")
    @PostMapping("/importFiles")
    public ResultDto importFiles (@RequestParam MultipartFile file){
        ResultDto resultDto = ResultDtoFactory.success();
        //文件校验
        try {
            if (null == file || !file.getContentType().contains("excel") || file.isEmpty()) {
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
            List<GrantHuser> hUsersFromList = null;
            try{
                hUsersFromList = hUserService.getUsersFromList(excels);
            }catch (Exception e){
                throw new CustomException(DtoCodeConsts.EXCEL_FORMAT, DtoCodeConsts.EXCEL_FORMAT_MSG);
            }
            // TODO 插库 是否插库前展示确认一下 逐个校验的话不用批处理插入 速度慢 huser已经做成逐个校验 是否开启事务，excel文件中已经有的档案插到一半回滚
            Long time = System.currentTimeMillis();
            if(ListUtils.isNotEmptyList(hUsersFromList)) {
                int fromNums = hUsersFromList.size();
                //更新状态表
                GrantTemp gTemp = new GrantTemp();
                gTemp.setOperId(DtoCodeConsts.GRANT_TEMP_OPER_ID);
                gTemp.setOperName(DtoCodeConsts.GRANT_TEMP_OPER_ID_MSG);
                gTemp.setC1(fromNums+"-当前上传总数");
                if(0 == gTempService.insert(gTemp))
                    gTempService.update(gTemp);
                int stNum = 1;
                int perNum = fromNums/50;
                for(GrantHuser huser : hUsersFromList){
                    if(stNum%perNum==0) {
                        gTemp.setC2((double)stNum/fromNums*100+"");
                        gTempService.update(gTemp);
                    }
                    GrantUser user = new GrantUser();
                    user.setNickname(huser.getName());
                    user.setUsername(huser.getIdCard());
                    user.setSex(huser.getSex());
                    user.setTel(huser.getTelephone());
                    user.setEmail(huser.getEmail());
                    user.setIsUse(false);
                    //检查user表中是否已有此用户名-对应huser表中身份证号
                    if(ListUtils.isEmptyList(userService.listUsersByUserName(user.getUsername()))) {
                        List<Short> roleList = new ArrayList<>();
                        roleList.add((short)3);
                        Long id = userService.saveUser(user, roleList);
                        huser.setUserId(id);
                        hUserService.insert(huser);
                    }else{
                        throw new CustomException(DtoCodeConsts.USER_EXISTS, user.getUsername()+ "--"+ user.getNickname()+ "--"+ DtoCodeConsts.USER_EXISTS_MSG);
                    }
                    stNum++;
                }
                //hUserService.insertBatch(hUsersFromList);
            }
            resultDto.setMsg("插库时间："+ (System.currentTimeMillis()-time));
            return resultDto;
        }
        return ResultDtoFactory.error();
    }

    @ApiOperation("获取Mysql同步Hdfs实时状态")
    @GetMapping("/hdfsNow")
    public ResultDto hdfsNow(){
        ResultDto resultDto = ResultDtoFactory.success();
        Float process = hUserService.getSynchronizedProcess();
        resultDto.setData(process);
        return resultDto;
    }

    @ApiOperation("获取上传数据到mysql实时状态")
    @GetMapping("/mysqlNow")
    public ResultDto mysqlNow(){
        ResultDto resultDto = ResultDtoFactory.success();
        Float process = hUserService.getUpdateProcess();
        resultDto.setData(process);
        return resultDto;
    }

}
