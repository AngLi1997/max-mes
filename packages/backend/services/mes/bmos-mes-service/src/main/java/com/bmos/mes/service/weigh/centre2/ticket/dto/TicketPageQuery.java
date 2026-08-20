package com.bmos.mes.service.weigh.centre2.ticket.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author liang
 * @version 1.0.0
 * @date 2025/5/21 16:32
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TicketPageQuery extends BasePage {

    @ApiModelProperty(value = "工单编号", example = "TK2023120100001")
    private String ticketNo;

    @ApiModelProperty(value = "物料名称", example = "聚乙烯")
    private String materialName;

    @ApiModelProperty(value = "物料合并代码", example = "M1001")
    private String materialMergeCode;

    @ApiModelProperty(value = "称重中心名称", example = "中心A")
    private String weighCentreName;

    @ApiModelProperty(value = "执行时间开始", example = "2025-05-21")
    private String planDateStart;

    @ApiModelProperty(value = "执行时间结束", example = "2025-05-21")
    private String planDateEnd;
}
