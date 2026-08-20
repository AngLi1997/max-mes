package com.bmos.lims2.server.inspect.retention.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bmos.lims2.server.inspect.retention.dto.RetentionObservationTaskListDTO;
import com.bmos.lims2.server.inspect.retention.dto.RetentionObservationTaskPageQueryDTO;
import com.bmos.lims2.server.inspect.retention.entity.RetentionObservationTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: 留样观察任务Mapper
 * @Author: yigaohui
 * @Date: 2026/02/06
 */
@Mapper
public interface RetentionObservationTaskMapper extends BaseMapper<RetentionObservationTask> {

    /**
     * 分页查询留样观察任务列表
     * @param queryDTO 查询条件
     * @return 留样观察任务列表
     */
    List<RetentionObservationTaskListDTO> selectObservationTaskPageList(@Param("query") RetentionObservationTaskPageQueryDTO queryDTO);

    /**
     * 查询样品的所有未完成的早于指定任务的观察任务
     * @param sampleId 样品ID
     * @param taskId 当前任务ID
     * @return 早期未完成任务列表
     */
    List<RetentionObservationTask> selectEarlierUncompletedTasks(@Param("sampleId") Long sampleId, @Param("taskId") Long taskId);

    /**
     * 查询临期任务数量（指定天数内到期且未完成的任务）
     * @param days 天数
     * @return 临期任务数量
     */
    Long countUpcomingTasks(@Param("days") Integer days);
}
