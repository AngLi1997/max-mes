package com.bmos.lims2.web.inspect.sampling.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Pattern;

/**
 * @className: SamplingItemReqVO
 * @author: yigaohui
 * @date: 2025/8/18 11:43
 * @Version: 1.0
 * @description:
 */

@ApiModel("取样明细列表")
@Data
public class SamplingItemReqVO {
    @ApiModelProperty("样品ID")
    private Long id;

    @ApiModelProperty("样品编号")
    private String sampleNo;

    @ApiModelProperty("实际取样量")
    @Pattern(regexp = "^-?\\d{1,6}(\\.\\d{1,5})?$", message = "实际取样量最多整数6位，小数5位")
    private String quantity;

    @ApiModelProperty("取样单位")
    private Long unitId;
}
