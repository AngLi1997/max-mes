package com.bmos.lims2.server.task.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bmos.lims2.server.task.entity.TaskStatusHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 任务状态变更历史Mapper接口
 * 
 * @author system
 * @since 2025/01/29
 */
@Mapper
public interface TaskStatusHistoryMapper extends BaseMapper<TaskStatusHistory> {

    /**
     * 查询任务状态变更历史
     * 
     * @param taskId 任务ID
     * @return 状态变更历史列表
     */
    List<TaskStatusHistory> selectByTaskId(@Param("taskId") Long taskId);
}