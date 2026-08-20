package com.bmos.mes.service.plan.template.vo;

import cn.hutool.core.util.StrUtil;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.mes.service.plan.template.dto.RelationProcessDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@ApiModel("模板详情批次VO")
@Data
public class PlanTemplateDetailBatchVO {

    @ApiModelProperty("批次详情id")
    private Long templateBatchId;

    @ApiModelProperty("关联生产计划模板id")
    private Long planTemplateId;

    private String processKey;

    /**
     * 工艺id
     */
    @ApiModelProperty("工艺id")
    private Long processId;

    @ApiModelProperty("产品名称")
    private String productName;

    @ApiModelProperty("产品合并编码")
    private String productMergeCode;

    @ApiModelProperty("产品规格")
    private String productSpecification;

    @ApiModelProperty("工艺名称")
    private String processName;
    @ApiModelProperty(value = "内包规格")
    private String innerPackingSpecification;

    @ApiModelProperty(value = "包装规格")
    private String packingSpecification;

    @ApiModelProperty("产品标识")
    private String productMark;

    @ApiModelProperty("是否沿用批号")
    private Boolean reuseBatchNumber;


    /**
     * 工艺版本
     */
    @ApiModelProperty("工艺版本")
    private String processVersion;

    @ApiModelProperty("工艺当前生效版本")
    private String activeVersion;

    /**
     * 间隔时长
     */
    @ApiModelProperty("间隔时长")
    private Integer intervalDuration;

    /**
     * 执行时长
     */
    @ApiModelProperty("执行时长")
    private Integer executionDuration;

    /**
     * 产线id
     */
    @ApiModelProperty("产线id")
    private Long productionLineId;

    @ApiModelProperty("产线名称")
    private String productionLineName;

    @ApiModelProperty("产线编码")
    private String productionLineCode;

    /**
     * 生产批量
     */
    @ApiModelProperty("生产批量")
    private BigDecimal batchQuantity;

    @ApiModelProperty("生产批量单位")
    private Long unitId;

    @ApiModelProperty("生产批量单位名称")
    private String unitName;

    @ApiModelProperty("前端使用配置")
    private String relationProcessesList;

    /**
     * 沿用批号批次index
     */
    @ApiModelProperty("沿用批号批次index")
    private Integer followBatchSort;

    /**
     * 关联模板批次sort集合
     */
    @ApiModelProperty("关联模板批次sort集合")
    private List<Integer> relationBatchSortList;

    /**
     * 工序执行时长配置
     */
    @ApiModelProperty("工序执行时长配置")
    @JsonIgnore
    private String procedureConfig;

    @ApiModelProperty("工序列表")
    private List<PlanTemplateProcedureVO> procedureList;

    /**
     * 批次排序
     */
    @ApiModelProperty("批次排序")
    private Integer sort;

    public List<RelationProcessDTO> getRelationProcessesList() {
        if (StrUtil.isEmpty(relationProcessesList)) {
            return new ArrayList<>();
        }
        return JsonUtils.parseArray(relationProcessesList, RelationProcessDTO.class);
    }

}
