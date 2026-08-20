package com.bmos.mes.service.plan.production.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

/**
 * @ClassName PlanBatchNextNoVO
 * @Description 生成批号返回vo
 * @Author Ren Jin Guang
 * @Date 2024/8/28 14:57
 */
@Setter
@Getter
@ToString
@ApiModel("生成批号返回vo")
public class PlanBatchNextNoVO {

    @ApiModelProperty("key")
    private Integer key;

    @ApiModelProperty("工艺id")
    private Long processId;

    @ApiModelProperty("生产指令单批号")
    private String planNo;

    @ApiModelProperty("生产批号")
    private String batchNo;

    @ApiModelProperty("排序号")
    private Integer sort;

    @ApiModelProperty("生产指令单规则编码")
    private String planNoCode;

    @ApiModelProperty("生产批号规则编码")
    private String batchNoCode;

    @ApiModelProperty("分组号")
    private Integer groupNumber;

    @ApiModelProperty("关联批次信息")
    private String productionBatchList;

    @ApiModelProperty("关联模板批次sort集合")
    private List<Integer> relationBatchSortList;

    @ApiModelProperty("是否沿用")
    private Boolean reuseBatchNumber;

    @ApiModelProperty("工艺名称")
    private String processName;

    @ApiModelProperty("指令单编号")
    private String productPlanType;

    @ApiModelProperty("是否已处理沿用")
    private Boolean isFlay;

    @ApiModelProperty("产线编码")
    private String productionLineCode;

    @ApiModelProperty("关联信息")
    private String relatedBatchInfo;

    @ApiModelProperty("批号")
    private List<String> batchNoList;

    @ApiModelProperty("计划开始日期")
    private LocalDate startTime;

    @ApiModelProperty("确认指令单编号回传时间")
    private LocalDate planNoCodeApplyTime;
}
