package com.bmos.mes.service.audit.convert;

import com.bmos.mes.service.audit.dto.SaveAuditDTO;
import com.bmos.mes.service.audit.model.FlowAuditVersion;
import com.bmos.mes.service.audit.vo.FlowAuditDetailVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author renjinguang
 */
@Mapper
public interface FlowAuditVersionConvert {
    FlowAuditVersionConvert INSTANCE = Mappers.getMapper(FlowAuditVersionConvert.class);


    FlowAuditVersion convertToVersion(SaveAuditDTO dto);

    FlowAuditDetailVO convertToVo(FlowAuditVersion version);

}
