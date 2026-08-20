package com.bmos.mes.service.plan.production.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

/**
 * @ClassName PlanBatchNoDTO
 * @Description 生成编号dto
 * @Author Ren Jin Guang
 * @Date 2024/8/28 15:12
 */
@Setter
@Getter
@ToString
@ApiModel("生成编号dto")
public class PlanBatchNoDTO {

    @ApiModelProperty("工艺id")
    @NotNull
    private Long processId;

    @ApiModelProperty("工艺名称")
    @NotBlank
    private String processName;

    @ApiModelProperty("产线id")
    @NotNull
    private Long productionLine;

    @ApiModelProperty("产线编码")
    @NotBlank
    private String productionLineCode;

    @ApiModelProperty("是否沿用")
    @NotNull
    private Boolean reuseBatchNumber;

    @ApiModelProperty("关联模板批次sort集合")
    private List<Integer> relationBatchSortList;

    @ApiModelProperty("批次排序")
    @NotNull
    private Integer sort;

    @ApiModelProperty("分组信息")
    @NotNull
    private Integer groupNumber;

    @ApiModelProperty("指令单编码")
    @NotBlank
    private String productPlanType;

    @ApiModelProperty("批号")
    private List<String> batchNoList;

    @ApiModelProperty("关联信息")
    private String relatedBatchInfo;

    @ApiModelProperty("计划开始日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startTime;
}
