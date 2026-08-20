package com.bmos.lims2.server.report.dto;

import com.bmos.mybatis.page.BasePage;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ApiModel("报告审批分页查询参数")
public class ReportApprovalPageQueryDTO extends BasePage {

    @ApiModelProperty("检验单号")
    private String orderNo;

    @ApiModelProperty("检品名称")
    private String materialName;

    @ApiModelProperty("检品编码")
    private String materialCode;

    @ApiModelProperty("批次号")
    private String batchNo;

    @ApiModelProperty("请验时间起")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime inspectionRequestTimeStart;

    @ApiModelProperty("请验时间止")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime inspectionRequestTimeEnd;

    @ApiModelProperty("检品ID集合过滤")
    private List<Long> materialIds;
}



