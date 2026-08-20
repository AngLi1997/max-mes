package com.bmos.mes.service.output.weigh.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * 确认产出批次DTO
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/4/18 14:02
 */
@ApiModel("确认产出批次DTO")
@Data
public class OutputMakeSureBatchDTO {

    /**
     * 产出称量流程id
     */
    @ApiModelProperty(value = "产出称量流程id", example = "1", required = true)
    @NotNull
    private Long outputWeighProcessId;

    /**
     * 物料id
     */
    @ApiModelProperty(value = "物料id", example = "1", required = true)
    @NotNull
    private Long materialId;

    /**
     * 物料批号
     */
    @ApiModelProperty(value = "物料批号", example = "1", required = true)
    @NotBlank
    @Length(max = 100)
    private String storageMaterialBatchNo;

    /**
     * 有效期(物料批号不存在时必填)
     */
    @ApiModelProperty(value = "有效期(物料批号不存在时必填)", example = "2024-04-28")
    private LocalDate expiredDate;

    /**
     * 关联物料id
     */
    @ApiModelProperty(value = "关联物料id", example = "1")
    private Long relevanceMaterialId;

    /**
     * 关联物料批号
     */
    @ApiModelProperty(value = "关联物料批号", example = "1")
    private String relevanceMaterialBatchNo;
}
