package com.example.smartstudy.mapper;

import com.example.smartstudy.model.dto.TaskDto;
import com.example.smartstudy.model.po.TaskPo;

import java.util.List;

public interface TaskMapper {

    //根据姓名查询所有任务
    List<TaskPo> allTask(String name);

    //添加任务
    int insertTask(TaskDto taskDto);

    //修改任务
    int updateTask(TaskDto taskDto);

    //删除任务
    int deleteTask(String taskId);

    //修改任务状态
    int updateTaskStatus(String taskId, Integer status);

    //查询单条内容
    TaskPo selectOneTask(String content, String name);
}
