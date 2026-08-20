package com.bmos.mes.service.plan.production.vo;

import com.bmos.mes.common.enums.plan.ProductPlanTypeEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassName ProductionPlanDetailVO
 * @Description 计划查看详情vo
 * @Author Ren Jin Guang
 * @Date 2024/8/27 18:41
 */
@Setter
@Getter
@ToString
@ApiModel("生产计划查看详情vo")
public class ProductionPlanDetailVO {

    @ApiModelProperty("计划名称")
    private String planName;

    @ApiModelProperty("生产计划模板id")
    private String planTemplateName;

    @ApiModelProperty("指令单类型")
    private String planType;

    @ApiModelProperty("首批生成日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate planFirstDate;

    @ApiModelProperty("计划数量")
    private Integer planNumber;

    @ApiModelProperty("间隔时长")
    private String duration;

    @ApiModelProperty("计划详情数据")
    private List<List<ProductionPlanItemDetailVO>> planDetailVOList;

    public String getPlanType() {
        return ProductPlanTypeEnum.getNameByValue(planType);
    }

}
