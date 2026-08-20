package com.bmos.mes.service.storage.manage.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 物料预定参数（移动端）
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/3/13 14:43
 */
@Data
@ApiModel("物料预定参数(移动端)")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StorageMaterialReserveDTO {

    /**
     * 暂存物料id
     */
    @ApiModelProperty(value = "暂存物料id", example = "1", required = true)
    @NotNull
    private Long storageMaterialId;

    /**
     * 产品id
     */
    @ApiModelProperty(value = "产品id", example = "1", required = true)
    @NotNull
    private Long productId;

    /**
     * 工艺id
     */
    @ApiModelProperty(value = "工艺id", example = "1", required = true)
    @NotNull
    private Long processId;

    /**
     * 生产批次id
     */
    @ApiModelProperty(value = "生产批次id", example = "1", required = true)
    @NotNull
    private Long batchId;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注", example = "备注", required = true)
    @NotBlank
    @Length(max = 200)
    private String remark;

    /**
     * 操作人id
     */
    @ApiModelProperty(value = "操作人id", example = "1", required = true)
    @NotBlank
    private String operatorId;

    /**
     * 复核人id
     */
    @ApiModelProperty(value = "复核人id", example = "1", required = true)
    @NotBlank
    private String reCheckerId;

    /**
     * 皮重
     */
    @ApiModelProperty("皮重")
    private BigDecimal tareWeight;

    /**
     * 毛重
     */
    @ApiModelProperty("毛重")
    private BigDecimal grossWeight;
}
