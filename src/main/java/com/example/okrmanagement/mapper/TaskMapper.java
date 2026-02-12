package com.example.okrmanagement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.okrmanagement.entity.Task;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TaskMapper extends BaseMapper<Task> {
}
