package com.bmos.lims2.web.stability.plan.vo.request;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 稳定性考察计划查询请求VO
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("稳定性考察计划查询请求")
public class StabilityInspectPlanQueryReqVO extends BasePage {

    @ApiModelProperty("考察计划编号（模糊查询）")
    private String code;

    @ApiModelProperty("计划状态（PENDING/IN_PROGRESS/COMPLETED/PAUSED）")
    private String status;

    @ApiModelProperty("创建时间起（yyyy-MM-dd HH:mm:ss）")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTimeStart;

    @ApiModelProperty("创建时间止（yyyy-MM-dd HH:mm:ss）")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTimeEnd;

    @ApiModelProperty("检品分类ID（用于左侧树过滤）")
    private Long materialCategoryId;

    @ApiModelProperty("检品ID")
    private Long materialId;
}
