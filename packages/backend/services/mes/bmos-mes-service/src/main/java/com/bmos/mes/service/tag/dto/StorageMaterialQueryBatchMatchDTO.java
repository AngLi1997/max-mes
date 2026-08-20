package com.bmos.mes.service.tag.dto;

import com.bmos.mes.service.storage.manage.dto.StorageMaterialQueryValidateDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("符合批次校验DTO")
public class StorageMaterialQueryBatchMatchDTO extends StorageMaterialQueryValidateDTO {

    @ApiModelProperty("物料批次id")
    private Long storageMaterialBatchId;

}
