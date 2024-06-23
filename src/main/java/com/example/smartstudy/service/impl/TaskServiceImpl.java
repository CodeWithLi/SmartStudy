package com.example.smartstudy.service.impl;

import com.example.smartstudy.exception.BaseException;
import com.example.smartstudy.mapper.TaskMapper;
import com.example.smartstudy.model.dto.TaskDto;
import com.example.smartstudy.model.po.TaskPo;
import com.example.smartstudy.model.vo.TaskVo;
import com.example.smartstudy.service.TaskService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    @Resource
    private TaskMapper taskMapper;

    // TODO: 2024/4/28 调用讯飞文本合规
    //查询所有
    @Override
    public List<TaskVo> allTask(String name) {
        List<TaskPo> pos = taskMapper.allTask(name);
        return pos.stream().map(taskPo -> {
            TaskVo vo = new TaskVo();
            BeanUtils.copyProperties(taskPo, vo);
            return vo;
        }).collect(Collectors.toList());
    }

    //添加任务
    @Override
    public boolean addTask(TaskDto taskDto) {
        taskDto.setTaskId(UUID.randomUUID().toString());
        taskDto.setCreateTime(new Date());
        taskDto.setStatus(0);
        int result=taskMapper.insertTask(taskDto);
        if (result==0){
            throw new BaseException("添加失败");
        }
        return result==1;
    }

    //修改任务
    @Override
    public boolean editTask(TaskDto taskDto) {
        int result=taskMapper.updateTask(taskDto);
        if (result==0){
            throw new BaseException("修改失败");
        }
        return result==1;
    }

    //删除任务
    @Override
    public boolean removeTask(String taskId) {
        int result=taskMapper.deleteTask(taskId);
        if (result==0){
            throw new BaseException("删除失败");
        }
        return result==1;

    }

    //修改任务状态
    @Override
    public boolean editStatus(String taskId, Integer status) {
        int result=taskMapper.updateTaskStatus(taskId,status);
        if (result==0){
            throw new BaseException("删除失败");
        }
        return result==1;

    }

    //查询单调内容
    @Override
    public TaskVo queryOneTask(String content, String name) {
       TaskPo taskPo= taskMapper.selectOneTask(content,name);
       if (taskPo==null){
           throw new BaseException("该任务不存在");
       }
       TaskVo vo=new TaskVo();
       BeanUtils.copyProperties(taskPo,vo);
        return vo;
    }
}
