package com.bmos.mes.service.plan.document.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 生成的批记录操作DTO
 */
@Getter
@Setter
@ApiModel("生成批记录操作DTO")
public class BatchRecordArchiveOperateDTO {

    /**
     * 生成批记录id
     */
    @ApiModelProperty(value = "生成批记录id", required = true)
    @NotNull
    private Long archiveId;

    /**
     * 排序的id集合
     */
    @ApiModelProperty("排序的生产计划id集合")
    private List<Long> sortPlanIdList;

}
