package com.bmos.mes.service.weigh.data.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/11/12 18:17
 */
@Data
@ApiModel("保存称量数据dto")
public class WeighDataDTO {

    /**
     * 重量
     */
    @ApiModelProperty(value = "重量", example = "1")
    private String weight;

    /**
     * 单位id
     */
    @ApiModelProperty(value = "单位id", example = "1")
    private Long unitId;

    /**
     * 称量数据组件实例id
     */
    @ApiModelProperty(value = "称量数据组件实例id", example = "1")
    private Long componentInstanceId;
}
