package com.bmos.mes.service.audit.convert;

import com.bmos.mes.service.audit.dto.CheckoutFlowAuditUserDTO;
import com.bmos.mes.service.audit.dto.SaveFlowAuditUserDTO;
import com.bmos.mes.service.audit.model.FlowAuditUser;
import com.bmos.mes.service.audit.vo.FlowAuditUserVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author renjinguang
 */
@Mapper
public interface FlowAuditUserConvert {
    FlowAuditUserConvert INSTANCE = Mappers.getMapper(FlowAuditUserConvert.class);

    List<FlowAuditUser> convertToUserList(List<SaveFlowAuditUserDTO> auditUserList);

    List<FlowAuditUserVO> convertToUserListVo(List<FlowAuditUser> auditUserList);

    List<CheckoutFlowAuditUserDTO> convertToCheckoutUserList(List<SaveFlowAuditUserDTO> userList);

    List<CheckoutFlowAuditUserDTO> convert2CheckoutUserList(List<FlowAuditUserVO> auditUserList);
}
