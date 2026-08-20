package com.bmos.lims2.server.stability.review.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 稳定性结果审核分页查询DTO
 */
@Getter
@Setter
public class StabilityResultReviewQueryDTO extends BasePage {

    @ApiModelProperty("稳定性考察编号（模糊）")
    private String planCode;

    @ApiModelProperty("检验单号（模糊）")
    private String orderNo;

    @ApiModelProperty("批号（精确）")
    private String batchNo;

    @ApiModelProperty("检品ID（来自左侧分类树选择）")
    private Long materialId;

    @ApiModelProperty("检品ID集合（由 categoryId 解析后填入）")
    private List<Long> materialIds;

    @ApiModelProperty("请验时间起")
    private LocalDateTime requestTimeStart;

    @ApiModelProperty("请验时间止")
    private LocalDateTime requestTimeEnd;
}
