package com.bmos.mes.service.storage.manage.dto;

import com.bmos.mes.common.enums.ingredient.WeighSignStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.math.BigDecimal;

/**
 * 物料管理参数
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/2/5 11:51
 */
@Data
@ApiModel("物料管理参数")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StorageMaterialManageCreateDTO {

    /**
     * 货品批次id
     */
    @ApiModelProperty(value = "货品批次id", example = "177253833300", required = true)
    @NotNull
    private Long storageMaterialBatchId;

    /**
     * 货位id
     */
    @ApiModelProperty(value = "货位id", example = "1", required = true)
    @NotNull
    private Long positionId;

    /**
     * 单件量
     */
    @ApiModelProperty(value = "单件量", example = "9999999999.999999999", required = true)
    @NotNull
    @DecimalMin("0.000000001")
    @DecimalMax("9999999999.999999999")
    private BigDecimal singleQuantity;

    /**
     * 单件量单位id
     */
    @ApiModelProperty(value = "单件量单位id", example = "1760853376209391616", required = true)
    @NotNull
    private Long singleUnitId;

    /**
     * 新增件数
     */
    @ApiModelProperty(value = "新增件数", example = "99", required = true)
    @NotNull
    @Min(1)
    @Max(99)
    private Integer size;

    /**
     * 操作人id
     */
    @ApiModelProperty(value = "操作人id", example = "1", required = true)
    @NotBlank
    private String operatorId;

    /**
     * 签名状态 仅限service使用
     */
    @ApiModelProperty(value = "签名状态", hidden = true)
    private WeighSignStatus weighSignStatus;

    /**
     * 容器id 仅限service使用
     */
    @ApiModelProperty(value = "容器id", example = "1")
    private Long containerId;

    /**
     * 容器名称 仅限service使用
     */
    @ApiModelProperty(value = "容器名称", example = "不锈钢盆", hidden = true)
    private String containerName;

    /**
     * 保存日志 仅限service使用
     */
    @ApiModelProperty(value = "是否保存日志", example = "false", hidden = true)
    private Boolean saveLog = true;
}
