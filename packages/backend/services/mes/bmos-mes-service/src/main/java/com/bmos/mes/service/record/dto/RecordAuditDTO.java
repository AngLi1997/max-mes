package com.bmos.mes.service.record.dto;

import com.bmos.mes.common.enums.audit.AuditCategoryCodeEnum;
import com.bmos.mes.service.audit.dto.AuditPageBaseQueryDTO;
import com.bmos.mes.service.audit.dto.FlowAuditTaskDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@ApiModel(value = "记录审核dto")
public class RecordAuditDTO extends AuditPageBaseQueryDTO {

    @ApiModelProperty(value = "记录名称")
    private String name;

    public FlowAuditTaskDTO convertAuditTaskDTO() {
        return super.convertAuditTaskDTO(AuditCategoryCodeEnum.RECODE);
    }

}
