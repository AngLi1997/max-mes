package com.bmos.lims2.server.inspect.scheme.mapper;

import com.bmos.lims2.server.inspect.scheme.dto.response.ReportDataPointRespDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface InspectionSchemeReportMapper {

    List<ReportDataPointRespDTO> pageReportDataPoints(
            @Param("schemeId") Long schemeId,
            @Param("inspectItemId") Long inspectItemId,
            @Param("parameterId") Long parameterId
    );
}


