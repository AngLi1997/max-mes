package com.bmos.lims2.server.report.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @Description: 已生成报告-分离返回结构
 * @Author: yigaohui
 * @Date: 2025/09/09 00:00
 */
@Getter
@Setter
@ApiModel("已生成报告-分离返回结构")
public class ReportGeneratedListDTO {

    @ApiModelProperty("检验单信息")
    private ReportOrderInfoDTO orderInfo;

    @ApiModelProperty("报告列表（按生成时间倒序，不分页）")
    private List<ReportGeneratedItemDTO> reports;
}


