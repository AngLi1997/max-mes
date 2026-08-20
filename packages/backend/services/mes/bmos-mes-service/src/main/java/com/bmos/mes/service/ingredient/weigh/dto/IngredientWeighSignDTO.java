package com.bmos.mes.service.ingredient.weigh.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/4/18 17:20
 */
@Data
@ApiModel("配料称量签名DTO")
public class IngredientWeighSignDTO {

    /**
     * 配料计划id
     */
    @ApiModelProperty(value = "配料计划id", example = "1", required = true)
    @NotNull
    private Long planId;

    /**
     * 组件id
     */
    @ApiModelProperty(value = "组件id", example = "1", required = true)
    @NotNull
    private Long componentId;

    /**
     * 工序步骤模型id
     */
    @ApiModelProperty(value = "工序步骤模型id", example = "1", required = true)
    @NotNull
    private Long procedureStepModelId;

    /**
     * 拷贝版本
     */
    @ApiModelProperty(value = "拷贝版本(默认0)", example = "1", required = true)
    @NotNull
    private Long copyVersion;

    /**
     * 称量人id
     */
    @ApiModelProperty(value = "称量人id", example = "1")
    @NotNull
    private String weigherId;

    /**
     * 复核人id
     */
    @ApiModelProperty(value = "复核人id", example = "1")
    @NotNull
    private String reCheckerId;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注", example = "备注")
    @Length(max = 200)
    private String remark;
}
