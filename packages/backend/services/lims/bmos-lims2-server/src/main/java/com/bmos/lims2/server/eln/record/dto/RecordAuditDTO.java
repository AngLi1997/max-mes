package com.bmos.lims2.server.eln.record.dto;

import com.bmos.lims2.common.enums.AuditCategoryCodeEnum;
import com.bmos.lims2.server.audit.dto.AuditPageBaseQueryDTO;
import com.bmos.lims2.server.audit.dto.FlowAuditTaskDTO;
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

    @ApiModelProperty("记录编码")
    private String code;

    public FlowAuditTaskDTO convertAuditTaskDTO() {
        return super.convertAuditTaskDTO(AuditCategoryCodeEnum.METHOD_AUDIT);
    }

}
