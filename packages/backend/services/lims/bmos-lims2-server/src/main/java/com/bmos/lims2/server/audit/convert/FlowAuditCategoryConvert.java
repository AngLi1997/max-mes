package com.bmos.lims2.server.audit.convert;

import com.bmos.lims2.server.audit.entity.FlowAuditCategory;
import com.bmos.lims2.server.audit.vo.FlowAuditCategoryVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author renjinguang
 */
@Mapper
public interface FlowAuditCategoryConvert {
    FlowAuditCategoryConvert INSTANCE = Mappers.getMapper(FlowAuditCategoryConvert.class);

    List<FlowAuditCategoryVO> convertToCategory(List<FlowAuditCategory> list);


}
