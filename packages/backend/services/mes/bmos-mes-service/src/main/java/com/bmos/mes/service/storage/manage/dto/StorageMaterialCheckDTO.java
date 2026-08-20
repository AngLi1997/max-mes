package com.bmos.mes.service.storage.manage.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 暂存物料批次盘库参数
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/2/5 11:51
 */
@Data
@ApiModel("暂存物料批次盘库参数")
public class StorageMaterialCheckDTO {

    /**
     * 暂存物料件id
     */
    @ApiModelProperty(value = "暂存物料件id", example = "1", required = true)
    @NotNull
    private Long storageMaterialId;

    /**
     * 初始量
     */
    @ApiModelProperty(value = "初始量", example = "1.000", required = true)
    @NotNull
    @DecimalMin(value = "0.000000001", message = "初始量必须大于0")
    @DecimalMax(value = "9999999999.999999999", message = "初始量不能大于9999999999.999999999")
    private BigDecimal initQuantity;

    /**
     * 消耗量
     */
    @ApiModelProperty(value = "消耗量", example = "1.000", required = true)
    @NotNull
    @DecimalMin(value = "0", message = "消耗量必须为非负数")
    @DecimalMax(value = "9999999999.999999999", message = "初始量不能大于9999999999.999999999")
    private BigDecimal consumeQuantity;

    /**
     * 可用量
     */
    @ApiModelProperty(value = "可用量", example = "1.000", required = true)
    @NotNull
    @DecimalMin(value = "0", message = "初始量必须为非负数")
    @DecimalMax(value = "9999999999.999999999", message = "初始量不能大于9999999999.999999999")
    private BigDecimal availableQuantity;

    /**
     * 是否用尽
     */
    @ApiModelProperty(value = "是否用尽", required = true, example = "true")
    @NotNull
    private Boolean useUp;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注", example = "备注", required = true)
    @NotBlank
    @Length(max = 200)
    private String remark;

    /**
     * 盘点人id
     */
    @ApiModelProperty(value = "盘点人id", example = "1", required = true)
    @NotBlank
    private String checkerId;

    /**
     * 复核人id
     */
    @ApiModelProperty(value = "复核人id", example = "1", required = true)
    @NotBlank
    private String reCheckerId;
}
