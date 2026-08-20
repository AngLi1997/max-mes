package com.bmos.mes.service.plan.template.model;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.bmos.mybatis.dataobject.BaseDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * 生产计划模板批次信息
 */
@Getter
@Setter
@TableName(value = "bm_plan_template_batch", autoResultMap = true)
public class PlanTemplateBatch extends BaseDO {

    /**
     * 关联生产计划模板id
     */
    private Long planTemplateId;

    /**
     * 工艺id
     */
    private Long processId;

    /**
     * 工艺名称
     */
    private String processName;

    /**
     * 前端使用key
     */
    private String processKey;

    /**
     * 工艺版本
     */
    private String processVersion;

    /**
     * 间隔时长
     */
    private Integer intervalDuration;

    /**
     * 执行时长
     */
    private Integer executionDuration;

    /**
     * 产线id
     */
    private Long productionLineId;

    /**
     * 产线名称,前端回显使用
     */
    private String productionLineName;

    /**
     * 产线编码,用于生成计划编码批次编码
     */
    private String productionLineCode;


    private Long unitId;

    /**
     * 生产批量
     */
    private BigDecimal batchQuantity;

    /**
     * 沿用批号批次index
     */
    private Integer followBatchSort;

    /**
     * 是否沿用批号
     */
    private boolean reuseBatchNumber;

    /**
     * 关联模板批次sort集合
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Integer> relationBatchSortList;

    @ApiModelProperty("关联工艺列表,前端使用")
    private String relationProcessesList;

    /**
     * 工序执行时长配置
     */
    private String procedureConfig;

    /**
     * 批次排序
     */
    private Integer sort;

    @NotNull
    @ApiModelProperty("产品Id")
    private Long productId;

    @NotEmpty
    @ApiModelProperty("产品名称")
    private String productName;

    @NotEmpty
    @ApiModelProperty("产品编码")
    private String productMergeCode;

    @NotEmpty
    @ApiModelProperty("产品规格")
    private String productSpecification;

    @ApiModelProperty("内包规格")
    private String innerPackingSpecification;

    @ApiModelProperty("包装规格")
    private String packingSpecification;

    @ApiModelProperty("产品标识")
    private String productMark;

}
