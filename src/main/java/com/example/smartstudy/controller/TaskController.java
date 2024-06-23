package com.example.smartstudy.controller;

import com.example.smartstudy.common.Result;
import com.example.smartstudy.exception.BaseException;
import com.example.smartstudy.model.dto.TaskDto;
import com.example.smartstudy.model.vo.TaskVo;
import com.example.smartstudy.service.TaskService;
import com.example.smartstudy.utils.ThrowUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
@Slf4j
/**
 * 任务清单
 */
public class TaskController {

    @Autowired
    private TaskService taskService;

    /**
     * 查询所有任务
     * @param name
     * @return
     */
    @GetMapping("/")
    public Result allTask(@RequestParam String name) {
        if (name == null) {
            throw new BaseException("请求参数错误");
        }
        List<TaskVo> vos = taskService.allTask(name);
        return Result.success(vos);
    }

    /**
     * 查询某个任务内容
     * @param content
     * @param name
     * @return
     */
    @GetMapping("/one")
    public Result queryOneTask(@RequestParam String content,String name){
        if (content==null&&name==null){
            throw new BaseException("请求参数错误");
        }
        TaskVo vo=taskService.queryOneTask(content,name);
        return Result.success(vo);

    }

    /**
     * 添加任务
     * @param taskDto
     * @return
     */
    @PostMapping("/")
    public Result addTask(@RequestBody TaskDto taskDto) {
        if (taskDto == null) {
            throw new BaseException("请求参数错误");
        }
        System.out.println(taskDto.toString());
        boolean result = taskService.addTask(taskDto);
        ThrowUtils.throwIf(!result, "添加失败");
        return Result.success("添加成功");
    }

    /**
     * 修改任务
     * @param taskDto
     * @return
     */
    @PutMapping("/")
    public Result editTask(@RequestBody TaskDto taskDto) {
        if (taskDto == null) {
            throw new BaseException("请求参数错误");
        }
        boolean result=taskService.editTask(taskDto);
        ThrowUtils.throwIf(!result, "修改失败");
        return Result.success("修改成功");
    }

    /**
     * 删除任务
     * @param taskId
     * @return
     */
    @DeleteMapping("/")
    public Result removeTask(@RequestParam String taskId){
        if (taskId==null){
            throw new BaseException("请求参数错误");
        }
        boolean result= taskService.removeTask(taskId);
        ThrowUtils.throwIf(!result, "删除失败");
        return Result.success("删除成功");
    }

    /**
     * 修改任务状态
     * @param taskDto
     * @return
     */
    @PutMapping("/status")
    public Result editStatus(@RequestBody TaskDto taskDto){
        String taskId = taskDto.getTaskId();
        Integer status = taskDto.getStatus();
        if (StringUtils.isBlank(taskId)&&status!=1&&status!=0){
            throw new BaseException("请求参数错误");
        }
        boolean result= taskService.editStatus(taskId,status);
        ThrowUtils.throwIf(!result, "修改任务状态失败");
        return Result.success("修改任务状态成功");
    }
}
