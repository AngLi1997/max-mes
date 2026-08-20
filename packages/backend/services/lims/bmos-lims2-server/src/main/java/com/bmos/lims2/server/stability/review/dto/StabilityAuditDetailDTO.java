package com.bmos.lims2.server.stability.review.dto;

import com.bmos.lims2.server.inspect.entry.dto.InspectionOrderEntryDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 稳定性结果审核详情DTO
 * 继承 InspectionOrderEntryDTO，确保与常规样品审核详情结构一致。
 * 额外携带稳定性特有字段：planCode、schemeCode、schemeVersion、remark。
 */
@Getter
@Setter
public class StabilityAuditDetailDTO extends InspectionOrderEntryDTO {

    @ApiModelProperty("稳定性考察编号")
    private String planCode;

    @ApiModelProperty("稳定性考察方案编码")
    private String schemeCode;

    @ApiModelProperty("稳定性考察方案版本")
    private String schemeVersion;

    @ApiModelProperty("备注")
    private String remark;
}
