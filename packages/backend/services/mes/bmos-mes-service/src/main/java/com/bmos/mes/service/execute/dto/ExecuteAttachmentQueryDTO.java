package com.bmos.mes.service.execute.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ApiModel("附件查询")
@ToString
public class ExecuteAttachmentQueryDTO {



    @ApiModelProperty(value = "文件类型",required = true)
    @NotEmpty
    private String type;

    /**
     * 生产计划id
     */
    @ApiModelProperty(value = "生产计划id",required = true)
    @NotNull
    private Long productPlanId;

    /**
     * 记录项id
     */
    @ApiModelProperty(value = "记录项id",required = true)
    @NotNull
    private Long recordItemId;

    /**
     * 历史工序步骤id
     */
    @ApiModelProperty(value = "工序步骤id",required = true)
    @NotNull
    private Long procedureStepId;

    /**
     * 是否复用
     */
    @ApiModelProperty(value = "是否复用",required = true)
    @NotNull
    private Boolean reuse;

    /**
     * 复制版本（默认0）
     */
    @ApiModelProperty(value = "复制版本号",required = true)
    @NotNull
    private Long copyVersion;
    /**
     * 工艺换班次数
     */
    @ApiModelProperty(value = "工艺换班次数",required = true)
    @NotNull
    private Integer processChangeNumber;

    /**
     * 工序换班次数
     */
    @ApiModelProperty(value = "工序换班次数",required = true)
    @NotNull
    private Integer procedureChangeNumber;
}
