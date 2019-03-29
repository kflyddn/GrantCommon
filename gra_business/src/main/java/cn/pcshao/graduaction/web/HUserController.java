package cn.pcshao.graduaction.web;

import cn.pcshao.graduaction.service.GrantTempService;
import cn.pcshao.graduaction.service.HUserService;
import cn.pcshao.graduaction.service.UserService;
import cn.pcshao.graduaction.util.HadoopUtil;
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
import cn.pcshao.grant.websocket.util.WebSocketUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
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
            hUsersFromList = hUserService.getUsersFromList(excels);
            //TODO 校验重复性能和边插边校验差不多 还是要从用户源头解决
            //checkIDNumber(hUsersFromList);
            Long time = System.currentTimeMillis();
            if(ListUtils.isNotEmptyList(hUsersFromList)) {
                int fromNums = hUsersFromList.size();
                //更新状态表 TODO 换成websocket推送上传进度
                GrantTemp gTemp = new GrantTemp();
                gTemp.setOperId(DtoCodeConsts.GRANT_TEMP_OPER_ID);
                gTemp.setOperName(DtoCodeConsts.GRANT_TEMP_OPER_ID_MSG);
                gTemp.setCreateTime(new Date());
                gTemp.setC1(fromNums+"-当前上传总数");
                gTemp.setC2("0");
                gTemp.setId(gTempService.selectByName(gTemp.getOperName()).get(0).getId());
                if(null == gTempService.selectByName(gTemp.getOperName()))
                    gTempService.insert(gTemp);
                gTempService.update(gTemp);
                int stNum = 1;
                int perNum = fromNums/200;
                if(perNum<10)
                    perNum=10;
                //逐条插库
                for(GrantHuser huser : hUsersFromList){
                    if(stNum%perNum==0) {
                        gTemp.setC2((double)stNum/fromNums*100+"");
                        gTempService.update(gTemp);
                        //WebSocket广播进度
                        WebSocketUtil.broadCast(WebSocketUtil.getProcessSessionMap(), (double)stNum/fromNums*100+"");
                    }
                    //检查user表中是否已有此用户名-对应huser表中身份证号
                    GrantUser user = new GrantUser();
                    user.setNickname(huser.getName());
                    user.setUsername(huser.getIdCard());
                    user.setTel(huser.getTelephone());
                    user.setSex(huser.getSex());
                    user.setEmail(huser.getEmail());
                    user.setIsUse(false);
                    List<Short> roleList = new ArrayList<>();
                    roleList.add((short)3);
                    huser.setUserId(userService.saveUser(user, roleList));
                    hUserService.insert(huser);
                    stNum++;
                }
                //hUserService.insertBatch(hUsersFromList);
                gTemp.setC2("");
                gTempService.update(gTemp);
            }
            resultDto.setMsg("插库时间："+ (System.currentTimeMillis()-time)/1000+ "秒");
            return resultDto;
        }
        return ResultDtoFactory.error();
    }

    private void checkIDNumber(List<GrantHuser> hUsersFromList) {
        for (GrantHuser huser : hUsersFromList) {
            List<GrantUser> users = userService.listUsersByUserName(huser.getIdCard());
            if(ListUtils.isNotEmptyList(users)){
                throw new CustomException(DtoCodeConsts.USER_EXISTS, DtoCodeConsts.USER_EXISTS_MSG
                        + "-"+ users.get(0).getUsername()+ "-"+ users.get(0).getNickname());
            }
        }
    }

    @ApiOperation("获取Mysql同步Hdfs实时状态")
    @GetMapping("/hdfsNow")
    public ResultDto hdfsNow(){
        ResultDto resultDto = ResultDtoFactory.success();
        Float process = 100f;
        try{
            process = hUserService.getSynchronizedProcess();
        }catch (Exception e){

        }
        resultDto.setData(process);
        return resultDto;
    }

    @ApiOperation("获取上传数据到mysql实时状态")
    @GetMapping("/mysqlNow")
    public ResultDto mysqlNow(){
        ResultDto resultDto = ResultDtoFactory.success();
        Float process = -1f;
        try {
            process = Float.parseFloat(gTempService.selectByName(DtoCodeConsts.GRANT_TEMP_OPER_ID_MSG).get(0).getC2().split("-")[0]);
        }catch (Exception e){
        }
        resultDto.setData(process);
        return resultDto;
    }

    @ApiOperation("重置数据库与DFS，清除记录")
    @GetMapping("/resetDBandHDFS")
    public ResultDto resetDBandHDFS(){
        ResultDto resultDto = ResultDtoFactory.success();
        Long minUserId = 58l; //用户表中从哪个开始删
        hUserService.resetDB();
        userService.resetDB(minUserId);
        GrantTemp model = new GrantTemp();
        model.setId(DtoCodeConsts.GRANT_TEMP_OPER_ID);
        model.setC2("-1");
        gTempService.update(model);
        try{
            String hadoopURI = PropertiesUtil.getBusinessConfig("task.Mysql2Hdfs.hadoopURI");
            String hdfsLocatePath = PropertiesUtil.getBusinessConfig("task.Mysql2Hdfs.hdfsLocate");
            HadoopUtil.clearHdfs(hadoopURI, hdfsLocatePath);
        }catch (Exception e){
            e.printStackTrace();
            resultDto = ResultDtoFactory.error();
        }
        return resultDto;
    }

}
