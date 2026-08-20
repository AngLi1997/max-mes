package com.bmos.lims2.server.report.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ApiModel("报告-已样品审核通过的检验单分页查询条件")
public class ReportEligibleOrderPageQueryDTO extends BasePage {

    @ApiModelProperty("物料ID集合")
    private List<Long> materialIds;

    @ApiModelProperty("检验单号")
    private String orderNo;

    @ApiModelProperty("批号")
    private String batchNo;

    @ApiModelProperty("请验时间开始")
    private LocalDateTime inspectionRequestTimeStart;

    @ApiModelProperty("请验时间结束")
    private LocalDateTime inspectionRequestTimeEnd;
}


