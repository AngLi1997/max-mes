package com.bmos.mes.service.preparation.measure.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("配液量取结果VO")
public class LiquidMeasureResultVO {

    /**
     * 称量人id
     */
    @ApiModelProperty(value = "称量人id", example = "1")
    private String measurerId;

    /**
     * 称量人名称
     */
    @ApiModelProperty(value = "称量人名称", example = "张三")
    private String measurerName;

    @ApiModelProperty("称量人登录名")
    private String measurerLoginName;

    /**
     * 复核人id
     */
    @ApiModelProperty(value = "复核人id", example = "1")
    private String reCheckerId;

    /**
     * 复核人名称
     */
    @ApiModelProperty(value = "复核人名称", example = "张三")
    private String reCheckerName;

    @ApiModelProperty("复核人登录名")
    private String reCheckerLoginName;

    @ApiModelProperty("配液量取结果")
    private List<MeasureResultRecordVO> measureList;

    @ApiModelProperty("余液量取结果")
    private List<MeasureResultRecordVO> oddMeasureList;



}
