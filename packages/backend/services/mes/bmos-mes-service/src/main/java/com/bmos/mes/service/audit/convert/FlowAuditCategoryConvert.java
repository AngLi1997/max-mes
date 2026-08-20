package com.bmos.mes.service.audit.convert;

import com.bmos.mes.service.audit.model.FlowAuditCategory;
import com.bmos.mes.service.audit.vo.FlowAuditCategoryVO;
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
