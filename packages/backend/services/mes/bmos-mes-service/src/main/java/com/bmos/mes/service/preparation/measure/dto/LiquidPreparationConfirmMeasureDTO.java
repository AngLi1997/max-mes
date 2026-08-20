package com.bmos.mes.service.preparation.measure.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@ApiModel("确认量取DTO")
@Data
public class LiquidPreparationConfirmMeasureDTO {

    @ApiModelProperty("量取组件实例id")
    @NotNull
    private Long id;

    /**
     * 量取人id
     */
    @ApiModelProperty(value = "量取人id(首次确认时必填)", example = "1", required = true)
    private String measurerId;

    /**
     * 复核人id
     */
    @ApiModelProperty(value = "复核人id(首次确认时必填)", example = "1", required = true)
    private String reCheckerId;

    @ApiModelProperty(value = "配液批次id", required = true)
    @NotNull
    private Long planBatchId;

    /**
     * 消耗物料件id列表
     */
    @ApiModelProperty(value = "消耗物料件id列表")
    private List<Long> consumeStorateMaterialIdList = new ArrayList<>();

    @ApiModelProperty("备注")
    private String remark;


}
