package com.bmos.mes.service.workflow.change.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.bmos.mybatis.dataobject.BaseDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassName ProductChangeTeam
 * @Description TODO
 * @Author Ren Jin Guang
 * @Date 2024/8/16 14:02
 */
@TableName(value = "bm_product_change_team",autoResultMap = true)
@Setter
@Getter
@ToString
@ApiModel("换班班次信息表")
public class ProductChangeTeam extends BaseDO {

    @ApiModelProperty("生产计划班次表信息")
    private Long productInstructionTeamId;

    @TableField(typeHandler = JacksonTypeHandler.class)
    @ApiModelProperty("班组id集合")
    private List<Long> teamIds;

    @ApiModelProperty("换班次数")
    private Integer changeTeamNumber;

    @ApiModelProperty("换班类型")
    private String changeTeamType;

}
