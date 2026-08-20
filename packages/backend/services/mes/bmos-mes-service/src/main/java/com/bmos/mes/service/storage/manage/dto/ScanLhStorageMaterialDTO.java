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

@ApiModel("联华称量中心扫描物料件查询以及校验DTO")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class ScanLhStorageMaterialDTO {

    @ApiModelProperty("物料件号或容器编号")
    @NotEmpty
    private String no;

    @ApiModelProperty("物料批次id")
    @NotNull
    private Long storageMaterialBatchId;

    @ApiModelProperty("生产计划id")
    private Long productPlanId;

}
