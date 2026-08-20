package com.bmos.mes.service.weigh.centre.requirement.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * 称量需求分页查询参数
 * @author liang
 * @version 1.0.0
 * @date 2024/7/4 17:57
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("称量需求分页查询参数")
public class WeighRequirementPageQuery extends BasePage {

    /**
     * 物料名称
     */
    @ApiModelProperty(value = "物料名称", example = "氯化钠")
    @Length(max = 100)
    private String materialName;

    /**
     * 物料编码（合并编码）
     */
    @ApiModelProperty(value = "物料编码（合并编码）", example = "CNA")
    @Length(max = 100)
    private String materialMergeCode;

    /**
     * 称量中心名称
     */
    @ApiModelProperty(value = "称量中心名称", example = "称量中心1")
    @Length(max = 100)
    private String weighCentreName;

    /**
     * 需求时间开始
     */
    @ApiModelProperty(value = "需求时间开始", example = "2024-07-01")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate requirementDateStart;

    /**
     * 需求时间结束
     */
    @ApiModelProperty(value = "需求时间结束", example = "2024-07-01")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate requirementDateEnd;

    /**
     * 产品id
     */
    @ApiModelProperty(value = "产品id", example = "1")
    private Long productId;

    /**
     * 生产批号
     */
    @ApiModelProperty(value = "生产批号", example = "20240701")
    @Length(max = 100)
    private String batchNo;

    /**
     * 需求状态
     */
    @ApiModelProperty(value = "需求状态", example = "1")
    private Integer requirementStatus;
}
