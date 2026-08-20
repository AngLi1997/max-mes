package com.bmos.mes.service.plan.production.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @ClassName ProductionPlan
 * @Description 生产计划表
 * @Author Ren Jin Guang
 * @Date 2024/8/27 16:34
 */
@TableName("bm_production_plan")
@Getter
@Setter
public class ProductionPlan extends BaseDO {

    @ApiModelProperty("计划名称")
    private String planName;

    @ApiModelProperty("生产指令单id")
    private Long planTemplateId;

    @ApiModelProperty("指令单类型")
    private String planType;

    @ApiModelProperty("首批生成日期")
    private LocalDate planFirstDate;

    @ApiModelProperty("计划数量")
    private Integer planNumber;

    @ApiModelProperty("间隔时长")
    private String duration;

    @ApiModelProperty("计划状态")
    private String planState;

    @TableField(exist = false)
    private LocalDate planEndDate;
}
