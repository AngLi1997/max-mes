package com.bmos.mes.service.storage.manage.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel("物料件列表VO")
@Getter
@Setter
public class MaterialPartListVO {

    @ApiModelProperty("物料件id")
    private Long id;

    @ApiModelProperty("物料件号")
    private String materialNo;

}
