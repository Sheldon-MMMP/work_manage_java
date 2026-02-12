package com.example.okrmanagement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.okrmanagement.entity.TimeRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface TimeRecordMapper extends BaseMapper<TimeRecord> {
    List<TimeRecord> findByUserIdAndStartTimeBetween(@Param("userId") Long userId,
                                                     @Param("start") LocalDateTime start,
                                                     @Param("end") LocalDateTime end);
    List<TimeRecord> findByUserIdOrderByStartTimeDesc(@Param("userId") Long userId);
    List<TimeRecord> findTodayRecordsByUserId(@Param("userId") Long userId);
}
