package com.bmos.lims2.web.stability.review.vo.request;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 稳定性结果审核分页查询请求VO
 */
@Getter
@Setter
@ApiModel("稳定性结果审核分页查询")
public class StabilityResultReviewQueryReqVO extends BasePage {

    @ApiModelProperty("稳定性考察编号（模糊）")
    private String planCode;

    @ApiModelProperty("检验单号（模糊）")
    private String orderNo;

    @ApiModelProperty("批号")
    private String batchNo;

    @ApiModelProperty("检品ID（来自左侧分类树）")
    private Long materialId;

    @ApiModelProperty("检品分类ID（来自左侧分类树）")
    private Long categoryId;

    @ApiModelProperty("请验时间起")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime requestTimeStart;

    @ApiModelProperty("请验时间止")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime requestTimeEnd;
}
