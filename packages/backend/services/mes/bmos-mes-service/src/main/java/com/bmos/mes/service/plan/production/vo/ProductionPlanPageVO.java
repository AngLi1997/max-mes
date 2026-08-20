package com.bmos.mes.service.plan.production.vo;

import com.bmos.mes.common.enums.plan.ProductPlanTypeEnum;
import com.bmos.mes.common.enums.plan.ProductionPlanStateEnum;
import com.bmos.mes.service.utils.UserUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @ClassName ProductionPlanPageVO
 * @Description 生产计划管理页vo
 * @Author Ren Jin Guang
 * @Date 2024/8/27 17:05
 */
@Setter
@Getter
@ToString
@ApiModel("生产计划管理页vo")
public class ProductionPlanPageVO {

    @ApiModelProperty("计划id")
    private Long id;

    @ApiModelProperty("计划名称")
    private String planName;

    @ApiModelProperty("指令单类型")
    private String planType;

    @ApiModelProperty("生产指令单数量")
    private Integer planNumber;

    @ApiModelProperty("计划首批生产日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate planFirstDate;

    @ApiModelProperty("计划生产结束日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate planEndDate;

    @ApiModelProperty("创建人")
    private String createBy;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("计划状态")
    private ProductionPlanStateEnum planState;

    @ApiModelProperty("计划状态名称")
    private String planStateName;

    public String getPlanType(){
        return ProductPlanTypeEnum.getNameByValue(planType);
    }

    public String getCreateBy(){
        return UserUtils.getUsername(createBy);
    }
}
