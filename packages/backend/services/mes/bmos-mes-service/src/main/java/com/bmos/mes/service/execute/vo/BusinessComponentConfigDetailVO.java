package com.bmos.mes.service.execute.vo;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("业务组件配置详情")
@JsonIgnoreProperties(ignoreUnknown = true)
public class BusinessComponentConfigDetailVO {

    @ApiModelProperty("配置信息")
    private String configInfo;

    @ApiModelProperty("工序步骤id")
    private Long procedureStepId;

    @ApiModelProperty("组件id")
    private Long componentId;

    @ApiModelProperty("组件标识")
    private Long fieldId;

    @ApiModelProperty("工艺id")
    private Long processId;

    @ApiModelProperty("记录项id")
    private Long recordItemId;

}
