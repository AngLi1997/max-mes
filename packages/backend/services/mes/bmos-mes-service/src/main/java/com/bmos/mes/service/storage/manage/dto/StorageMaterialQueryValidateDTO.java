package com.bmos.mes.service.storage.manage.dto;

import com.bmos.common.exception.BmosException;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.storage.manage.model.StorageMaterial;
import com.bmos.mes.service.storage.manage.vo.StorageMaterialDetailVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@ApiModel("物料查询以及基础校验DTO")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class StorageMaterialQueryValidateDTO {

    @ApiModelProperty("物料件号或容器编号")
    @NotEmpty
    private String no;

    @ApiModelProperty("生产批次")
    @NotNull
    private Long productPlanId;

    @ApiModelProperty(value = "是否校验可用状态", hidden = true)
    private boolean validateAvailable;

    @ApiModelProperty(value = "是否校验批次状态", hidden = true)
    private boolean validateBatch;

    @ApiModelProperty(value = "是否校验预定状态", hidden = true)
    private boolean validateReserve;

    @ApiModelProperty(value = "是否校验出库状态", hidden = true)
    private boolean validateOutbound;

    public StorageMaterialQueryValidateDTO validateAll() {
        this.validateAvailable = true;
        this.validateBatch = true;
        this.validateReserve = true;
        this.validateOutbound = true;
        return this;
    }

    public void validateAll(StorageMaterialDetailVO detail) {
        StorageMaterial storageMaterial = detail.getStorageMaterial();
        // 物料件生效状态
        storageMaterial.availableValidate();
        // 批次有效期、质量状态
        detail.getStorageMaterialBatch().availableValidate();
        // 预定校验
        if (detail.isOrderByOthers()) {
            throw new BmosException(MesResponseCode.RESERVED_BY_UNRELATED_BATCH);
        }
        // 出库校验
        storageMaterial.outboundValidate();
    }

}
