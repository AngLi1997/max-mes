package com.bmos.mes.service.weigh.centre.input.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 物料投入记录vo
 * @author liang
 * @version 1.0.0
 * @date 2024/7/18 15:30
 */
@Data
@ApiModel("物料投入记录vo")
public class WeighInputRecordResultVO {

    @ApiModelProperty(value = "是否已经完成投料", example = "true")
    private Boolean finished;

    @ApiModelProperty(value = "是否可以完成投料", example = "true")
    private Boolean canFinished;

    @ApiModelProperty(value = "物料投入记录列表")
    private List<WeighInputRecordVO> list;
}
