package com.example.okrmanagement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.okrmanagement.entity.TaskList;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TaskListMapper extends BaseMapper<TaskList> {
}
