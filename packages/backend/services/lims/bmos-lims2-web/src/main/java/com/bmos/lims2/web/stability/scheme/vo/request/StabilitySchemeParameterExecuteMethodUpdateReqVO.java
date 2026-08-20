package com.bmos.lims2.web.stability.scheme.vo.request;

import com.bmos.lims2.common.enums.ExecuteMethodEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 更新稳定性方案分析项执行方式请求VO（执行方式为ELN时须携带方法信息）
 */
@Data
@ApiModel("更新稳定性方案分析项执行方式请求VO")
public class StabilitySchemeParameterExecuteMethodUpdateReqVO {

    @ApiModelProperty(value = "分析项配置ID", required = true)
    @NotNull(message = "分析项配置ID不能为空")
    private Long parameterConfigId;

    @ApiModelProperty(value = "执行方式：LIMS/ELN", required = true)
    @NotNull(message = "执行方式不能为空")
    private ExecuteMethodEnum executeMethod;

    @ApiModelProperty("记录ID（执行方式为ELN时必填）")
    private Long recordId;

    @ApiModelProperty("记录版本ID（执行方式为ELN时必填）")
    private Long recordVersionId;

    @ApiModelProperty("记录编码（执行方式为ELN时必填）")
    private String recordCode;

    @ApiModelProperty("记录项ID（执行方式为ELN时建议携带）")
    private Long recordItemId;
}
