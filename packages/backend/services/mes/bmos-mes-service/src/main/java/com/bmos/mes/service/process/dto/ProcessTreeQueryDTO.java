package com.bmos.mes.service.process.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("工艺树查询DTO")
@Data
public class ProcessTreeQueryDTO {

    @ApiModelProperty("查询激活工艺")
    private Boolean activeProcess;

    @ApiModelProperty("过滤部门权限")
    private Boolean filterPermission;

}
