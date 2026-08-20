package com.bmos.mes.service.storage.log.dto;

import com.bmos.mes.common.enums.storage.StorageOperateTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 货位日志参数
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/2/5 11:51
 */
@Data
@ApiModel("货位日志参数")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StorageMaterialPositionLogDTO {

    /**
     * 物料件id
     */
    @ApiModelProperty(value = "物料件id", example = "1", required = true)
    @NotNull
    private Long storageMaterialId;

    /**
     * 操作类型
     */
    @ApiModelProperty(value = "操作类型", example = "INBOUND", required = true)
    @NotNull
    private StorageOperateTypeEnum operateType;

    /**
     * 操作数量(目标单位数量)
     */
    @ApiModelProperty(value = "操作数量", example = "1", required = true)
    @NotNull
    @DecimalMin(value = "0.000000001", message = "操作数量必须大于0")
    @DecimalMax(value = "9999999999.999999999", message = "操作数量不能大于9999999999.999999999")
    private BigDecimal quantity;

    /**
     * 单位id(目标单位id)
     */
    @ApiModelProperty(value = "单位id", example = "1", required = true)
    @NotNull
    private Long unitId;

    /**
     * 递交人id
     */
    @ApiModelProperty(value = "递交人id", example = "1", required = true)
    @NotBlank
    private String senderId;

    /**
     * 接收人id
     */
    @ApiModelProperty(value = "接收人id", example = "1", required = true)
    @NotBlank
    private String receiverId;

    /**
     * 产品id
     */
    @ApiModelProperty(value = "产品id", example = "1")
    private Long productId;

    /**
     * 产品名称
     */
    @ApiModelProperty(value = "产品名称", example = "人血白蛋白")
    private String productName;

    /**
     * 产品编码
     */
    @ApiModelProperty(value = "产品编码", example = "CX005")
    private String productCode;

    /**
     * 产品批号
     */
    @ApiModelProperty(value = "产品批号", example = "CX0052402001")
    private String productBatchNo;

    /**
     * 来源/去向/备注
     */
    @ApiModelProperty(value = "来源/去向/备注", example = "备注", required = true)
    private String remark;

    /**
     * 货位id
     */
    @ApiModelProperty(value = "货位id", example = "1")
    private Long materialPositionId;

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
