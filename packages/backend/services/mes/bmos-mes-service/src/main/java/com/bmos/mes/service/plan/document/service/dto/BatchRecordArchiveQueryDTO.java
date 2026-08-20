package com.bmos.mes.service.plan.document.service.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("批次批记录列表查询DTO")
@Data
public class BatchRecordArchiveQueryDTO extends BasePage {

    @ApiModelProperty("生产批次id")
    private Long planId;

    @ApiModelProperty("工艺id")
    private Long processId;

    @ApiModelProperty("部门id列表")
    private List<Long> deptIds;

    @ApiModelProperty("版本状态值")
    private Integer statusValue;

    @ApiModelProperty("模板id列表")
    private List<Long> templateIdList;

}
