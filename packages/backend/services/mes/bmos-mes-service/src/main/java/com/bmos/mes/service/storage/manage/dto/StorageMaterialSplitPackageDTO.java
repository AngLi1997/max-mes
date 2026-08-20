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
 * 拆包出库参数（移动端）
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/3/13 14:43
 */
@Data
@ApiModel("拆包出库参数(移动端)")
public class StorageMaterialSplitPackageDTO {

    /**
     * 暂存物料件id
     */
    @ApiModelProperty(value = "暂存物料件id", example = "1", required = true)
    @NotNull
    private Long storageMaterialId;

    /**
     * 出库量
     */
    @ApiModelProperty(value = "出库数量", required = true)
    @DecimalMin(value = "0.000000001", message = "出库数量必须大于0")
    @DecimalMax(value = "9999999999.999999999", message = "出库数量不能大于9999999999.999999999")
    @NotNull
    private BigDecimal quantity;

    /**
     * /备注
     */
    @ApiModelProperty(value = "备注", example = "备注")
    @Length(max = 200)
    private String remark;

    /**
     * 容器id
     */
    @ApiModelProperty(value = "容器ID", example = "1")
    private Long containerId;

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
}
