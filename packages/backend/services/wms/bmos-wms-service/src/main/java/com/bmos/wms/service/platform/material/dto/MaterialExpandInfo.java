package com.bmos.wms.service.platform.material.dto;

import com.bmos.web.swagger.base.ApiModelEnumProperty;
import com.bmos.wms.common.enums.TimeUnitEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@ApiModel("物料拓展信息")
@Getter
@Setter
public class MaterialExpandInfo {

    @ApiModelProperty("供应商")
    private String supplier;

    @ApiModelProperty("生产商")
    private String producer;

    @ApiModelProperty("级别")
    private String level;

    @ApiModelProperty("预置皮重")
    private BigDecimal presetTareWeight;

    @ApiModelProperty("剂型")
    private String formulation;

    @ApiModelProperty("默认效期")
    private Integer defaultExpiration;

    @ApiModelEnumProperty(value = "默认效期时间单位", enumClass = TimeUnitEnum.class)
    private Integer timeUnit;

}
