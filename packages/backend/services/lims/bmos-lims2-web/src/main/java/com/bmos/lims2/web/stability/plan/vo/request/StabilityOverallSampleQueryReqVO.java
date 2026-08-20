package com.bmos.lims2.web.stability.plan.vo.request;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 稳定性整体样品分页查询请求VO
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("稳定性整体样品分页查询请求")
public class StabilityOverallSampleQueryReqVO extends BasePage {

    @ApiModelProperty("稳定性考察计划编号（模糊）")
    private String planCode;

    @ApiModelProperty("批号（模糊）")
    private String batchNo;

    @ApiModelProperty("检品ID")
    private Long materialId;

    @ApiModelProperty("物料分类ID")
    private Long categoryId;

    @ApiModelProperty("请验时间起（计划创建时间，含）")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTimeStart;

    @ApiModelProperty("请验时间止（计划创建时间，含）")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTimeEnd;
}
