package com.bmos.mes.service.output.weigh.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 确认称量人dto
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/4/18 14:02
 */
@ApiModel("确认称量人dto")
@Data
public class OutputMakeSureWeigherDTO {

    /**
     * 生产计划id
     */
    @ApiModelProperty(value = "生产计划id", example = "1", required = true)
    @NotNull
    private Long productPlanId;

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
     * 组件id
     */
    @ApiModelProperty(value = "组件id", example = "1")
    @NotNull
    private Long componentId;

    /**
     * 称量人id
     */
    @ApiModelProperty(value = "称量人id", example = "1", required = true)
    @NotBlank
    private String weigherId;

    /**
     * 复核人id
     */
    @ApiModelProperty(value = "复核人id", example = "1", required = true)
    @NotBlank
    private String reCheckerId;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注", example = "备注")
    @Length(max = 200)
    private String remark;
}
