package com.bmos.mes.service.plan.production.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@ApiModel("生产计划下发返回值")
@Data
public class ProductionPlanIssueResVO {

    @ApiModelProperty("是否成功")
    private Boolean success;

    @ApiModelProperty("指令单编号重复集合")
    private List<String> planNoList = new ArrayList<>();

    @ApiModelProperty("工艺生产批号重复对象集合")
    private List<ProcessBatchNoVO> batchNoList = new ArrayList<>();

    @Data
    @ApiModel("工艺批号重复VO")
    public static class ProcessBatchNoVO {

        @ApiModelProperty("工艺id")
        private Long processId;

        @ApiModelProperty("批号")
        private String batchNo;

        @ApiModelProperty("工艺名称")
        private String processName;

    }

}
