package com.bmos.lims2.server.inspect.entry.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bmos.lims2.server.inspect.entry.entity.InspectionEntryHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 检验录入历史Mapper
 *
 * @author system
 * @since 2025/01/30
 */
@Mapper
public interface InspectionEntryHistoryMapper extends BaseMapper<InspectionEntryHistory> {

    /**
     * 根据录入记录ID查询历史记录
     *
     * @param entryRecordId 录入记录ID
     * @return 历史记录列表
     */
    List<InspectionEntryHistory> selectByEntryRecordId(@Param("entryRecordId") Long entryRecordId);

    /**
     * 根据任务ID查询历史记录
     *
     * @param taskId 任务ID
     * @return 历史记录列表
     */
    List<InspectionEntryHistory> selectByTaskId(@Param("taskId") Long taskId);

    /**
     * 根据数据点ID查询历史记录
     *
     * @param dataPointId 数据点ID
     * @return 历史记录列表
     */
    List<InspectionEntryHistory> selectByDataPointId(@Param("dataPointId") Long dataPointId);
}
