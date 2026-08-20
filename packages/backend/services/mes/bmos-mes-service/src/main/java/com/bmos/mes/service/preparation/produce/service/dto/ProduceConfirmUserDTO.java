package com.bmos.mes.service.preparation.produce.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * 配液产出人员确认DTO
 */
@Getter
@Setter
@ApiModel("配液产出人员确认DTO")
public class ProduceConfirmUserDTO extends PreparationProduceComponentDTO  {

    /**
     * 记录项id
     */
    @ApiModelProperty(value = "记录项id", required = true)
    @NotNull
    private Long recordItemId;

    /**
     * 记录项版本id
     */
    @ApiModelProperty(value = "记录项版本id", required = true)
    @NotNull
    private Long recordVersionId;

    /**
     * 配液单id
     */
    @ApiModelProperty(value = "配液单id", required = true)
    @NotNull
    private Long preparationPlanId;

    /**
     * 物料批次编号
     */
    @ApiModelProperty(value = "物料批次编号", required = true)
    private String materialBatchNo;

    /**
     * 物料编码
     */
    @ApiModelProperty(value = "物料编码", required = true)
    @NotEmpty
    private String materialMergeCode;

    /**
     * 有效期
     */
    @ApiModelProperty(value = "有效期", required = true)
    private LocalDate expireDate;

    /**
     * 产出人员id
     */
    @ApiModelProperty(value = "产出人员id", required = true)
    @NotEmpty
    private String confirmUserId;

    /**
     * 复核人员id
     */
    @ApiModelProperty(value = "复核人员id", required = true)
    @NotEmpty
    private String reCheckUserId;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 配方物料id
     */
    @ApiModelProperty(value = "配方物料id", required = true)
    @NotNull
    private Long formulaMaterialId;

}
