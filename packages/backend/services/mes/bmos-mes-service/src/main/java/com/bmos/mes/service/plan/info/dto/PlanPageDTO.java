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
public class PlanPageDTO extends BasePage {
    @ApiModelProperty("计划编号")
    private String planNo;

    @ApiModelProperty("生产批号")
    private String batchNo;

    @ApiModelProperty("产品Id")
    private Long productId;

    @ApiModelProperty("产品名称")
    private String productName;

    @ApiModelProperty("生产工艺名称")
    private String processName;

    @ApiModelProperty("计划类型 PRODUCT 生产批次 EXPERIMENT 实验批次 VERIFY 验证批次")
    private String type;

    @ApiModelProperty("状态 编辑EDIT 审批中AUDIT 确认CONFIRM 废弃DISCARD")
    private String status;

    @ApiModelProperty("生产状态")
    private String productionStatus;

    @ApiModelProperty(value = "状态 待分解WAIT_DECOMPOSE 待确认WAIT_CONFIRM 待下发WAIT_SEND 已下发 SEND", hidden = true)
    private List<String> instructStatus;

    @ApiModelProperty(value = "进行状态", hidden = true)
    private String isStart;

    @ApiModelProperty(value = "暂停状态", hidden = true)
    private Boolean paused;

    @ApiModelProperty("类型，当为任务节点时需特殊处理")
    private String taskType;

    @ApiModelProperty("id集合")
    private List<Long> ids;

    @ApiModelProperty("计划id集合，数据权限使用")
    private List<Long> planIds;

    @ApiModelProperty("产品分类id")
    private Long productCategoryId;

    @ApiModelProperty("产线id列表")
    private List<Long> lineIdList;

    @ApiModelProperty("开始时间")
    private String startTime;

    @ApiModelProperty("结束时间")
    private String endTime;

    @ApiModelProperty("产品id列表")
    private List<Long> productIds;

    @ApiModelProperty(value = "负责人角色列表", hidden = true)
    private List<Long> principalRoleIds;

    @ApiModelProperty(value = "工艺id列表", hidden = true)
    private List<Long> processIds;

    @ApiModelProperty(value = "权限过滤后计划id")
    private Set<Long> teamPlanIdList;

    public void parseProductionStatus() {
        if (StrUtil.isEmpty(productionStatus)) {
            return;
        }
        ProductionStatusEnum status = ProductionStatusEnum.getEnumByValue(productionStatus);
        this.setInstructStatus(status.getInstructStatus().stream().map(ProductPlanInstructStatusEnum::getValue).collect(Collectors.toList()));
        this.setIsStart(status.getIsStart().getValue());
        if (Objects.equals(status, ProductionStatusEnum.PRODUCTION_PAUSED)) {
            this.setPaused(status.getPaused());
        }
    }
}
