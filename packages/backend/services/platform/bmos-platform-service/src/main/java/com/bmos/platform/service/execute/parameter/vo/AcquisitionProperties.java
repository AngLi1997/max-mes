package com.bmos.platform.service.execute.parameter.vo;

import com.bmos.platform.service.equipment.datasource.config.HubProperties;
import com.bmos.platform.service.equipment.datasource.config.SupConProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName BusinessParameterValueDetailVO
 * @Description
 * @Author Ren Jin guang
 * @Date 2024/7/29 15:28
 */
@Data
public class AcquisitionProperties {

    @ApiModelProperty("hub配置")
    private HubProperties hub;

    @ApiModelProperty
    private SupConProperties supCon;
}
