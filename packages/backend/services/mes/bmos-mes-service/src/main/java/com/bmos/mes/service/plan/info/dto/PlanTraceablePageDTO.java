package com.bmos.mes.service.plan.info.dto;

import com.bmos.mybatis.page.BasePage;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ApiModel("PlanPageDTO:生产计划追溯分页列表查询条件DTO")
public class PlanTraceablePageDTO extends BasePage {
    @JsonIgnore
    @ApiModelProperty("计划编号")
    private List<Long> deptIds;

    @ApiModelProperty("生产批号")
    private String batchNo;

    @ApiModelProperty("生产工艺名称")
    private String processName;

    @ApiModelProperty("产品分类id")
    private Long productCategoryId;

    @ApiModelProperty("是否成品")
    private Boolean finishedProduct;

    @ApiModelProperty("产品id")
    private List<Long> productIds = new ArrayList<>();

    @ApiModelProperty("生产开始时间的起始时间")
    private String startTime;

    @ApiModelProperty("生产开始时间的结束时间")
    private String endTime;
}
