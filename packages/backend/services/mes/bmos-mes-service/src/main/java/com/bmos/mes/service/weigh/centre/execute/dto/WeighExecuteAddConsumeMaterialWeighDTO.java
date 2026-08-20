package com.bmos.mes.service.weigh.centre.execute.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 物料称量添加物料dto
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/7/10 16:25
 */
@ApiModel("物料称量添加物料dto")
@Data
public class WeighExecuteAddConsumeMaterialWeighDTO {

    /**
     * 需求id
     */
    @ApiModelProperty(value = "需求id", example = "1", required = true)
    @NotNull
    private Long requirementId;

    /**
     * 消耗物料件id列表
     */
    @ApiModelProperty(value = "消耗物料件id列表")
    private List<Long> consumeStorateMaterialIdList = new ArrayList<>();
}
