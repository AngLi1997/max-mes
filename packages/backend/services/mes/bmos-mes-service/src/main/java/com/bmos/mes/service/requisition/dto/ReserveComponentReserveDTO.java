package com.bmos.mes.service.requisition.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@ApiModel("物料预定组件批量预定")
@Data
public class ReserveComponentReserveDTO {

    @ApiModelProperty("组件实例id")
    @NotNull
    private Long componentInstanceId;

    @ApiModelProperty("物料件id列表")
    @NotEmpty
    private List<Long> storageMaterialIdList;

    @ApiModelProperty("配方物料id")
    @NotNull
    private Long formulaMaterialId;

}
