package com.bmos.mes.service.plan.info.dto;

import cn.hutool.core.util.StrUtil;
import com.bmos.mes.common.enums.plan.ProductPlanInstructStatusEnum;
import com.bmos.mes.common.enums.plan.ProductionStatusEnum;
import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@ApiModel("PlanPageDTO:生产计划分页列表查询条件DTO")
public class PlanPageCountDTO{
    @ApiModelProperty("计划编号")
    private String planNo;

    @ApiModelProperty("生产批号")
    private String batchNo;

    @ApiModelProperty("产品Id")
    private Long productId;

    @ApiModelProperty("计划类型 PRODUCT 生产批次 EXPERIMENT 实验批次 VERIFY 验证批次")
    private String type;

    @ApiModelProperty("状态 编辑EDIT 审批中AUDIT 确认CONFIRM 废弃DISCARD")
    private String status;

    @ApiModelProperty(value = "进行状态", hidden = true)
    private String isStart;

    @ApiModelProperty("类型，当为任务节点时需特殊处理")
    private String taskType;

    @ApiModelProperty("id集合")
    private List<Long> ids;

    @ApiModelProperty("计划id集合，数据权限使用")
    private List<Long> planIds;

    @ApiModelProperty("产品分类id")
    private Long productCategoryId;

    @ApiModelProperty("产线id")
    private Long lineId;

    @ApiModelProperty("产品物料id")
    private Long productMaterialId;

    @ApiModelProperty("开始时间")
    private String startTime;

    @ApiModelProperty("结束时间")
    private String endTime;

    @ApiModelProperty("产品id列表")
    private List<Long> productIds;




}
