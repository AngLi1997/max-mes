package com.bmos.lims2.server.audit.convert;

import com.bmos.lims2.server.audit.dto.CheckoutFlowAuditUserDTO;
import com.bmos.lims2.server.audit.dto.SaveFlowAuditUserDTO;
import com.bmos.lims2.server.audit.entity.FlowAuditUser;
import com.bmos.lims2.server.audit.vo.FlowAuditUserVO;
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
