package cn.pcshao.graduaction.web;

import cn.pcshao.graduaction.service.TaskService;
import cn.pcshao.grant.common.aop.LogAnnotation;
import cn.pcshao.grant.common.base.BaseController;
import cn.pcshao.grant.common.bo.TaskBo;
import cn.pcshao.grant.common.consts.DtoCodeConsts;
import cn.pcshao.grant.common.dto.ResultDto;
import cn.pcshao.grant.common.entity.GrantTask;
import cn.pcshao.grant.common.entity.GrantTaskResult;
import cn.pcshao.grant.common.exception.CustomException;
import cn.pcshao.grant.common.util.ResultDtoFactory;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author pcshao.cn
 * @date 2019/1/7
 */
@RestController
@RequestMapping("/task")
public class TaskController extends BaseController {

    @Autowired
    @Qualifier("taskServiceImpl")
    private TaskService taskService;

    @ApiOperation("新增任务接口")
    @PostMapping("/add")
    @LogAnnotation("任务新增")
    public ResultDto addTask(@RequestBody GrantTask task){
        ResultDto resultDto = ResultDtoFactory.success();
        int insert;
        try{
            task.setSerialNumber((int) System.currentTimeMillis());
            task.setState((byte) 3);
            task.setProcess((short) 0);
            task.setCreateTime(new Date());
            insert = taskService.insert(task);
            resultDto.setData(insert);
        }catch (Exception e){
            throw new CustomException(DtoCodeConsts.VIEW_ERROR, DtoCodeConsts.VIEW_ERROR_MSG);
        }
        return resultDto;
    }

    @ApiOperation("移除任务接口")
    @PostMapping("/remove")
    @LogAnnotation("任务移除")
    public ResultDto removeTask(@RequestBody List<Integer> taskIdList){
        ResultDto resultDto = ResultDtoFactory.success();
        for(Integer id : taskIdList) {
            taskService.delete(id);
        }
        return resultDto;
    }

    @ApiOperation("启动任务接口")
    @PostMapping("/startTask")
    @LogAnnotation("任务启动")
    public ResultDto startTask(@RequestBody List<Integer> taskIdList){
        ResultDto resultDto = ResultDtoFactory.success();
        taskService.startTask(taskIdList);
        return resultDto;
    }

    @ApiOperation("重启任务接口")
    @PostMapping("/restartTask")
    @LogAnnotation("任务重启")
    public ResultDto restartTask(@RequestBody List<Integer> taskIdList){
        ResultDto resultDto = ResultDtoFactory.success();
        taskService.startTask(taskIdList);
        return resultDto;
    }

    @ApiOperation("停止任务接口")
    @PostMapping("/stopTask")
    @LogAnnotation("任务停止")
    public ResultDto stopTask(@RequestBody List<Integer> taskIdList){
        ResultDto resultDto = ResultDtoFactory.success();
        taskService.stopTask(taskIdList);
        return resultDto;
    }

    @ApiOperation("条件获取任务列表")
    @PostMapping("/getTaskList")
    public ResultDto getTaskList(@RequestBody(required = false) TaskBo taskBo){
        ResultDto resultDto = ResultDtoFactory.success();
        int pageNum = 1;
        int pageSize = 8;
        if(null != taskBo && taskBo.checkSelf()){
            pageNum = taskBo.getPageNum();
            pageSize = taskBo.getPageSize();
        }
        PageHelper.startPage(pageNum, pageSize);
        List<GrantTask> grantTasks = taskService.listTask(taskBo);
        PageInfo page = new PageInfo(grantTasks);
        resultDto.setData(page);
        return resultDto;
    }

    @ApiOperation("获取任务结果接口")
    @PostMapping("/getTaskResult")
    public ResultDto getTaskResult(@RequestParam Integer taskId){
        ResultDto resultDto = ResultDtoFactory.success();
        if(null != taskId) {
            List taskResultList = taskService.listTaskResult(taskId);
            GrantTask task = taskService.selectById(taskId);
            if(null != taskResultList){
                Map map = new HashMap<String, Object>();
                map.put("taskInfo", task);
                map.put("taskResultList", taskResultList);
                resultDto.setData(map);
            }
        }
        return resultDto;
    }

}
