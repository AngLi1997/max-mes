package com.bmos.mes.service.tag.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/9/23 10:15
 */
@Data
@ApiModel("皮重配置标签信息")
public class TareWeighTag {

    @ApiModelProperty(value = "id", example = "1")
    private Long id;

    @ApiModelProperty(value = "皮重", example = "1")
    private String tareWeigh;

    @ApiModelProperty(value = "单位", example = "1")
    private String unit;

    @ApiModelProperty(value = "单位id", example = "1")
    private Long unitId;

    @ApiModelProperty(value = "备注", example = "1")
    private String describe;
}
