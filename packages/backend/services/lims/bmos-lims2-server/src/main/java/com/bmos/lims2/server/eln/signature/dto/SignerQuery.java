package com.bmos.lims2.server.eln.signature.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * 签名人信息查询
 * @author liang
 * @version 1.0.0
 * @date 2024/5/24 10:16
 */
@Data
@ToString
@ApiModel("双签名人信息查询")
public class SignerQuery {

    /**
     * 生产工序步骤id
     */
    @ApiModelProperty(value = "生产工序步骤id", required = true)
    private Long procedureStepModelId;

    /**
     * 组件id
     */
    @ApiModelProperty(value = "组件id", required = true)
    private Long componentId;

    /**
     * 权限码
     */
    @ApiModelProperty(value = "权限码", required = true)
    private Long permissionCode;

    @ApiModelProperty("生产计划id")
    private Long productPlanId;
}
