package com.bmos.mes.service.weigh.centre.execute.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.Optional;

/**
 * 称量执行任务分页查询结果
 * @author liang
 * @version 1.0.0
 * @date 2024/7/10 10:46
 */
@Data
@ApiModel("称量执行任务分页查询结果")
public class WeighExecuteTaskPageVO {

    @ApiModelProperty(value = "称量任务id", example = "1")
    private Long id;

    @ApiModelProperty(value = "物料名称", example = "氯化钠")
    private String materialName;

    @ApiModelProperty(value = "物料合并编码", example = "WH03")
    private String materialMergeCode;

    @ApiModelProperty(value = "称量中心编码", example = "KQ-PYCL")
    private String weighCentreCode;

    @ApiModelProperty(value = "称量中心名称", example = "狂犬疫苗配液称")
    private String weighCentreName;

    @ApiModelProperty(value = "称量任务编号", example = "1")
    private String taskNo;

    @ApiModelProperty(value = "下发时间", example = "2024-07-01 15:21:24")
    private String sendTime;

    @ApiModelProperty(value = "执行时间", example = "2024-07-01")
    private String executeDate;

    @ApiModelProperty(value = "完成时间", example = "2024-07-01 15:21:24")
    private String finishTime;

    @ApiModelProperty(value = "物料规格", example = "8.000")
    private String materialSpecification;

    @ApiModelProperty(value = "是否过期 true 过期 false 未过期", example = "true")
    private Boolean isExpired = Optional.ofNullable(executeDate)
            .map(d -> LocalDate.now().isAfter(LocalDate.parse(d)))
            .orElse(null);
}
