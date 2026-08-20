package com.bmos.lims2.server.eln.signature.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("根据权限码和生产计划查询用户DTO")
public class SignerQueryDTO {

    /**
     * 权限码
     */
    @ApiModelProperty(value = "权限码", required = true)
    private Long permissionCode;

    @ApiModelProperty("检验单id")
    private Long inspectionOrderId;

}
