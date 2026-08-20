package com.bmos.mes.service.platform.plan.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Getter
@Setter
@ApiModel("BatchNextUseCodeDTO:获取下一个使用的编码数据")
public class BatchNextUseCodeDTO {

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

    @NotNull
    @ApiModelProperty("生成数量")
    private Integer num;
}
