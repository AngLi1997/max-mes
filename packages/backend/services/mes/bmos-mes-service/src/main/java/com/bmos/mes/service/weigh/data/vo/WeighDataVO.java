package com.bmos.mes.service.weigh.data.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 称量数据记录vo
 * @author liang
 * @version 1.0.0
 * @date 2024/11/13 09:40
 */
@Data
@ApiModel("称量数据记录vo")
public class WeighDataVO {

    @ApiModelProperty(value = "id", example = "id")
    private Long id;

    @ApiModelProperty(value = "重量", example = "1")
    private String weight;

    @ApiModelProperty(value = "单位", example = "kg")
    private String unit;

    @ApiModelProperty(value = "称重人员", example = "张三")
    private String weigher;

    @ApiModelProperty(value = "称重时间", example = "2024-11-13 09")
    private LocalDateTime weighTime;
}
