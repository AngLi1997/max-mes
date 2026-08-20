package com.bmos.lims2.web.inspect.scheme.vo.request;

import com.bmos.lims2.common.enums.ExecuteMethodEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * @Description: 更新方案分析项执行方式请求VO（执行方式为ELN时须携带方法信息）
 * @Author: yigaohui
 * @Date: 2025/11/12 00:00
 */
@Getter
@Setter
@ApiModel("更新方案分析项执行方式请求VO")
public class InspectionSchemeParameterExecuteMethodUpdateReqVO {

    @ApiModelProperty(value = "分析项配置ID", required = true)
    @NotNull(message = "分析项配置ID不能为空")
    private Long parameterConfigId;

    @ApiModelProperty(value = "执行方式：LIMS/ELN", required = true)
    @NotNull(message = "执行方式不能为空")
    private ExecuteMethodEnum executeMethod;

    @ApiModelProperty(value = "记录ID（执行方式为ELN时必填）")
    private Long recordId;

    @ApiModelProperty(value = "记录版本ID（执行方式为ELN时必填）")
    private Long recordVersionId;

    @ApiModelProperty(value = "记录编码（执行方式为ELN时必填）")
    private String recordCode;

    @ApiModelProperty(value = "记录项ID（执行方式为ELN时建议携带）")
    private Long recordItemId;
}

