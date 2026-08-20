package com.bmos.mes.service.storage.manage.dto;

import com.bmos.mes.service.execute.dto.BusinessDataHandleBaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@ApiModel("投料DTO")
@Data
public class ChargeStorageMaterialDTO extends BusinessDataHandleBaseDTO {

    @ApiModelProperty("操作人id")
    @NotNull
    private String operatorId;

    @ApiModelProperty("设备id")
    private Long deviceId;

    @ApiModelProperty("投入物料件列表")
    @NotEmpty
    private List<ChargeMaterial> chargeMaterialList;

    @ApiModelProperty("投料回收主键id(非componentId)")
    @NotNull
    private Long chargeRecycleComponentId;

    @Data
    public static class ChargeMaterial {

        @ApiModelProperty("投入物料件id")
        @NotNull
        private Long storageMaterialId;

        @ApiModelProperty("投料量")
        @NotNull
        private BigDecimal chargeQuantity;

        @ApiModelProperty("单位id")
        @NotNull
        private Long unitId;

    }


}
