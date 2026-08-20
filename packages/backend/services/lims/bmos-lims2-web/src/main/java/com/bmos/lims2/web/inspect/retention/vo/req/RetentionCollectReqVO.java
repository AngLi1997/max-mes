package com.bmos.lims2.web.inspect.retention.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Description: 留样样品领用请求VO
 * @Author: yigaohui
 * @Date: 2026/02/09
 */
@Data
@ApiModel("留样样品领用请求")
public class RetentionCollectReqVO {

    @ApiModelProperty(value = "领用数量", required = true)
    @NotBlank(message = "领用数量不能为空")
    private String collectQuantity;

    @ApiModelProperty(value = "单位ID", required = true)
    @NotNull(message = "单位ID不能为空")
    private Long unitId;

    @ApiModelProperty("领用原因")
    private String collectReason;

    @ApiModelProperty(value = "领样人ID", required = true)
    @NotBlank(message = "领样人ID不能为空")
    private String collectorId;

    @ApiModelProperty(value = "领样人名称", required = true)
    @NotBlank(message = "领样人名称不能为空")
    private String collectorName;
}
