package com.bmos.mes.service.plan.info.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("查询产品已完成的生产批次DTO")
@Data
public class ProductPlanBatchDTO extends BasePage{

    @ApiModelProperty("工艺名称")
    private String processName;

    @ApiModelProperty("生产批号")
    private String batchNo;

    @ApiModelProperty("生产开始时间的起始时间")
    private String startTime;

    @ApiModelProperty("生产开始时间的结束时间")
    private String endTime;

    @ApiModelProperty("产品分类Id")
    private Long productCategoryId;

    @ApiModelProperty("产品id")
    private Long productId;

}
