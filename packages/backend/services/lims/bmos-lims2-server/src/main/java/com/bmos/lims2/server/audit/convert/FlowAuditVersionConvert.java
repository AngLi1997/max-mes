package com.bmos.lims2.server.audit.convert;

import com.bmos.lims2.server.audit.dto.SaveAuditDTO;
import com.bmos.lims2.server.audit.entity.FlowAuditVersion;
import com.bmos.lims2.server.audit.vo.FlowAuditDetailVO;
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
