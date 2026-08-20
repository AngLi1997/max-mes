package com.bmos.mes.service.platform.plan.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Map;

@ApiModel("NextUseCodeDTO:获取下一个使用的编码数据")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NextUseCodeDTO {

    @NotEmpty
    @ApiModelProperty("生产批号规则 PRODUCT_PLAN_BATCH_NO 生产计划批号规则 PRODUCT_PLAN_NO")
    private String type;

    @NotNull
    @ApiModelProperty("工序id")
    private Long processId;

    @ApiModelProperty("编码规则code")
    private String code;

    @NotNull
    @ApiModelProperty("编码规则详情传参")
    private Map<String, String> fields;
}
