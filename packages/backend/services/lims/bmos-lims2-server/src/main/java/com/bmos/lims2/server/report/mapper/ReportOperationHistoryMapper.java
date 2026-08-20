package com.bmos.lims2.server.report.mapper;

import com.bmos.lims2.server.report.entity.ReportOperationHistory;
import com.bmos.mybatis.mapper.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReportOperationHistoryMapper extends BaseMapperX<ReportOperationHistory> {

    List<ReportOperationHistory> selectByTaskId(@Param("taskId") Long taskId);
}


