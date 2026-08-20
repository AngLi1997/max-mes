package com.bmos.lims2.web.stability.plan.vo.request;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 稳定性时间点取样列表分页查询请求VO
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("稳定性时间点取样分页查询请求")
public class StabilityTimepointSampleQueryReqVO extends BasePage {

    @ApiModelProperty("样品编号（模糊）")
    private String sampleNo;

    @ApiModelProperty("批号（模糊）")
    private String batchNo;

    @ApiModelProperty("稳定性考察编号（模糊）")
    private String planCode;

    @ApiModelProperty("检验单号（模糊）")
    private String orderNo;

    @ApiModelProperty("请验时间起（含）")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime requestTimeStart;

    @ApiModelProperty("请验时间止（含）")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime requestTimeEnd;
}
