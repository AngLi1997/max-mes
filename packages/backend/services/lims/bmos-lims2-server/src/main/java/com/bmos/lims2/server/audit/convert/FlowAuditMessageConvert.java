package com.bmos.lims2.server.audit.convert;

import com.bmos.lims2.server.audit.dto.CheckoutFlowAuditMegDTO;
import com.bmos.lims2.server.audit.dto.SaveFlowAuditMegDTO;
import com.bmos.lims2.server.audit.entity.FlowAuditMessage;
import com.bmos.lims2.server.audit.vo.FlowAuditMegVO;
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
