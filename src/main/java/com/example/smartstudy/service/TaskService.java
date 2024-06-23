package com.example.smartstudy.service;

import com.example.smartstudy.model.dto.TaskDto;
import com.example.smartstudy.model.vo.TaskVo;
import org.springframework.cache.annotation.CacheConfig;

import java.util.List;


public interface TaskService {

    //查询所有任务
    List<TaskVo> allTask(String name);

    //添加任务
    boolean addTask(TaskDto taskDto);

    //修改任务
    boolean editTask(TaskDto taskDto);

    //删除任务
    boolean removeTask(String taskId);

    //修改任务状态
    boolean editStatus(String taskId, Integer status);

    //查询单条内容
    TaskVo queryOneTask(String content, String name);
}
