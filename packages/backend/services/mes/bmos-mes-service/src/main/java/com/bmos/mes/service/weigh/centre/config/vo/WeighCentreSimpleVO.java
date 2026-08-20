package com.bmos.mes.service.weigh.centre.config.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 称量中心基本vo
 * @author liang
 * @version 1.0.0
 * @date 2024/7/8 15:31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("称量中心基本vo")
public class WeighCentreSimpleVO {


    @ApiModelProperty(value = "称量中心id", example = "1")
    private Long id;

    @ApiModelProperty(value = "称量中心名称", example = "称量中心名称")
    private String name;

    @ApiModelProperty(value = "称量中心编码", example = "编码")
    private String code;
}
