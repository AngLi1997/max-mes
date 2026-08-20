package com.bmos.mes.service.requisition.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("物料预定组件:取消预定DTO")
@Data
public class ReserveComponentCancelReserveDTO {

    @ApiModelProperty("组件实例id")
    private Long componentInstanceId;

    @ApiModelProperty("物料件id")
    private Long storageMaterialId;

}
