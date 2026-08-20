package com.bmos.lims2.server.audit.convert;

import com.bmos.lims2.server.audit.vo.AuditHistoryExportVO;
import com.bmos.lims2.server.audit.vo.AuditHistoryVO;
import com.bmos.lims2.server.audit.vo.TaskHistoryExportVO;
import com.bmos.lims2.server.audit.vo.TaskHistoryVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author renjinguang
 */
@Mapper
public interface FlowAuditExportConvert {
    FlowAuditExportConvert INSTANCE = Mappers.getMapper(FlowAuditExportConvert.class);

    default List<AuditHistoryExportVO> convertToHistoryExport(List<AuditHistoryVO> list){
        return list.stream().map(this::convertToHistoryExport).collect(Collectors.toList());
    }

    List<TaskHistoryExportVO> converToTaskExport(List<TaskHistoryVO> list);

    @Mapping(target = "processState", expression = "java(com.bmos.common.base.enums.CommonEnum.getEnumByName(com.bmos.lims2.common.enums.FlowStateEnum.class, vo.getProcessState()))")
    AuditHistoryExportVO convertToHistoryExport(AuditHistoryVO vo);
}
