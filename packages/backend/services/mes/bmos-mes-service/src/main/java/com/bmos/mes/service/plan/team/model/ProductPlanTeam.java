package com.bmos.mes.service.plan.team.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.bmos.mes.common.enums.BooleanEnum;
import com.bmos.mybatis.dataobject.BaseDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.With;
import lombok.experimental.SuperBuilder;
import lombok.experimental.Tolerate;

import java.util.List;

/**
* 生产计划班组
*/
@Getter
@Setter
@SuperBuilder
@With
@AllArgsConstructor
@ToString
@TableName(value = "bm_product_plan_team", autoResultMap = true)
public class ProductPlanTeam extends BaseDO {
    @Tolerate
    public ProductPlanTeam() {}
    @ApiModelProperty("班组名称")
    private String name;

    @ApiModelProperty("班组编码")
    private String code;

    @ApiModelProperty("班组描述")
    private String description;

    @TableField(typeHandler = JacksonTypeHandler.class)
    @ApiModelProperty("班组人员 json数据")
    private List<String> people;

    @ApiModelProperty("状态 TRUE 启用 FALSE 禁用")
    private BooleanEnum status;
}
