package com.bmos.mes.service.storage.manage.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 物料预定组件:预定暂存物料DTO
 */
@Data
@ApiModel("批量预定暂存物料DTO")
public class ReserveComponentReserveMaterialDTO {

    /**
     * 暂存物料id
     */
    @NotEmpty
    private List<Long> storageMaterialIdList;

    /**
     * 生产计划id
     */
    @NotNull
    private Long productPlanId;

}
