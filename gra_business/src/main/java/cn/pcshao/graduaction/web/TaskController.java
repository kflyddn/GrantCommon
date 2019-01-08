package cn.pcshao.graduaction.web;

import cn.pcshao.graduaction.service.TaskService;
import cn.pcshao.grant.common.base.BaseController;
import cn.pcshao.grant.common.bo.TaskBo;
import cn.pcshao.grant.common.consts.DtoCodeConsts;
import cn.pcshao.grant.common.dto.ResultDto;
import cn.pcshao.grant.common.entity.GrantTask;
import cn.pcshao.grant.common.exception.CustomException;
import cn.pcshao.grant.common.util.ResultDtoFactory;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

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
    public ResultDto addTask(@RequestBody GrantTask task){
        ResultDto resultDto = ResultDtoFactory.success();
        //TODO 任务的具体体现
        int insert;
        try{
            task.setSerialNumber((int) System.currentTimeMillis());
            task.setState((byte) 0);
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
    public ResultDto removeTask(@RequestBody List<Integer> taskIdList){
        ResultDto resultDto = ResultDtoFactory.success();
        //TODO 任务的具体体现
        for(Integer id : taskIdList) {
            taskService.delete(id);
        }
        return resultDto;
    }

    @ApiOperation("启动任务接口")
    @PostMapping("/startTask")
    public ResultDto startTask(@RequestBody List<Integer> taskIdList){
        ResultDto resultDto = ResultDtoFactory.success();
        //TODO 任务的具体实现
        taskService.startTask(taskIdList);
        return resultDto;
    }

    @ApiOperation("重启任务接口")
    @PostMapping("/restartTask")
    public ResultDto restartTask(@RequestBody List<Integer> taskIdList){
        ResultDto resultDto = ResultDtoFactory.success();
        //TODO 任务的具体实现
        taskService.startTask(taskIdList);
        return resultDto;
    }

    @ApiOperation("停止任务接口")
    @PostMapping("/stopTask")
    public ResultDto stopTask(@RequestBody List<Integer> taskIdList){
        ResultDto resultDto = ResultDtoFactory.success();
        //TODO 任务的具体实现
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
        }else{
            taskBo = new TaskBo();
        }
        PageHelper.startPage(pageNum, pageSize);
        List<GrantTask> grantTasks = taskService.listTask(taskBo);
        PageInfo page = new PageInfo(grantTasks);
        resultDto.setData(page);
        return resultDto;
    }

}
