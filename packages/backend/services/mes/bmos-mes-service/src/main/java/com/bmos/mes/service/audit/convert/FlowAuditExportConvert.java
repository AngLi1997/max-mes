package com.bmos.mes.service.audit.convert;

import com.bmos.mes.service.audit.vo.AuditHistoryExportVO;
import com.bmos.mes.service.audit.vo.AuditHistoryVO;
import com.bmos.mes.service.audit.vo.TaskHistoryExportVO;
import com.bmos.mes.service.audit.vo.TaskHistoryVO;
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

    @Mapping(target = "processState", expression = "java(com.bmos.common.base.enums.CommonEnum.getEnumByName(com.bmos.mes.common.enums.audit.FlowStateEnum.class, vo.getProcessState()))")
    AuditHistoryExportVO convertToHistoryExport(AuditHistoryVO vo);
}
