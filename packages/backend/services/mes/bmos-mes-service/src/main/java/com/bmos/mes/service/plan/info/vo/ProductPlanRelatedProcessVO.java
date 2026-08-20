package com.bmos.mes.service.plan.info.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("生产计划关联工艺列表")
@Data
public class ProductPlanRelatedProcessVO {

    @ApiModelProperty("工艺id")
    private Long processId;

    @ApiModelProperty("工艺名称")
    private String processName;

    @ApiModelProperty("关联批次列表")
    private List<PlanRelatedBatchVO> relationBatchList;

    @Data
    public static class PlanRelatedBatchVO {

        @ApiModelProperty("生产计划id")
        private Long planId;

        @ApiModelProperty("生产计划批号")
        private String planBatchNo;

        @ApiModelProperty("是否被其他批次关联")
        private Boolean related;

    }

}
