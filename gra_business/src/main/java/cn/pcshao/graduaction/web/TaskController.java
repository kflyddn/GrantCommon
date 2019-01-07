package cn.pcshao.graduaction.web;

import cn.pcshao.graduaction.service.TaskService;
import cn.pcshao.grant.common.base.BaseController;
import cn.pcshao.grant.common.bo.TaskBo;
import cn.pcshao.grant.common.dto.ResultDto;
import cn.pcshao.grant.common.entity.GrantTask;
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
        int insert = taskService.insert(task);
        resultDto.setData(insert);
        return resultDto;
    }

    @ApiOperation("移除任务接口")
    @PostMapping("/remove")
    public ResultDto removeTask(Integer[] taskId){
        ResultDto resultDto = ResultDtoFactory.success();
        //TODO 任务的具体体现
        for(Integer id : taskId) {
            taskService.delete(id);
        }
        return resultDto;
    }

    @ApiOperation("启动任务接口")
    @PostMapping("/startTask")
    public ResultDto startTask(List<Integer> taskId){
        ResultDto resultDto = ResultDtoFactory.success();
        //TODO 任务的具体实现
        taskService.startTask(taskId);
        return resultDto;
    }

    @ApiOperation("停止任务接口")
    @PostMapping("/stopTask")
    public ResultDto stopTask(List<Integer> taskId){
        ResultDto resultDto = ResultDtoFactory.success();
        //TODO 任务的具体实现
        taskService.stopTask(taskId);
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
