package com.bmos.mes.service.weigh.free.dto;

import com.bmos.common.validate.EnumValidate;
import com.bmos.mes.common.enums.ingredient.WeighMode;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 自由称量打码DTO
 * @author liang
 * @version 1.0.0
 * @date 2025/2/25 09:57
 */
@ApiModel("自由称量打码DTO")
@Data
public class FreeWeighDTO {

    @ApiModelProperty(value = "物料id", example = "1")
    @NotNull
    private Long materialId;

    @ApiModelProperty(value = "物料批次编号", example = "物料批次编号")
    @NotBlank
    @Length(max = 100)
    private String storageMaterialBatchNo;

    @ApiModelProperty(value = "有效期至", example = "2025-02-05")
    @NotNull
    private LocalDate expiredDate;

    @ApiModelProperty(value = "称量人员id", example = "1")
    private String weigherId;

    @ApiModelProperty(value = "复核人员id", example = "1")
    private String reCheckerId;

    @ApiModelProperty(value = "皮重", example = "1.00")
    private BigDecimal tareWeight;

    @ApiModelProperty(value = "毛重", example = "1.00")
    private BigDecimal grossWeight;

    @ApiModelProperty(value = "净重", example = "1.00")
    private BigDecimal netWeight;

    @ApiModelProperty(value = "单位id", example = "1")
    private Long unitId;

    @ApiModelProperty(value = "容器id", example = "1")
    private Long containerId;

    @ApiModelProperty(value = "货位id", example = "1")
    private Long materialPositionId;

    @ApiModelProperty(value = "产品id", example = "1")
    private Long productId;

    @ApiModelProperty(value = "生产批号", example = "批次1")
    private String batchNo;

    @ApiModelEnumProperty(value = "称量模式", enumClass = WeighMode.class, required = true)
    @EnumValidate(WeighMode.class)
    @NotNull
    private Integer weighMode;

    @ApiModelProperty("称量设备id")
    private Long deviceId;
}
