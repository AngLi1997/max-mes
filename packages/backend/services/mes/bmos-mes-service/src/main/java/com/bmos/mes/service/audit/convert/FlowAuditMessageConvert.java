package com.bmos.mes.service.audit.convert;

import com.bmos.mes.service.audit.dto.CheckoutFlowAuditMegDTO;
import com.bmos.mes.service.audit.dto.CheckoutFlowAuditUserDTO;
import com.bmos.mes.service.audit.dto.SaveFlowAuditMegDTO;
import com.bmos.mes.service.audit.dto.SaveFlowAuditUserDTO;
import com.bmos.mes.service.audit.model.FlowAuditMessage;
import com.bmos.mes.service.audit.vo.FlowAuditMegVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author renjinguang
 */
@Mapper
public interface FlowAuditMessageConvert {
    FlowAuditMessageConvert INSTANCE = Mappers.getMapper(FlowAuditMessageConvert.class);

    List<FlowAuditMessage> convertToUserList(List<SaveFlowAuditMegDTO> auditMegDTOList);

    List<FlowAuditMegVO> convertToUserListVo(List<FlowAuditMessage> auditMegList);

    List<CheckoutFlowAuditMegDTO> convertToCheckoutUser(List<SaveFlowAuditMegDTO> megUserList);

    List<CheckoutFlowAuditMegDTO> convert2CheckoutUser(List<FlowAuditMegVO> auditMegDTOList);
}
